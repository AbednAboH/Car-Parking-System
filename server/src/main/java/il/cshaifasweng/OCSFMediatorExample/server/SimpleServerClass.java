package il.cshaifasweng.OCSFMediatorExample.server;

import com.google.gson.Gson;
import il.cshaifasweng.MySQL;
import il.cshaifasweng.ParkingLot;
import il.cshaifasweng.PricingChart;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

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
        List<ParkingLot> pl=MySQL.acquireEntitiesFromDB("Lot");
        for (ParkingLot p :pl)
            System.out.println(p);
        message.setObject(pl);
        client.sendToClient(message);
    }

    public void sendPricesChart(Message message, ConnectionToClient client) throws IOException,Exception {
        List<PricingChart> pc=MySQL.acquireEntitiesFromDB("Prices");
        for (PricingChart p:pc)
            System.out.println(p);
        message.setObject(MySQL.acquireEntitiesFromDB("Prices"));
        client.sendToClient(message);
    }

    public void updatePriceChart(Message message, ConnectionToClient client) throws IOException,Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);

            PricingChart chartObject=MySQL.getEntityByName(Integer.parseInt(subID),"Prices");
            chartObject.setRate((Double) message.getObject());
            MySQL.update(chartObject);
    }

    public void updateSubscriptionAmount(Message message, ConnectionToClient client) throws IOException,Exception {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);

        PricingChart chartObject = MySQL.getEntityByName(Integer.parseInt(subID), "Prices");
        chartObject.setRate((Double) message.getObject());
        MySQL.update(chartObject);
    }
}
