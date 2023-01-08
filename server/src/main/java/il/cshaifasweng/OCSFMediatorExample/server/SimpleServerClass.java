package il.cshaifasweng.OCSFMediatorExample.server;

import com.google.gson.Gson;
import il.cshaifasweng.Interfaces.DataBaseManipulation;
import il.cshaifasweng.MySQL;
import il.cshaifasweng.ParkingLot;
import il.cshaifasweng.PricingChart;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.jvnet.staxex.MtomEnabled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleServerClass extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private static DataBaseManipulation<PricingChart> pChart=new DataBaseManipulation<PricingChart>();
    private static DataBaseManipulation<ParkingLot> pLot= new DataBaseManipulation<ParkingLot>();
    public SimpleServerClass(int port) {

        super(port);

    }



    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Gson gson = new Gson();
        Message message = (Message) msg;
        String request = message.getMessage();

        try {
            System.out.println("Try");
            if (request.isBlank()) {
                    message.setMessage("Error! we got an empty message");
                    client.sendToClient(message);
                    System.out.println("black");
                }
                else if (request.startsWith("#getAllParkingLots")) {
                    System.out.println("PL");
                    sendParkingLots(message, client);

                }
                else if (request.startsWith("#getPricingChart")) {
                    System.out.println("PC");
                    sendPricesChart(message, client);

                }
                else if (request.startsWith("#updatePrice")) {
                    System.out.println("Update");
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

        PricingChart chartObject = MySQL.getEntityByName(Integer.parseInt(subID), "Prices");
        chartObject.setRate((Double) message.getObject());
        MySQL.update(chartObject);
    }
}
