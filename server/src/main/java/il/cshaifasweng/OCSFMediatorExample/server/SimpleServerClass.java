package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.LogInEntities.AuthenticationService;
import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.Employees.Employee;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static DataBaseManipulation<PricingChart> pChart = new DataBaseManipulation<>();
    private static DataBaseManipulation<ParkingLot> pLot = new DataBaseManipulation<>();
    private static DataBaseManipulation<RegisteredCustomer> rCustomer = new DataBaseManipulation<>();
    private static Map<Integer, Customer> clientsCustomersMap = new HashMap<>();
    private static Map<Integer, Employee> clientsEmployeeMap = new HashMap<>();

    public SimpleServerClass(int port) {

        super(port);

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

            } else if (request.startsWith("#getAllParkingLots")) {
                sendParkingLots(message, client);

            } else if (request.startsWith("#getPricingChart")) {
                sendPricesChart(message, client);

            } else if (request.startsWith("#updatePrice")) {
                System.out.println("Update");
                updatePriceChart(message, client);

            } else if (request.startsWith("#updateAmount")) {
                updateSubscriptionAmount(message, client);
            }else if (request.startsWith("#ConnectionAlive")) {
                System.out.println("Alive!");
            } else {
                System.out.println("no selection was done!!!");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    protected void registerUser(Message message,ConnectionToClient client){
        // TODO: 1/11/2023 handle messege from client to get email,password,name,.. all items of a Regular customer
        String name="aasd",email="blalba";
        if (AuthenticationService.checkEmailExistance(email)){
            // TODO: 1/11/2023 use a username instead more secure
            message.setMessage("email already exists");
            return;
        }
        //TODO:1/11/23 get all fields of Registered cutomer that are relevant to being a user
        RegisteredCustomer customer=new RegisteredCustomer();
        rCustomer.save(customer,RegisteredCustomer.class);
        customer=rCustomer.getLastAdded(RegisteredCustomer.class);
        clientsCustomersMap.put( customer.getId(), customer);
        client.setInfo("userId",customer.getId());
        message.setMessage("RegistrationSuccessful");
    }
    protected void Login(Message message,ConnectionToClient client){

        // TODO: 1/11/2023 check errors
        String email,password,userName="";
        String[] mess=message.getMessage().split("&");
        email=mess[1];
        password=mess[2];
        int clientType=0;
        clientType=AuthenticationService.checkAuthintecatedEntityType(email,password);
        if(clientType<=0)
            message.setMessage("authintication failed!");
        else if(clientType<5){
            Employee user=AuthenticationService.getAuthenticatedEntity(email,password);
            clientsEmployeeMap.put( user.getId(), user);
            client.setInfo("userId",user.getId());
            userName=user.getFirstName()+user.getLastName();
        }
        else if(clientType<=5){
            RegisteredCustomer  user=AuthenticationService.getAuthenticatedEntity(email,password);
            clientsCustomersMap.put( user.getId(), user);
            client.setInfo("userId",user.getId());
            userName=user.getFirstName()+user.getLastName();
        }

        message.setMessage("Welcome " + userName + "!");
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
}
