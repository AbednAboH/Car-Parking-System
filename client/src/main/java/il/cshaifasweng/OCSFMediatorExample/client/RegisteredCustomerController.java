package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RegisteredCutomerSubscriber;
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
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class RegisteredCustomerController {
    RegisteredCustomer rg = new RegisteredCustomer();

    @FXML
    private Button complaintbutton;

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
    private TextField passwordtxt;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField fnametxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TextField lnametxt;

    @FXML
    private Button logoutbutton;

    @FXML
    private Button addsubscriptionbutton1;

    @FXML
    private ChoiceBox<Integer> subType;

    @FXML
    private Button savebutton;

    @FXML
    private Button editbutton;

    @FXML
    void saveInfo(ActionEvent event) throws IOException {
        savebutton.setVisible(false);
        editbutton.setVisible(true);
        idtxt.setEditable(false);
        lnametxt.setEditable(false);
        fnametxt.setEditable(false);
        emailtxt.setEditable(false);
        passwordtxt.setEditable(false);
        Message message = new Message("#updateUser");
        SimpleClient.getClient().sendToServer(message);
    }

    @FXML
    void editInfo(ActionEvent event) {
        savebutton.setVisible(true);
        editbutton.setVisible(false);
        idtxt.setEditable(true);
        lnametxt.setEditable(true);
        fnametxt.setEditable(true);
        emailtxt.setEditable(true);
        passwordtxt.setEditable(true);
    }


    @FXML
    void addComplaint(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("complaint");
    }


    @FXML
    void placeOrder(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("orderGUI");
    }

    @FXML
    void logOut(ActionEvent event) throws Exception {
        SimpleChatClient.setRoot("logInScreen");
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

    @Subscribe
    public void setRegisteredCustomerDataFromServer(RegisteredCutomerSubscriber event) {
        RegisteredCustomer result = (RegisteredCustomer) event.getMessage().getObject();
        rg.setId(result.getId());
        rg.setSubscriptions(rg.getSubscriptions());
        rg.setComplaint(result.getComplaint());
        rg.setEmail(result.getEmail());
        rg.setFirstName(result.getFirstName());
        rg.setLastName(result.getLastName());
        rg.setPassword(result.getPassword());
        rg.setCars(result.getCars());
        System.out.println(rg.getEmail());
        setUserInfo();
    }

    private void setUserInfo(){
        if(rg != null){
            fnametxt.setText(rg.getFirstName());
            lnametxt.setText(rg.getLastName());
            idtxt.setText(rg.getId() + "");
            emailtxt.setText(rg.getEmail());
        }
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        subType.getItems().add(1);
        subType.getItems().add(2);
        Message message = new Message("#getRegisteredCustomer");
        SimpleClient.getClient().sendToServer(message);
    }

}