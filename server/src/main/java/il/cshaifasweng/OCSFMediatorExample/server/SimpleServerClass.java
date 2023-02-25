package il.cshaifasweng.OCSFMediatorExample.server;

import EmailSMPTServices.SendEmail;
import il.cshaifasweng.DataManipulationThroughDB.DAO;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.LogInEntities.AuthenticationService;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.*;
import il.cshaifasweng.LogInEntities.User;
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
    private static  final DataBaseManipulation<OnlineOrder> orderHandler = new DataBaseManipulation<>();
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
    private static  final DataBaseManipulation<Subscription> SubscriptionHandler = new DataBaseManipulation<>();
    private  static final DataBaseManipulation<RefundChart> refundChartHandler=new DataBaseManipulation<>();
    private  static final DataBaseManipulation<ParkingSpot> pSpot=new DataBaseManipulation<>();
    private static Session handleMessegesSession;
    static Session handleDelaysAndPenaltiesSession;
    public ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    public ScheduledFuture<?> HandleOnTimeOrderDelays, HandleSubsReminders;


    public SimpleServerClass(int port) {
        super(port);
        handleMessegesSession = DAO.factory.openSession();
        DataBaseManipulation.intiate(handleMessegesSession);
        AuthenticationService.intiate(handleMessegesSession);
        System.out.println("messegesSession is open");

//         TODO: 06/02/2023  should be working correctly use these lines in final project !
//                HandleOnTimeOrderDelays = executorService.scheduleAtFixedRate(new handleOrderesAndPenalties(this), 0, 1, TimeUnit.MINUTES);
//                HandleSubsReminders = executorService.scheduleAtFixedRate(new HandleSubscriptionReminders(this), HandleSubscriptionReminders.getDelay(), TimeUnit.HOURS.toSeconds(24), TimeUnit.SECONDS);
//                HandleSubsReminders = executorService.scheduleAtFixedRate(new HandleOfflineOrdersTimeLimit(this), 0, 1, TimeUnit.MINUTES);

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
                case 31-> getCustomerRefunds(message, client);
                case 32-> getCustomerTransactions(message, client);
                case 33->getCustomerLogs(message, client);
                case 34-> getCustomerOfflineOrders(message, client);
                case 35-> getOrdersToBeConfirmed(message, client);
                case 36-> confirmArrival(message, client);
                case 37 -> getActiveOrders(message, client);
                case 38 -> getAllOrdersForManager(message, client);
                case 39 -> RejectAllPriceRequests(message,client);
                case 40 -> getOnlineOrderForVerificationOfAttendance(message,client);
                case 41 -> setNewKioskOrder(message,client);
                case 42 -> checkKioskEmployeeCreditentials(message,client);
                case 43 -> getSubscriptionOrder(message,client);
                default -> System.out.println("message content doesn't match any request");
                // TODO: 25/02/2023 add case for updating subscription end date
                // TODO: 25/02/2023 add case for giving one time pass

            }

            client.sendToClient(message);
            handleMessegesSession.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            handleMessegesSession.close();
        }

    }

    private void getSubscriptionOrder(Message message, ConnectionToClient client) {
        String[] orderDetails=((String) message.getObject()).split("&");
        int subscriotionId=Integer.parseInt(orderDetails[0]);
        String carId=orderDetails[1];
        message.setObject(null);
        RegularSubscription actualSubcription= handleMessegesSession.get(RegularSubscription.class, subscriotionId);
        System.out.println(actualSubcription );
        if(actualSubcription==null)
            message.setMessage("#SubcriptionNotFound");
        else if(actualSubcription.getCar(carId) != null)
            message.setObject(actualSubcription);
        else message.setMessage("#SubcriptionNotFound");
    }

    private void checkKioskEmployeeCreditentials(Message message, ConnectionToClient client) {
        String[] creditentials=((String) message.getMessage()).split("&");
        int username=Integer.parseInt(creditentials[1]);
        String password=creditentials[2];
        ParkingLotEmployee employee=handleMessegesSession.get(ParkingLotEmployee.class,username);
        if (employee==null){
            message.setObject("Wrong Username");
        }
        else if (!employee.getPassword().equals(password)){
            message.setObject("Wrong Password");
        }
        else if (!(employee.getParkingLot().getId()==Integer.parseInt(creditentials[3]))){
            message.setObject("Missmach between employee and parking lot");
        }
        else {
            message.setObject("true");
        }
    }

    private void setNewKioskOrder(Message message, ConnectionToClient client) {
        OfflineOrder order=(OfflineOrder) message.getObject();
        Customer customer =order.getCustomer();
        ParkingLot pl=order.getParkingLotID();
        pl=handleMessegesSession.get(ParkingLot.class,pl.getId());
        if (pl.isFull()){
            message.setObject("Parking Lot Is Full ,please refer to the Kiosk worker for more information");
        }
        else if(pl.inParkingLot(order.getCar().getCarNum())) {
            message.setObject("Car is already in the parking lot");
        } else {
            RegisteredCustomer registeredCustomer = handleMessegesSession.get(RegisteredCustomer.class, customer.getId());
            if (registeredCustomer == null) {
               OneTimeCustomer oneTimeCustomer=handleMessegesSession.get(OneTimeCustomer.class,customer.getId());
                if (oneTimeCustomer==null){
                     handleMessegesSession.save(customer);
                     order.setCustomer(customer);
                }
                else {
                     order.setCustomer(oneTimeCustomer);
                }

            } else {
                order.setCustomer(registeredCustomer);
            }
            order.setParkingLotID(pl);
            handleMessegesSession.save(order);
            message.setObject(order.getId());

        }
    }

    private void getOnlineOrderForVerificationOfAttendance(Message message, ConnectionToClient client) {
        String[] orderDetails=((String) message.getObject()).split("&");
        int orderId=Integer.parseInt(orderDetails[0]);
        String carId=orderDetails[1];
        message.setObject(null);
        OnlineOrder actualOrder=orderHandler.get(orderId,OnlineOrder.class);
        System.out.println(actualOrder );
        if(actualOrder==null)
            message.setMessage("#OrderNotFound");
        else if(actualOrder.getCar().getCarNum().startsWith(carId))
            message.setObject(actualOrder);
        else message.setMessage("#OrderNotFound");
    }

    private void RejectAllPriceRequests(Message message, ConnectionToClient client) {
    }

    private void confirmArrival(Message message, ConnectionToClient client) {
        OnlineOrder order=(OnlineOrder) message.getObject(),actualOrder;
        actualOrder=orderHandler.get(order.getId(),OnlineOrder.class);
        actualOrder.setAgreedToPayPenalty(order.isAgreedToPayPenalty());
        orderHandler.update(actualOrder);
        message.setObject(actualOrder);

    }

    private void getOrdersToBeConfirmed(Message message, ConnectionToClient client) {
        // TODO: 21/02/2023 queiry !
        RegisteredCustomer customer =getCustomer(client);
        String hql= "FROM OnlineOrder o "
                + "WHERE o.registeredCustomer.id = :customerId "
                + "AND o.dateOfOrder  >=CURDATE() AND o.active = true" +
                " AND TIMESTAMPDIFF(MINUTE, o.dateOfOrder, CURRENT_TIMESTAMP) < 30" +
                " AND o.reminderSent = 3 AND agreedToPayPenalty=false";
        HashMap<String, Object> params = new HashMap<>();
        params.put("customerId", customer.getId());
        List<OnlineOrder> orders=orderHandler.executeListQuery(OnlineOrder.class,hql,params,handleMessegesSession);
        Hibernate.initialize(orders);
        message.setObject(orders);

    }

    private void getCustomerOfflineOrders(Message message, ConnectionToClient client) throws IOException {
        RegisteredCustomer customer =getCustomer(client);
        List<OfflineOrder> offlineOrders=customer.getOfflineOrders();
        Hibernate.initialize(offlineOrders);
        message.setObject(customer.getOfflineOrders());
    }

    private void getCustomerLogs(Message message, ConnectionToClient client) throws IOException {
        // TODO: 21/02/2023 data base queiry
        RegisteredCustomer customer =getCustomer(client);
        List<EntryAndExitLog> logs=customer.getEntryAndExitLog();
        Hibernate.initialize(logs);
        message.setObject(logs);
        System.out.println("...LOGS...");
        System.out.println(customer.getEntryAndExitLog());
    }

    private void getCustomerTransactions(Message message, ConnectionToClient client) throws IOException {
        // TODO: 21/02/2023 might not work , transactions itself doesn't have a one to many relationship with the customer
        RegisteredCustomer customer =getCustomer(client);
        List<Transactions> transaction=new ArrayList<>(customer.getOnlineOrders());
        List<Transactions> tr2=new ArrayList<>(customer.getOfflineOrders());
        List<Transactions> tr3=new ArrayList<>(customer.getSubscriptions());
        List<Transactions> tr4=new ArrayList<>(customer.getRefunds());
        transaction.addAll(tr2);
        transaction.addAll(tr3);
        transaction.addAll(tr4);
        Hibernate.initialize(transaction);
        message.setObject(transaction);
    }

    private void getCustomerRefunds(Message message, ConnectionToClient client) throws IOException {
        RegisteredCustomer customer =getCustomer(client);
        List<Refund> refunds=customer.getRefunds();
        Hibernate.initialize(refunds);
        message.setObject(refunds);
        // TODO: 21/02/2023
    }

    private void LogOut(Message message, ConnectionToClient client) {
        int id = (Integer) client.getInfo("userId");
        String type = (String) client.getInfo("userType");
        try{
                if (type.startsWith(RegisteredCustomer.class.getName()) || type.startsWith(OneTimeCustomer.class.getName())) clientsCustomersMap.remove(id);
                else clientsEmployeeMap.remove(id);
                client.setInfo("userId", null);
                client.setInfo("userType", null);
                message.setObject("Success");

        }
        catch (Exception e){
            message.setObject("Failed");
        }
        System.out.println(message.getObject());

    }

    private void exitParkingLot(Message message, ConnectionToClient client) throws IOException {
        EntryExitParkingLot(message,false);
    }

    private void enterParkingLot(Message message, ConnectionToClient client) throws IOException {

        EntryExitParkingLot(message,true);
    }
    private RegisteredCustomer getCustomer(ConnectionToClient client){
        return rCustomer.get((Integer) client.getInfo("userId"), RegisteredCustomer.class);
    }
    private static void EntryExitParkingLot(Message message,boolean isEntry) throws IOException {
        // TODO: 24/02/2023 check if car is already in the parking lot
        // TODO: 24/02/2023 check if the entrance and exit work on offline orders
        // TODO: 24/02/2023 make sure that each one that exits the parking lot holding an order is charged
        // TODO: 24/02/2023 make sure to update the Subscriptions days left +hours left , when they exit the parking lot
        // TODO: 24/02/2023 figure out the way to charge the customer , wheither to update his current balance or to create a new transaction that is included in the entry And exitlog
        // TODO: 25/02/2023 Make sure to check one time Entry and exit , might be done in verify also !
        String[] instructions=message.getMessage().split("&");
        ParkingLot plot=pLot.get(Integer.parseInt(instructions[1]),ParkingLot.class);
        Transactions transaction=new Transactions();
        String licensePlate=instructions[4];
        switch (instructions[5]) {
            case "Subscription"->transaction = handleMessegesSession.get(Subscription.class, Integer.parseInt(instructions[3]));
            case "Order"->transaction = handleMessegesSession.get(OnlineOrder.class, Integer.parseInt(instructions[3]));
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
        OnlineOrder onlineOrder =orderHandler.get(Integer.parseInt(instructions[2]), OnlineOrder.class) ;
        onlineOrder.setActive(false);
        orderHandler.update(onlineOrder);
        int customer=onlineOrder.getRegisteredCustomer().getId();
        Refund refund=new Refund("Calcelation",Double.parseDouble(instructions[1]),rCustomer.get( customer , RegisteredCustomer.class));
        refund.setTransaction_method(onlineOrder.getTransaction_method());
        refund.setTransactionStatus(true);
        handleMessegesSession.save(refund);
        SendEmail.sendEmail(onlineOrder.getEmail(),"Order Cancellation","Your refund ID is "+refund.getId()+"\n"+"Your refund amount is "+refund.getValue());
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
        String queryOnOrder = "SELECT registeredCustomer FROM OnlineOrder o "
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
            queryOnOrder = "SELECT registeredCustomer FROM OnlineOrder o "
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
        // TODO: 25/02/2023 make an option for updating the expiration date of the subscription
        String[] instructions=message.getMessage().split("&");

        RegisteredCustomer rg = rCustomer.get(Integer.parseInt(instructions[1]), RegisteredCustomer.class);
        if (rg== null) {
            rg = new RegisteredCustomer(Integer.parseInt(instructions[1]) ,instructions[2],instructions[3],instructions[4]);
            rCustomer.save(rg, RegisteredCustomer.class);
        }
        Subscription sub=(Subscription) message.getObject();
        sub.setRegisteredCustomer(rg);
        if(sub.getClass().getSimpleName().compareTo("FullSubscription") == 0){
            fullSubHandler.save((FullSubscription) sub, FullSubscription.class);
            SendEmail.sendEmail(sub.getEmail(),
                    "Your Full Subscription has been approved",
                    "Your Full Subscription's id is : " + sub.getId() +
                            "\nsubscription start date: " + sub.getStartDate() +"\nsubscription end date: " + sub.getExpirationDate() +
                            "\nThank you for choosing our service!");
            message.setObject(sub.getId());
        }
        else{
            regularSubHandler.save((RegularSubscription) sub, RegularSubscription.class);
            SendEmail.sendEmail(sub.getEmail(),
                    "Your Regular Subscription has been approved",
                    "Your Regular Subscription's id is : " + sub.getId() +
                            "\nsubscription start date: " + sub.getStartDate() +"\nsubscription end date: " + sub.getExpirationDate() +
                            "\nThank you for choosing our service!");
            message.setObject(sub.getId());
        }


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
        List<OnlineOrder> OnlineOrders = orderHandler.getAll(OnlineOrder.class);
        OnlineOrders.forEach(order -> {
            Hibernate.initialize(order.getRegisteredCustomer());
            Hibernate.initialize(order.getParkingLotID());
            Hibernate.initialize(order.getCar());
        });
        message.setObject(OnlineOrders);
    }

    protected void registerUser(Message message,ConnectionToClient client){
        // TODO: 24/02/2023 check for existing custoemr profile when Registering , if exists then update the profile with the new info , +new password , should'nt be hard
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
        OnlineOrder newOnlineOrder = (OnlineOrder)message.getObject();
        String[] instructions=message.getMessage().split("&");

        RegisteredCustomer rg = rCustomer.get(Integer.parseInt(instructions[1]), RegisteredCustomer.class);
        if (rg== null) {
            rg = new RegisteredCustomer(Integer.parseInt(instructions[1]) ,instructions[2],instructions[3],instructions[4]);
            rCustomer.save(rg, RegisteredCustomer.class);
        }
        //TODO: change to customerID from client saved info
        orderHandler.save(newOnlineOrder, OnlineOrder.class);
        message.setObject(newOnlineOrder.getId());
        System.out.println(message.getObject());
        rg.addOrder(newOnlineOrder);
        rCustomer.update(rg);
        SendEmail.sendEmail(newOnlineOrder.getEmail(),
                "Order Confirmation",
                "Your order has been placed successfully\nYour orderId is :"+newOnlineOrder.getId()+"\nIn Parking Lot number :"
        +newOnlineOrder.getParkingLotID().getId()+"\nThank you for choosing our service");
        message.setObject(newOnlineOrder.getId());
    }

    public void getCustomersOrders(Message message,ConnectionToClient client) throws Exception{

        RegisteredCustomer regCostumer= handleMessegesSession.get(RegisteredCustomer.class,(Integer) client.getInfo("userId"));
        Object orders =regCostumer.getOnlineOrders();
//        Hibernate.initialize(orders);
        for (OnlineOrder onlineOrder : regCostumer.getOnlineOrders()) {
            Hibernate.initialize(onlineOrder.getCar());
        }

        handleMessegesSession.flush();
        message.setObject(orders);

    }
    public void getUser(Message message,ConnectionToClient client)throws Exception{
        int id;
        try{
            id=(Integer) client.getInfo("userId");
            String type=(String) client.getInfo("userType");
            if (type.startsWith(RegisteredCustomer.class.getName())||type.startsWith(OneTimeCustomer.class.getName()))
                message.setObject(rCustomer.get(id,RegisteredCustomer.class));
            else {
                 message.setObject(getEmployee(id));
            }
        }
        catch (Exception e){
           message.setObject(null);
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
        if(message.getObject() instanceof OnlineOrder){
            OnlineOrder order = (OnlineOrder) message.getObject();
            //OneTimePass newPass = order.getOneTimePass();
//            handleMessegesSession.save(newPass);
            handleMessegesSession.saveOrUpdate(order);
        }else {
            Subscription sub = (Subscription) message.getObject();
            handleMessegesSession.saveOrUpdate(sub);
        }
    }

    public void getActiveOrders(Message message, ConnectionToClient client) throws Exception {
        List<OnlineOrder> orders = (List<OnlineOrder>) orderHandler.getAll(OnlineOrder.class);
        List<OnlineOrder> activeOrders = new ArrayList<>();
        orders.forEach(order -> {
            if (order.isActive()) activeOrders.add(order);
        });
        message.setObject(activeOrders);
    }

    public void getAllOrdersForManager(Message message, ConnectionToClient client) throws Exception {
        message.setObject(orderHandler.getAll(OnlineOrder.class));
    }

    public void rejectOnePriceRequest(Message message, ConnectionToClient client) throws Exception {
        PricingChart pc = pChart.get((int) message.getObject(), PricingChart.class);
        pc.setWaitForPermission(false);
        pChart.update(pc);
    }

    public void rejectAllPriceRequests(Message message, ConnectionToClient client) throws Exception {
        List<PricingChart> allPc = pChart.getAll(PricingChart.class);
        allPc.forEach(pc -> {
            pc.setWaitForPermission(false);
            pChart.update(pc);
        });
    }
}
