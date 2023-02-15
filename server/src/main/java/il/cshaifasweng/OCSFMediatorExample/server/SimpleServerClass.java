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
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import il.cshaifasweng.ParkingLotEntities.*;
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

import static il.cshaifasweng.OCSFMediatorExample.server.ServerMessegesEnum.*;
import static il.cshaifasweng.ParkingLotEntities.ConstantMessegesForClient.*;

@Getter
@Setter
public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static  final DataBaseManipulation<PricingChart> pChart = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<ParkingLot> pLot = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<Order> orderHandler = new DataBaseManipulation<>();
    private static  final DataBaseManipulation<RegisteredCustomer> rCustomer = new DataBaseManipulation<>();
    private static Map<Integer, Customer> clientsCustomersMap = new HashMap<>();
    private static Map<Integer, Employee> clientsEmployeeMap = new HashMap<>();
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
//                HandleOnTimeOrderDelays = executorService.scheduleAtFixedRate(new handleOrderesAndPenalties(this), 0, 1, TimeUnit.MINUTES);
//                HandleSubsReminders = executorService.scheduleAtFixedRate(new HandleSubscriptionReminders(this), HandleSubscriptionReminders.getDelay(), TimeUnit.HOURS.toSeconds(24), TimeUnit.SECONDS);

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

        try {
            if (!handleMessegesSession.isOpen()){
                handleMessegesSession = DAO.factory.openSession();
                DataBaseManipulation.intiate(handleMessegesSession);
                AuthenticationService.intiate(handleMessegesSession);
            }
            handleMessegesSession.beginTransaction();

            int type=messageType((Message) msg);
            //types of messeges are in ServerMessegesEnum class !pinpoint the number and check the enum value to understand the code !
            switch (type) {
                case 0 -> message.setMessage("Empty message");
                case 1 -> Login(message, client);
                case 2 -> registerUser(message, client);
                case 3 -> initializeParkingLot(message, client);
                case 4 -> sendParkingLots(message, client);
                case 5 -> placeOrder(message, client);
                case 6 -> getUser(message, client);
                case 7 -> sendPricesChart(message, client);
                case 8 -> updatePriceChart(message, client);
                case 9 -> updateSubscriptionAmount(message, client);
                case 10 -> diretToParkingLots(message, client);
                case 11 -> getParkingSpots(message, client);
                case 12 -> setParkingSpots(message, client);
                case 13 -> showOrders(message, client);
                case 14 -> showSubscription(message, client);
                case 15 -> addSubscription(message, client);
                case 16 -> cancelOrder(message, client);
                case 17 -> cancelSubscription(message, client);
                case 18 -> getRefundChart(message, client);
                case 19 -> System.out.println("connection alive");
                case 20 -> getCustomersOrders(message, client);
                case 21 -> applyCompaint(message, client);
                case 22 -> showComplaints(message, client);
                case 23 -> closeCompliants(message, client);
                case 24 -> verifySubscription(message, client);
                case 25 -> verifyOrder(message, client);
                case 26 -> getCustomerCars(message, client);
                case 27 -> cancelOrderAndGetRefund(message, client);
                case 28 -> LogOut(message, client);
                case 29-> enterParkingLot(message, client);
                case 30-> exitParkingLot(message, client);
                default -> System.out.println("message content doesn't match any request");
            }
//            handleMessegesSession.getTransaction().commit();
//            handleMessegesSession.close();
//
            client.sendToClient(message);
            handleMessegesSession.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            handleMessegesSession.close();
        }

    }

    private void LogOut(Message message, ConnectionToClient client) {
        // TODO: 15/02/2023 implement log out
    }

    private void exitParkingLot(Message message, ConnectionToClient client) throws IOException {
        EntryExitParkingLot(message,false);
    }

    private void enterParkingLot(Message message, ConnectionToClient client) throws IOException {
        EntryExitParkingLot(message,true);
    }

    private static void EntryExitParkingLot(Message message,boolean isEntry) throws IOException {
        String[] instructions= message.getMessage().split("&");
        ParkingLot plot=pLot.get(Integer.parseInt(instructions[1]),ParkingLot.class);
        Transactions transaction=new Transactions();
        String licensePlate=instructions[4];
        switch (instructions[5]) {
            case "Subscription"->transaction = handleMessegesSession.get(Subscription.class, Integer.parseInt(instructions[3]));
            case "Order"->transaction = handleMessegesSession.get(Order.class, Integer.parseInt(instructions[3]));
            case "BasicOrder"-> System.out.println("basicOrder");// TODO: 15/02/2023 implement basic order
            default->throw new IOException("transaction type is not valid");
        }
        //todo: check if the parking lot is full
        if (isEntry&&plot.isFull()) {
            message.setMessage(FULL_PARKING_LOT.type);
        }
        else{
            try {
                EntryAndExitLog log = isEntry?plot.entryToPLot(transaction, licensePlate):plot.exitParkingLot(transaction,licensePlate);
                handleMessegesSession.saveOrUpdate(log);
            } catch (Exception e) {
                message.setMessage(e.getMessage());
            }
        }
    }

    private static int messageType(Message msg) {
        int messageType = -1;
        for (ServerMessegesEnum messageTypeEnum : ServerMessegesEnum.values()) {
            messageType = messageTypeEnum.startswith(msg.getMessage());
            if (messageType != -1) {
                return messageType;
            }
        }
        return messageType;
    }

    private void cancelOrderAndGetRefund(Message message, ConnectionToClient client) {
        String[] instructions=message.getMessage().split("&");
        Order order=orderHandler.get(Integer.parseInt(instructions[2]), Order.class) ;
        order.setActive(false);
        orderHandler.update(order);
        int customer= (Integer) client.getInfo("userId");
        Refund refund=new Refund("Calcelation",Double.parseDouble(instructions[1]),rCustomer.get( customer , RegisteredCustomer.class));
        refund.setTransaction_method(order.getTransaction_method());
        refund.setTransactionStatus(true);
        handleMessegesSession.save(refund);
        message.setMessage(CANCEL_ORDER_AND_GET_REFUND.type);

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
        int  orderID;

        parkingLotId = Integer.parseInt(args[1]);
        customerID = Integer.parseInt(args[2]);
        orderID = Integer.parseInt(args[3]);
        String queryOnOrder = "SELECT registeredCustomer FROM Order o "
                + "WHERE o.id = :orderID "
                + "AND o.registeredCustomer.id = :customerId "
                + "AND o.parkingLotID.id = :parkingLotId "
                + "AND o.dateOfOrder  >=CURDATE() AND o.active = true AND TIMESTAMPDIFF(MINUTE, o.dateOfOrder, CURRENT_TIMESTAMP) < 30";
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderID", orderID);
        params.put("customerId", customerID);
        params.put("parkingLotId", parkingLotId);
        System.out.println("queryOnOrder: " + queryOnOrder);
        RegisteredCustomer lst = (RegisteredCustomer) rCustomer.queiryData(Object.class, queryOnOrder, params);
        if (lst == null) {
            queryOnOrder = "SELECT registeredCustomer FROM Order o "
                    + "WHERE o.id = :orderID "
                    + "AND o.registeredCustomer.id = :customerId "
                    + "AND o.parkingLotID.id = :parkingLotId "
                    + "AND o.dateOfOrder  >=CURDATE() AND o.active = true AND TIMESTAMPDIFF(MINUTE, o.dateOfOrder, CURRENT_TIMESTAMP) < 30";

        } else {
            message.setMessage(VERIFY_ORDER.type);
            message.setObject(lst);
        }
        message.setObject(lst);
    }

    private void verifySubscription(Message message, ConnectionToClient client) throws IOException {
        String request = message.getMessage();
        String[] args;
        args = request.split("&");
        int parkingLotId;
        int customerID;
        int  subscriptionID;

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
        Customer lst = (RegisteredCustomer) rCustomer.queiryData(Object.class, queryOnFull, params);
        if (lst != null ) {
            message.setObject(lst);
        } else {
            params.put("parkingLotId", parkingLotId);
            lst= rCustomer.queiryData(RegisteredCustomer.class, queryOnRegular, params);
            if (lst != null ) {
                message.setObject(lst);
            } else {
                message.setObject(null);
            }
        }

    }
    private void closeCompliants(Message message, ConnectionToClient client) throws IOException {
        System.out.println(message.getMessage());
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
        List<Order> orders = orderHandler.getAll(Order.class);
        orders.forEach(order -> {
            Hibernate.initialize(order.getRegisteredCustomer());
            Hibernate.initialize(order.getParkingLotID());
            Hibernate.initialize(order.getCar());
        });
        message.setObject(orders);
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

        message.setMessage("RegistrationSuccessful");
    }
    protected void Login(Message message,ConnectionToClient client){

        String email,password;
        String[] mess=message.getMessage().split("&");
        email=mess[1];
        password=mess[2];

        int clientType=0;
        clientType=AuthenticationService.checkAuthintecatedEntityType(email,password);
        if(clientType<=0)
            message.setMessage("#authintication failed!");
        else if(clientType<5){
            Employee user=AuthenticationService.getAuthenticatedEntity(email,password);
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
            RegisteredCustomer  user=AuthenticationService.getAuthenticatedEntity(email,password);

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
        ps.setSaved(((ParkingSpot) message.getObject()).isSaved());
        ps.setFaulty(((ParkingSpot) message.getObject()).isFaulty());
        pSpot.update(ps);
        System.out.println(ps);
        Hibernate.initialize(lot.getSpots());
        System.out.println(ps);
        message.setMessage(GET_PARKING_SPOTS.type);
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
//        Hibernate.initialize(orders);
        for (Order order : regCostumer.getOrders()) {
            Hibernate.initialize(order.getCar());
        }

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
        } else if (user.getClass()==ParkingLotManager.class){
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
        PL.ExitAllCarsAndInitiateAllSpots();
        handleMessegesSession.update(PL);
        handleMessegesSession.flush();
        message.setObject(PL.getSpots());
        message.setMessage(GET_PARKING_SPOTS.type);
    }
    // todo: need to fix it!!!!!!!
    public void diretToParkingLots(Message message, ConnectionToClient client) throws IOException, Exception {
        // TODO: get all parkinglots find nearenest that has space
        message.setMessage("#GO TO :" + "TO BE CONTINUED");
        client.sendToClient(message);
    }


}
