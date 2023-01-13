package il.cshaifasweng.OCSFMediatorExample.server;

import com.google.gson.Gson;
import il.cshaifasweng.DataManipulationThroughDB.DataBaseManipulation;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import il.cshaifasweng.customerCatalogEntities.Order;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static DataBaseManipulation<PricingChart> pChart=new DataBaseManipulation<PricingChart>();
    private static DataBaseManipulation<ParkingLot> pLot= new DataBaseManipulation<ParkingLot>();
    private static DataBaseManipulation<Order> orderHandler= new DataBaseManipulation<Order>();

    public SimpleServerClass(int port) {

        super(port);

    }



    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Gson gson = new Gson();
        Message message = (Message) msg;
        String request = message.getMessage();

        try {
            if (request.isBlank()) {
                    message.setMessage("Error! we got an empty message");
                    client.sendToClient(message);
                }
                else if (request.startsWith("#getAllParkingLots")) {
                    sendParkingLots(message, client);
                }
                else if (request.startsWith("#getPricingChart")) {
                    sendPricesChart(message, client);
                }
                else if (request.startsWith("#updatePrice")) {
                    updatePriceChart(message, client);
                }
                else if (request.startsWith("#updateAmount")) {
                    updateSubscriptionAmount(message, client);
                }
                else if (request.startsWith("#placeOrder")) {
                    placeOrder(message, client);
                }
                else {
                    System.out.println("no selection was done!!!");
                }


        } catch (Exception ex) {
            ex.printStackTrace();
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

    public void sendParkingLots(Message message, ConnectionToClient client) throws IOException,Exception {
        message.setObject(pLot.getAll(ParkingLot.class));
        client.sendToClient(message);
    }

    public void sendPricesChart(Message message, ConnectionToClient client) throws IOException,Exception {
        message.setObject(pChart.getAll(PricingChart.class));
        client.sendToClient(message);
    }

    public void updatePriceChart(Message message, ConnectionToClient client) throws IOException,Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);

            PricingChart chartObject=pChart.get(Integer.parseInt(subID),PricingChart.class);
            chartObject.setRate((Double) message.getObject());
            pChart.update(chartObject);
    }

    public void updateSubscriptionAmount(Message message, ConnectionToClient client) throws IOException,Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);
        PricingChart chartObject=pChart.get(Integer.parseInt(subID),PricingChart.class);
        chartObject.setRate((Integer) message.getObject());
        pChart.update(chartObject);
    }

    public void placeOrder(Message message, ConnectionToClient client) throws IOException,Exception {
        orderHandler.save((Order)message.getObject(), Order.class);
        client.sendToClient(message);
    }
}
