package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.Order;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class RegisteredCustomerController {

    @FXML
    private TextArea result;

    @FXML
    private Button orderbutton;
    @FXML
    private Button subscriptionbutton;

    @FXML
    private Button placebutton;

    @FXML
    private Button cancelorderbutton;

    @FXML
    private Button cancelsubsbutton1;

    @FXML
    private Button addsubscriptionbutton1;

    @FXML
    private ChoiceBox<Integer> subType;

    @FXML
    void placeOrder(ActionEvent event) {
    }

    @FXML
    void getAllSubscriptions(ActionEvent event) {
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        try {
            Message message = new Message("#cancelOrder");
            SimpleClient.getClient().sendToServer(message);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @FXML
    void cancelSubscription(ActionEvent event) {
        try {
            Message message = new Message("#cancelSubscription");
            SimpleClient.getClient().sendToServer(message);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @FXML
    void AddSubscriptions(ActionEvent event) {

        try {
            Subscription subscription;
            if (subType.getValue() == 2) {
                subscription = new FullSubscription();
            } else {
                subscription = new RegularSubscription();
            }
            Message message = new Message("#addSubscription", subscription);
            SimpleClient.getClient().sendToServer(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showOrders(ActionEvent event) {

        try {
            Message message = new Message("#showOrders");
            SimpleClient.getClient().sendToServer(message);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @FXML
    void showSubscriptions(ActionEvent event) {
        try {
            Message message = new Message("#showSubscriptions");
            SimpleClient.getClient().sendToServer(message);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Subscribe
    public void showOrdersFromServer(OrderHistoryResponse event) {
        List<Order> ordersResults = (List<Order>) event.getMessage().getObject();
        ObservableList<Subscription> tmp = FXCollections.observableArrayList();
        for (Order order :
                ordersResults) {
            result.setText(result.getText() + order.toString());
        }

    }

    @Subscribe
    public void addSubscriptionResponse(UpdateMessageEvent event) {

    }

    @Subscribe
    public void cancelOrderResponse(UpdateMessageEvent event) {

    }

    @Subscribe
    public void cancelSubscriptionResponse(UpdateMessageEvent event) {

    }

    @Subscribe
    public void showSubscriptionsFromServer(SubscriptionResponse event) {
        List<Subscription> ordersResults = (List<Subscription>) event.getMessage().getObject();
        ObservableList<Subscription> tmp = FXCollections.observableArrayList();
        for (Subscription subscription :
                ordersResults) {
            result.setText(result.getText() + subscription.toString());
        }
    }

    @FXML
    void initialize() {
        subType.getItems().add(1);
        subType.getItems().add(2);
    }

}