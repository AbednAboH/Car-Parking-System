package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ComplaintSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionResponse;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class ComplaintController {
    private List<OnlineOrder> OnlineOrders;
    private List<Subscription> subscriptions;
    @FXML
    private Label ComplaintID;

    @FXML
    private ComboBox<String> ComplaintSubject;

    @FXML
    private TextField LastName;

    @FXML
    private ComboBox<String> Ordersubscription;

    @FXML
    private TextArea complaintBody = new TextArea();

    @FXML
    private TextField customerID;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField orderSubIdText;

    @FXML
    private ComboBox<String> orderSubscriptionBox;

    @FXML
    private ComboBox<Integer> parkingLot;

    @FXML
    private Button submitComplaint;
    @FXML
    private Label status;
    @FXML
    private Button back;

    @FXML
    void backButton(ActionEvent event) {
        try {
            if (SimpleChatClient.getUser() != null) {
                SimpleChatClient.setRoot("RegisteredCustomer");
            } else {
                SimpleChatClient.setRoot("KioskScreen");
                // TODO: 04/02/2023 add homescreen for all users as an option 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void getOrderSub(ActionEvent event) {

    }

    @FXML
    void submitComplaintAction(ActionEvent event) {
        // TODO: 1/16/2023 check viability of input!:
        Message requist_submition = new Message();
        String order_subOrKiosk, pLotId = "null";

        if (Ordersubscription.getValue() != null) {
            if (Ordersubscription.getValue().startsWith("Kiosk"))
                order_subOrKiosk = orderSubIdText.getText();
            else
                order_subOrKiosk = orderSubscriptionBox.getValue();
        } else
            order_subOrKiosk = null;
        if (parkingLot.getValue() != null)
            pLotId = parkingLot.getValue().toString();
        Complaint complaintRequest = new Complaint(ComplaintSubject.getValue()
                , Ordersubscription.getValue(), order_subOrKiosk, complaintBody.getText()
                , LocalDate.now(), LocalDate.now(), true);
        requist_submition.setMessage("#applyComplaint&" + firstName.getText() +
                "&" + LastName.getText() + "&" + customerID.getText() + "&" + email.getText() + "&"
                + pLotId);
        requist_submition.setObject(complaintRequest);
        try {
            SimpleClient.getClient().sendToServer(requist_submition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        submitComplaint.setDisable(true);
    }

    @FXML
    void typeOfSubscription(ActionEvent event) {
        if (Ordersubscription.getValue() != null) {
            if (Ordersubscription.getValue().startsWith("Order")) {
                orderSubscriptionBox.getItems().clear();
                for (OnlineOrder or : OnlineOrders)
                    orderSubscriptionBox.getItems().add(or.toString());
            } else if (Ordersubscription.getValue().startsWith("Subscription")) {
                orderSubscriptionBox.getItems().clear();
                for (Subscription subscription : subscriptions)
                    orderSubscriptionBox.getItems().add(subscription.toString());
            } else if (Ordersubscription.getValue().equals("Kiosk")) {
                orderSubIdText.setDisable(false);
                orderSubscriptionBox.setDisable(true);
            }
//

        }


    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);

        try {
            status.setVisible(false);
            // Check if the connection with the server is alive.
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
            RegisteredCustomer user = (RegisteredCustomer) SimpleChatClient.getUser();
            ComplaintSubject.getItems().add("Complaint about an employee");
            ComplaintSubject.getItems().add("Complaint about costumer service");
            ComplaintSubject.getItems().add("Complaint about a parking lot");
            ComplaintSubject.getItems().add("I can't get my car out/in the parking lot");
            Ordersubscription.getItems().add("Kiosk");

            if (user != null) {

                firstName.setText(user.getFirstName());
                LastName.setText(user.getLastName());
                email.setText(user.getEmail());
                customerID.setText(Integer.toString(user.getId()));

                message.setMessage("#getAllOrders");
                SimpleClient.getClient().sendToServer(message);

                message.setMessage("#showSubscription");
                SimpleClient.getClient().sendToServer(message);

                ComplaintSubject.getItems().add("Problem with my order");
                ComplaintSubject.getItems().add("Problem with my subscription");
                Ordersubscription.getItems().add("Order");
                Ordersubscription.getItems().add("Subscription");

            }
            ComplaintSubject.getItems().add("Other");


            Ordersubscription.setDisable(true);
            orderSubIdText.setDisable(true);
            orderSubscriptionBox.setDisable(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void subjectChange(ActionEvent event) {
        String subject = ComplaintSubject.getValue();
        switch (subject) {
            case "Complaint about an employee":
            case "Complaint about a parking lot":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(true);
                orderSubIdText.setDisable(true);
                orderSubscriptionBox.setDisable(true);
                break;
            case "Complaint about costumer service":
                parkingLot.setDisable(true);
                Ordersubscription.setDisable(true);
                orderSubIdText.setDisable(true);
                orderSubscriptionBox.setDisable(true);
                break;
            case "I can't get my car out/in the parking lot":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(false);
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }

                break;
            case "Problem with my subscription":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(true);
                Ordersubscription.setValue("Subscription");
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }

                break;
            case "Problem with my order":
                parkingLot.setDisable(false);
                Ordersubscription.setValue("Order");
                Ordersubscription.setDisable(true);
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }
                break;
            case "other":
                parkingLot.setDisable(true);
                Ordersubscription.setDisable(true);
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(false);
                }
                break;

        }
        if (parkingLot.isDisable())
            parkingLot.getSelectionModel().clearSelection();
        if (Ordersubscription.isDisable())
            Ordersubscription.getSelectionModel().clearSelection();
        if (orderSubscriptionBox.isDisable())
            orderSubIdText.clear();
        if (orderSubscriptionBox.isDisable())
            orderSubscriptionBox.getSelectionModel().clearSelection();


    }

    @FXML
    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        for (ParkingLot PL : PLresults)
            parkingLot.getItems().add(PL.getId());
    }

    @FXML
    @Subscribe
    public void getAllOrders(OrderHistoryResponse event) {
        OnlineOrders = (List<OnlineOrder>) event.getMessage().getObject();

    }

    @FXML
    @Subscribe
    public void getAllSubscriptions(SubscriptionResponse event) {
        subscriptions = (List<Subscription>) event.getMessage().getObject();
    }

    @FXML
    @Subscribe
    public void checkSubmit(ComplaintSubscriber event) {
        System.out.println("Complaint Was Sent Successfuly");
        status.setVisible(true);
        try {
            if (SimpleChatClient.getUser() == null)
                SimpleChatClient.setRoot("HomePage");
            else
                SimpleChatClient.setRoot("RegisteredCustomer");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
