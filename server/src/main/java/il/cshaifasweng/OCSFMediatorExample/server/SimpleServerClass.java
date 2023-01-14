package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.LogInEntities.AuthenticationService;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.Employee;
import il.cshaifasweng.MoneyRelatedServices.Penalty;
import il.cshaifasweng.MySQL;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.Subscription;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static DataBaseManipulation<PricingChart> pChart=new DataBaseManipulation<PricingChart>();
    private static DataBaseManipulation<ParkingLot> pLot= new DataBaseManipulation<ParkingLot>();
    private static DataBaseManipulation<Order> orderHandler= new DataBaseManipulation<Order>();
    private static DataBaseManipulation<RegisteredCustomer> rCustomer = new DataBaseManipulation<>();
    private static Map<Integer, Customer> clientsCustomersMap = new HashMap<>();
    private static Map<Integer, Employee> clientsEmployeeMap = new HashMap<>();
    private static DataBaseManipulation<Subscription> subscriptionHandler= new DataBaseManipulation<>();
    private static DataBaseManipulation<Complaint> complaintHandler = new DataBaseManipulation<>();

    public SimpleServerClass(int port) {
        super(port);
        DataBaseManipulation.intiate();
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        try {
            if (request.isBlank()) {
                message.setMessage("Error! we got an empty message");
                client.sendToClient(message);
            } else if (request.startsWith("#LogIn")) {
                Login(message,client);
                client.sendToClient(message);

            }else if (request.startsWith("#Register")) {
                registerUser(message,client);
                client.sendToClient(message);

            } else if (request.startsWith("#getAllParkingLots")) {
                sendParkingLots(message, client);

            }else if (request.startsWith("#placeOrder")) {
                placeOrder(message, client);
            }else if (request.startsWith("#getRegisteredCustomer")) {
                getRegisteredCustomer(message, client);
            }
            else if (request.startsWith("#getPricingChart")) {
                sendPricesChart(message, client);

            } else if (request.startsWith("#updatePrice")) {
                System.out.println("Update");
                updatePriceChart(message, client);
            } else if (request.startsWith("#updateAmount")) {
                updateSubscriptionAmount(message, client);

            }else if (request.startsWith("#showOrders")){
                showOrders(message , client);
            }
            else if (request.startsWith("#showSubscription")) {
                showSubscription(message, client);

            }else if (request.startsWith("#ConnectionAlive")) {
                System.out.println("Alive!");
            }else if(request.startsWith("#GetAllCompliants")){
                System.out.println("Got the message");
                showComplaints(message, client);
            }else if(request.startsWith("#CloseComplaint")) {
                System.out.println("ClosingCompliant");
                    closeCompliants(message, client);
            }else {
                System.out.println("no selection was done!!!");
            }



        } catch (Exception ex) {
            ex.printStackTrace();
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
//                Need to return the full refund but how to know if we don't have the order that was made?
        }else{
//            complaintHandler.delete(complaintHandler.get(complaintId,Complaint.class),Complaint.class);
        }
        complaintHandler.update(complaint);
        client.sendToClient(message);
    }

    private void showComplaints(Message message, ConnectionToClient client) {
        message.setObject(complaintHandler.getAll(Complaint.class));
        try {
            client.sendToClient(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSubscription(Message message, ConnectionToClient client) {
        // TODO: get subscription that has the client id and not all susbcriptions
        message.setObject(subscriptionHandler.getAll(Subscription.class));
        try {
            client.sendToClient(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOrders(Message message, ConnectionToClient client) throws Exception {
        // TODO: get order that has the client id and not all orders
        message.setObject(orderHandler.getAll(Order.class));
        client.sendToClient(message);
    }

    protected void registerUser(Message message,ConnectionToClient client){
        // TODO: 1/11/2023 handle messege from client to get email,password,name,.. all items of a Regular customer
        String[] mess=message.getMessage().split("&");
        String name=mess[3],email=mess[1],password=mess[2],lastName=mess[4];
        if (AuthenticationService.checkEmailExistance(email)){
            // TODO: 1/11/2023 use a username instead more secure
            message.setMessage("email already exists");
            return;
        }
        //TODO:1/11/23 might need to update the fields later on , for now this is the current format
        RegisteredCustomer customer=new RegisteredCustomer(2003,email,name,password);
        rCustomer.save(customer,RegisteredCustomer.class);
        customer=rCustomer.getLastAdded(RegisteredCustomer.class);
        clientsCustomersMap.put( customer.getId(), customer);
        client.setInfo("userId",customer.getId());
        System.out.println(customer);
        message.setMessage("RegistrationSuccessful");
    }
    protected void Login(Message message,ConnectionToClient client){

        String email,password,userName="";
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
            if (clientsEmployeeMap.containsKey(user.getId()))
                message.setMessage("#alreadySignedIn");
                // TODO: 1/13/2023 add this option in sign in screen !!
            else {

                clientsEmployeeMap.put(user.getId(), user);
                client.setInfo("userId", user.getId());
                message.setMessage("#authintication successful!");
                message.setObject(user);
            }
        }
        else if(clientType<=5){
            System.out.println("customer aquesition");
            RegisteredCustomer  user=AuthenticationService.getAuthenticatedEntity(email,password);
            System.out.println(user);

            if (clientsCustomersMap.containsKey(user.getId()))
                message.setMessage("#alreadySignedIn");
                // TODO: 1/13/2023 add this option in sign in screen !!
            else {
                message.setMessage("#authintication successful!");
                message.setObject(user);
                clientsCustomersMap.put( user.getId(), user);
                client.setInfo("userId",user.getId());

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

    public void sendParkingLots(Message message, ConnectionToClient client) throws IOException, Exception {
        message.setObject(pLot.getAll(ParkingLot.class));

        client.sendToClient(message);
    }

    public void sendPricesChart(Message message, ConnectionToClient client) throws IOException, Exception {
        message.setObject(pChart.getAll(PricingChart.class));
        client.sendToClient(message);
    }

    public void updatePriceChart(Message message, ConnectionToClient client) throws IOException, Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);

        PricingChart chartObject = pChart.get(Integer.parseInt(subID), PricingChart.class);
        chartObject.setRate((Double) message.getObject());
        pChart.update(chartObject);
    }

    public void updateSubscriptionAmount(Message message, ConnectionToClient client) throws IOException, Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);
        PricingChart chartObject = pChart.get(Integer.parseInt(subID), PricingChart.class);
        chartObject.setRate((Integer) message.getObject());
        pChart.update(chartObject);
    }

    public void placeOrder(Message message, ConnectionToClient client) throws IOException,Exception {
        Order newOrder = (Order)message.getObject();
        //TODO: change to customerID from client saved info
        RegisteredCustomer rg = rCustomer.get(1, RegisteredCustomer.class);
        System.out.println(rg.getId());
        orderHandler.save(newOrder, Order.class);
        rg.addOrder(newOrder);
        rCustomer.update(rg);
        rg=rCustomer.get(1,RegisteredCustomer.class);
        rg.getOrders().get(0).setRegisteredCustomer(rg);
        client.sendToClient(message);
    }

    public void getRegisteredCustomer(Message message, ConnectionToClient client) throws IOException,Exception {
        //TODO: change to customerID from client saved info
        message.setObject(rCustomer.get(1, RegisteredCustomer.class));
        client.sendToClient(message);
    }


}
