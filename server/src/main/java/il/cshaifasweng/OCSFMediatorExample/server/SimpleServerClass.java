package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.LogInEntities.AuthenticationService;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.*;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.MoneyRelatedServices.RefundChart;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import il.cshaifasweng.customerCatalogEntities.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static  final DataBaseManipulation<PricingChart> pChart = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<ParkingLot> pLot = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<Order> orderHandler = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<RegisteredCustomer> rCustomer = new DataBaseManipulation<>();
    private static Map<Long, Customer> clientsCustomersMap = new HashMap<>();
    private static Map<Long, Employee> clientsEmployeeMap = new HashMap<>();
    private static  final DataBaseManipulation<Complaint> complaintHandler = new DataBaseManipulation<>();
    private static  final  DataBaseManipulation<ParkingLotEmployee> plEmployee = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<ParkingLotManager> plManager = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<CustomerServiceEmployee> csEmployee = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<GlobalManager> globalManager = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<Customer> customerHandler = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<FullSubscription> fullSubHandler=new DataBaseManipulation<>();
    private  static final DataBaseManipulation<RegularSubscription> regularSubHandler=new DataBaseManipulation<>();
    private  static final DataBaseManipulation<RefundChart> refundChartHandler=new DataBaseManipulation<>();
    private  static final DataBaseManipulation<ParkingSpot> pSpot=new DataBaseManipulation<>();
    private static Session handleMessegesSession;
    static Session handleDelaysAndPenaltiesSession;
    public ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    public ScheduledFuture<?> HandleOnTimeOrderDelays, HandleSubsReminders;

    public SimpleServerClass(int port) {
        super(port);
        handleMessegesSession = DAO.factory.openSession();
        DataBaseManipulation.intiate(handleMessegesSession);
        AuthenticationService.intiate(handleMessegesSession);
        System.out.println("messegesSession is open");

//        handleDelaysAndPenaltiesSession = MySQL.getSessionFactory().openSession();
        // TODO: 06/02/2023 testing purposes only !!
//            HandleOnTimeOrderDelays = executorService.scheduleAtFixedRate(new handleOrderesAndPenalties(this), 0, 1, TimeUnit.SECONDS);
//            HandleSubsReminders = executorService.scheduleAtFixedRate(new HandleSubscriptionReminders(this),0, 1, TimeUnit.SECONDS);
        // TODO: 06/02/2023  should be working correctly use these lines in final project !
                HandleOnTimeOrderDelays = executorService.scheduleAtFixedRate(new handleOrderesAndPenalties(this), 0, 1, TimeUnit.MINUTES);
                HandleSubsReminders = executorService.scheduleAtFixedRate(new HandleSubscriptionReminders(this), HandleSubscriptionReminders.getDelay(), TimeUnit.HOURS.toSeconds(24), TimeUnit.SECONDS);

    }

    @Override
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        int id = (Integer) client.getInfo("userId");
        String type = (String) client.getInfo("userType");
        if (type.startsWith(RegisteredCustomer.class.getName()) || type.startsWith(OneTimeCustomer.class.getName()))
            clientsCustomersMap.remove(id);
        else {
            clientsEmployeeMap.remove(id);
        }

    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        if (!handleMessegesSession.isOpen()){
            handleMessegesSession = DAO.factory.openSession();
            DataBaseManipulation.intiate(handleMessegesSession);
            AuthenticationService.intiate(handleMessegesSession);
        }
        try {

            handleMessegesSession.beginTransaction();
            if (request.isBlank()) {
                message.setMessage("Error! we got an empty message");
            } else if (request.startsWith("#LogIn")) {
                Login(message,client);

            }else if (request.startsWith("#Register")) {
                registerUser(message,client);
            } else if (request.startsWith("#intializeParkingLot")) {
                initializeParkingLot(message, client);
            } else if (request.startsWith("#getAllParkingLots")) {
                sendParkingLots(message, client);

            }else if (request.startsWith("#placeOrder")) {
                placeOrder(message, client);
            }else if (request.startsWith("#getUser")) {
                getUser(message, client);
            }
            else if (request.startsWith("#getPricingChart")) {
                sendPricesChart(message, client);

            } else if (request.startsWith("#updatePrice")) {
                System.out.println("Update");
                updatePriceChart(message, client);
            } else if (request.startsWith("#updateAmount")) {
                updateSubscriptionAmount(message, client);
            } else if (request.startsWith("#DirectToAvailblePark")) {
                diretToParkingLots(message, client);
            } else if (request.startsWith("#GetParkingSpots")) {
                getParkingSpots(message, client);
            } else if (request.startsWith("#SetParkingSpots")) {
                setParkingSpots(message, client);
            } else if (request.startsWith("#showOrders")) {
                showOrders(message, client);
            } else if (request.startsWith("#showSubscription")) {
                showSubscription(message, client);

            }else if (request.startsWith("#addSubscription")) {
                addSubscription(message, client);

            } else if (request.startsWith("#cancelOrder")) {
                cancelOrder(message, client);

            } else if (request.startsWith("#cancelSubscription")) {
                cancelSubscription(message, client);
            }
            else if(request.startsWith("#GetRefundChart")){
                getRefundChart(message,client);
            }
            else if (request.startsWith("#ConnectionAlive")) {
                System.out.println("connection alive");
            } else if (request.startsWith("#getAllOrders")) {
                System.out.println("get all orders");
                getCustomersOrders(message, client);
            } else if (request.startsWith("#applyComplaint")) {
                applyCompaint(message, client);
            } else if (request.startsWith("#GetAllCompliants")) {
                System.out.println("Got the message");
                showComplaints(message, client);
            } else if (request.startsWith("#CloseComplaint")) {
                System.out.println("ClosingCompliant");
                closeCompliants(message, client);
            } else if (request.startsWith("#verifySubscription")) {
                verifySubscription(message, client);
            } else if (request.startsWith("#verifyOrder")) {
                verifyOrder(message, client);
            } else if (request.startsWith("#GetCustomerCars")) {
                getCustomerCars(message, client);
            } else if (request.startsWith("#CancelOrderAndGetRefund")) {
                cancelOrderAndGetRefund(message, client);

            } else {
                System.out.println("message content doesn't match any request");
            }
            client.sendToClient(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            handleMessegesSession.getTransaction().commit();
            handleMessegesSession.close();
        }
    }

    private void cancelOrderAndGetRefund(Message message, ConnectionToClient client) {
        String[] instructions=message.getMessage().split("&");

        Order order=orderHandler.get(Integer.parseInt(instructions[2]), Order.class) ;
        order.setActive(false);
        orderHandler.update(order);
        int customer= (int) client.getInfo("userId");
        Refund refund=new Refund("Calcelation",Double.parseDouble(instructions[1]),rCustomer.get( customer , RegisteredCustomer.class));
        refund.setTransaction_method(order.getTransaction_method());
        refund.setTransactionStatus(true);
        handleMessegesSession.save(refund);
        message.setMessage("#CancelOrderAndGetRefund");

    }

    private void getRefundChart(Message message, ConnectionToClient client) throws IOException {
        message.setObject(refundChartHandler.getAll(RefundChart.class));
        
    }
    private void applyCompaint(Message message, ConnectionToClient client) throws IOException {
        Complaint complaint = (Complaint) message.getObject();
//        "#applyComplaint&"+firstName+"&"+LastName+"&"+customerID+"&"+email+"&"+parkingLot
        String[] lazyElements=message.getMessage().split("&");
        if (!lazyElements[5].startsWith("null"))
            complaint.setParkingLot(pLot.get(Integer.parseInt(lazyElements[5]), ParkingLot.class));
        Customer customer = customerHandler.get(Integer.parseInt(lazyElements[3]), Customer.class);
        if (customer == null) {
            customer = new OneTimeCustomer(Integer.parseInt(lazyElements[3]), lazyElements[1], lazyElements[2], lazyElements[4], "");
            handleMessegesSession.save(customer);
        }
        complaint.setCustomer(customer);
        handleMessegesSession.save(complaint);
        handleMessegesSession.update(customer);
        handleMessegesSession.flush();

    }

    private void verifyOrder(Message message, ConnectionToClient client) throws IOException {
        String request = message.getMessage();
        String[] args;
        args = request.split("&");
        int parkingLotId;
        int customerID;
        int orderID;

        parkingLotId = Integer.parseInt(args[1]);
        customerID = Integer.parseInt(args[2]);
        orderID = Integer.parseInt(args[3]);
        String queryOnOrder = "SELECT registeredCustomer FROM Order o "
                + "WHERE o.id = :orderID "
                + "AND o.registeredCustomer.id = :customerId "
                + "AND o.parkingLotID.id = :parkingLotId "
                + "AND o.date = CURDATE()";
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderID", orderID);
        params.put("customerId", customerID);
        params.put("parkingLotId", parkingLotId);
        List<Object> lst = rCustomer.executeQuery(Object.class, queryOnOrder, params);
        if (lst != null && lst.size() > 0) {
            message.setObject(lst.get(0));
        } else {
            message.setObject(null);
        }
    }

    private void verifySubscription(Message message, ConnectionToClient client) throws IOException {
        String request = message.getMessage();
        String[] args;
        args = request.split("&");
        int parkingLotId;
        int customerID;
        int subscriptionID;

        parkingLotId = Integer.parseInt(args[1]);
        customerID = Integer.parseInt(args[2]);
        subscriptionID = Integer.parseInt(args[3]);
        String queryOnRegular = "SELECT registeredCustomer FROM RegularSubscription s "
                + "WHERE s.id = :subscriptionId "
                + "AND s.registeredCustomer.id = :customerId "
                + "AND s.desegnatedParkingLot.id = :parkingLotId "
                + "AND s.isActive = true";
        String queryOnFull = "SELECT registeredCustomer FROM FullSubscription s "
                + "WHERE s.id = :subscriptionId "
                + "AND s.registeredCustomer.id = :customerId "
                + "AND s.isActive = true";
        HashMap<String, Object> params = new HashMap<>();
        params.put("subscriptionId", subscriptionID);
        params.put("customerId", customerID);
        List<Object> lst = rCustomer.executeQuery(Object.class, queryOnFull, params);
        System.out.println(lst != null);
        if (lst != null && lst.size() > 0) {
            message.setObject(lst.get(0));
        } else {
            params.put("parkingLotId", parkingLotId);
            rCustomer.executeQuery(RegisteredCustomer.class, queryOnRegular, params);
            if (lst != null && lst.size() > 0) {
                message.setObject(lst.get(0));
            } else {
                message.setObject(null);
            }
        }

    }
    private void closeCompliants(Message message, ConnectionToClient client) throws IOException {
        String request = message.getMessage();
        int complaintId;
        int userId;
        double refundAmount;
        String[] args;
        args = request.split("&");

        userId = Integer.parseInt(args[2]);
        System.out.println((args[1]));
        complaintId = Integer.parseInt(args[1]);
        Complaint complaint = complaintHandler.get(complaintId,Complaint.class);
        complaint.setActive(false);
        if(request.contains("With")){
            refundAmount = Double.parseDouble(args[3]);
        }else if(request.contains("Full")){
//                TODO:Need to return the full refund but how to know if we don't have the order that was made?
        }else{
//            todo: complaintHandler.delete(complaintHandler.get(complaintId,Complaint.class),Complaint.class);
        }
        complaintHandler.update(complaint);
    }

    private void showComplaints(Message message, ConnectionToClient client) {
        message.setObject(complaintHandler.getAll(Complaint.class));
    }

    private void cancelOrder(Message message, ConnectionToClient client) {

    }

    private void cancelSubscription(Message message, ConnectionToClient client) {

    }

    private void addSubscription(Message message, ConnectionToClient client) throws IOException {

        if(message.getObject().getClass().getSimpleName().compareTo("FullSubscription") == 0)
            fullSubHandler.save((FullSubscription) message.getObject(), FullSubscription.class);
        else
            regularSubHandler.save((RegularSubscription) message.getObject(), RegularSubscription.class);

    }



    private void showSubscription(Message message, ConnectionToClient client) {
        RegisteredCustomer regCostumer= handleMessegesSession.get(RegisteredCustomer.class,(Integer) client.getInfo("userId"));
        List<Subscription> subscription=regCostumer.getSubscriptions();
        Hibernate.initialize(subscription);
        for (Subscription sub:subscription
             ) {
            Hibernate.initialize(sub.getCarsList());
        }

        message.setObject(subscription);
        handleMessegesSession.flush();

    }

    private void showOrders(Message message, ConnectionToClient client) throws Exception {
        // TODO: get order that has the client id and not all orders
        message.setObject(orderHandler.getAll(Order.class));
    }

    protected void registerUser(Message message,ConnectionToClient client){
        // TODO: 1/11/2023 handle messege from client to get email,password,name,.. all items of a Regular customer
        String[] mess = message.getMessage().split("&");
        String name = mess[4], email = mess[2], password = mess[3], lastName = mess[5], iD = mess[1];
        if (AuthenticationService.checkEmailExistance(email)) {
            // TODO: 1/11/2023 use id as it makes for a better solution , maybe create conditions and send appropriate messages
            message.setMessage("email already exists");
            return;
        }
        //TODO:1/11/23 might need to update the fields later on , for now this is the current format
        RegisteredCustomer customer = new RegisteredCustomer(Integer.parseInt(iD), email, name, lastName, password);
        rCustomer.save(customer, RegisteredCustomer.class);
        customer = rCustomer.getLastAdded(RegisteredCustomer.class);
        clientsCustomersMap.put(customer.getId(), customer);
//        client.setInfo("userId", customer.getId());
//        System.out.println(customer);
        message.setMessage("RegistrationSuccessful");
    }
    protected void Login(Message message,ConnectionToClient client){

        String email,password;
        String[] mess=message.getMessage().split("&");
        email=mess[1];
        password=mess[2];
        System.out.println("get customer");

        int clientType=0;
        clientType=AuthenticationService.checkAuthintecatedEntityType(email,password);
        if(clientType<=0)
            message.setMessage("#authintication failed!");
        else if(clientType<5){
            Employee user=AuthenticationService.getAuthenticatedEntity(email,password);
            System.out.println(user);
            assert user != null;
            if (clientsEmployeeMap.containsKey(user.getId()))
                message.setMessage("#alreadySignedIn");
                // TODO: 1/13/2023 add this option in sign in screen !!
            else {

                clientsEmployeeMap.put(user.getId(), user);
                client.setInfo("userId", user.getId());
                client.setInfo("userType",user.getClass().getName());
                message.setMessage("#authintication successful!");
                message.setObject(user);
            }
        }
        else if(clientType<=5){
            System.out.println("customer aquesition");
            RegisteredCustomer  user=AuthenticationService.getAuthenticatedEntity(email,password);
            System.out.println(user);

            assert user != null;
            if (clientsCustomersMap.containsKey(user.getId()))
                message.setMessage("#alreadySignedIn");
                // TODO: 1/13/2023 add this option in sign in screen !!
            else {
                message.setMessage("#authintication successful!");
                message.setObject(user);
                clientsCustomersMap.put( user.getId(), user);
                client.setInfo("userId",user.getId());
                client.setInfo("userType",user.getClass().getName());

            }
        }

    }
    public void sendToAllClients(Message message) {
        try {
            for (SubscribedClient SubscribedClient : SubscribersList) {
                SubscribedClient.getClient().sendToClient(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void sendParkingLots(Message message, ConnectionToClient client) throws Exception {
        message.setObject(pLot.getAll(ParkingLot.class));

    }
    public void getParkingSpots(Message message, ConnectionToClient client) throws Exception {
        ParkingLotEmployee employee = plEmployee.get((Integer) client.getInfo("userId"), ParkingLotEmployee.class);
        ParkingLot lot = employee.getParkingLot();
        Hibernate.initialize(lot.getSpots());
        message.setObject(lot.getSpots());
    }
    public void setParkingSpots(Message message,ConnectionToClient client){
        ParkingLotEmployee employee = plEmployee.get((Integer) client.getInfo("userId"), ParkingLotEmployee.class);
        ParkingLot lot = employee.getParkingLot();
        ParkingSpot ps = pSpot.get(((ParkingSpot) message.getObject()).getId(), ParkingSpot.class);
        ps.setOccupied(((ParkingSpot) message.getObject()).isOccupied());
        pSpot.update(ps);
        System.out.println(ps);
        Hibernate.initialize(lot.getSpots());
        System.out.println(ps);
        message.setMessage("#GetParkingSpots");
        message.setObject(lot.getSpots());

    }
    public void sendPricesChart(Message message, ConnectionToClient client) throws  Exception {
        message.setObject(pChart.getLastAdded(PricingChart.class));
    }

    public void updatePriceChart(Message message, ConnectionToClient client) {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        int subID = Integer.parseInt(msg.substring(idx + 1));
        PricingChart chartObject = pChart.getLastAdded(PricingChart.class);
        switch (subID) {
            case 1 -> chartObject.setKioskPrice((Double) message.getObject());
            case 2 -> chartObject.setOrderBeforeHandPrice((Double) message.getObject());
        }
        pChart.update(chartObject);
    }

    public void updateSubscriptionAmount(Message message, ConnectionToClient client) throws Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        int subID = Integer.parseInt(msg.substring(idx + 1));
        PricingChart chartObject = pChart.getLastAdded(PricingChart.class);
        switch (subID) {
            case 3 -> chartObject.setOrderBeforeHandPrice((Double) message.getObject());
            case 4 -> chartObject.setMultipleCarRegularSubHours((Double) message.getObject());
            case 5 -> chartObject.setFullSubHours((Double) message.getObject());
        }
        pChart.update(chartObject);
    }

    public void placeOrder(Message message, ConnectionToClient client) throws Exception {
        Order newOrder = (Order)message.getObject();
        //TODO: change to customerID from client saved info
        RegisteredCustomer rg = rCustomer.get((Integer) client.getInfo("userId"), RegisteredCustomer.class);
        orderHandler.save(newOrder, Order.class);
        message.setObject(newOrder.getId());
        System.out.println(message.getObject());
        rg.addOrder(newOrder);
        rCustomer.update(rg);
        message.setObject(newOrder.getId());
        client.sendToClient(message);
    }

    public void getCustomersOrders(Message message,ConnectionToClient client) throws Exception{

        RegisteredCustomer regCostumer= handleMessegesSession.get(RegisteredCustomer.class,(Integer) client.getInfo("userId"));
        Object orders =regCostumer.getOrders();
        Hibernate.initialize(orders);
        handleMessegesSession.flush();
        message.setObject(orders);

    }
    public void getUser(Message message,ConnectionToClient client)throws Exception{
        int id=(Integer) client.getInfo("userId");
        String type=(String) client.getInfo("userType");
        if (type.startsWith(RegisteredCustomer.class.getName())||type.startsWith(OneTimeCustomer.class.getName()))
            message.setObject(rCustomer.get(id,RegisteredCustomer.class));
        else {
             message.setObject(getEmployee(id));
        }

    }
    public void getCustomerCars(Message message, ConnectionToClient client) throws IOException {
        RegisteredCustomer regCostumer= handleMessegesSession.get(RegisteredCustomer.class,(Integer) client.getInfo("userId"));
        List<Car> cars=regCostumer.getCars();
        Hibernate.initialize(cars);
        message.setObject(cars);

    }
    private Employee getEmployee(int id) {
        Employee user=clientsEmployeeMap.get(id);
        if (user.getClass()==ParkingLotEmployee.class){
            user=plEmployee.get(id,ParkingLotEmployee.class);
        }
        else if (user.getClass()==ParkingLotManager.class){
            user=plManager.get(id,ParkingLotManager.class);
        }else if (user.getClass()==CustomerServiceEmployee.class){
            user=csEmployee.get(id,CustomerServiceEmployee.class);
        }else if (user.getClass()==GlobalManager.class){
            user=globalManager.get(id,GlobalManager.class);
        }
        return user;
    }

    private void initializeParkingLot(Message message, ConnectionToClient client) {
        ParkingLotEmployee E = plEmployee.get((Integer) client.getInfo("userId"), ParkingLotEmployee.class);
        ParkingLot PL = E.getParkingLot();
        Hibernate.initialize(PL.getSpots());
        PL.reInitiateParkingSpots();
        handleMessegesSession.update(PL);
        handleMessegesSession.flush();
        message.setObject(PL.getSpots());
        message.setMessage("#GetParkingSpots");
    }


    // todo: need to fix it!!!!!!!
    public void diretToParkingLots(Message message, ConnectionToClient client) throws IOException, Exception {
        // TODO: get all parkinglots find nearenest that has space
        message.setMessage("#GO TO :" + "TO BE CONTINUED");
        client.sendToClient(message);
    }


}
