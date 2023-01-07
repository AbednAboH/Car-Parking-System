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

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static ArrayList<ParkingLot> parkingLots = new ArrayList<>();
    private static ArrayList<PricingChart> pricingChart;
    private static HashMap<String, Double> prices = new HashMap<>();
    private static final MySQL sqlDB=new MySQL();
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
                return;
            }
            if (request.startsWith("#getAllParkingLots")) {
                sendParkingLots(message, client);
                return;
            }
            if (request.startsWith("#getPricingChart")) {
                sendPricesChart(message, client);
                return;
            }
            if (request.startsWith("#updatePrice")) {
                updatePriceChart(message, client);
                return;
            }
            if (request.startsWith("#updateAmount")) {
                updateSubscriptionAmount(message, client);
                return;
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

    public void sendParkingLots(Message message, ConnectionToClient client) throws IOException {

        message.setObject(parkingLots);
        client.sendToClient(message);
    }

    public void sendPricesChart(Message message, ConnectionToClient client) throws IOException {
        message.setObject(pricingChart);
        client.sendToClient(message);
    }

    public void updatePriceChart(Message message, ConnectionToClient client) throws IOException {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);
//        switch (subID) {
//            case "1":
//                pricingChart.setParkViaKioskHourly((Double) message.getObject());
//                break;
//            case "2":
//                pricingChart.setOneTimePurchaseHourly((Double) message.getObject());
//                break;
//        }
    }

    public void updateSubscriptionAmount(Message message, ConnectionToClient client) throws IOException {
        String msg = message.getMessage().replaceAll(" ", "");
        int idx = msg.indexOf(":");
        String subID = msg.substring(idx + 1);
//        switch (subID) {
//            case "3":
//                pricingChart.setRegularSubMonthlyHours((int) message.getObject());
//                break;
//            case "4":
//                pricingChart.setRegularSubWithCarsMonthlyHours((int) message.getObject());
//                break;
//            case "5":
//                pricingChart.setFullSubMonthlyHours((int) message.getObject());
//                break;
//        }
    }
}