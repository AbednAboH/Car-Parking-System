package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ComplaintSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionResponse;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class ComplaintController {
    private boolean allValid = true;
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
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
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
        String order_subOrKiosk = null, pLotId = "null";
        allValid = true;
        allValid=allValid&InputValidator.isValidName(firstName.getText());
        if(!allValid){
            Notifications notification = Notifications.create()
                    .title("Invalid First Name")
                    .text("Enter a valid First Name.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showWarning();
        }
        allValid=allValid&InputValidator.isValidName(LastName.getText());
        if(!allValid){
            Notifications notification = Notifications.create()
                    .title("Invalid Last Name")
                    .text("Enter a valid Last Name.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showWarning();
        }
        allValid=allValid&InputValidator.isValidEmail(email.getText());
        if(!allValid){
            Notifications notification = Notifications.create()
                    .title("Invalid Email")
                    .text("Enter a valid Email.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showWarning();
        }
        allValid=allValid&InputValidator.isValidNumber(customerID.getText());
        if(!allValid){
            Notifications notification = Notifications.create()
                    .title("Invalid Customer ID")
                    .text("Enter a valid Customer ID.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showWarning();
        }

        if (Ordersubscription.getValue() != null) {
            if (Ordersubscription.getValue().startsWith("Kiosk")) {
                if (orderSubIdText.getText().compareTo("") != 0)
                    order_subOrKiosk = orderSubIdText.getText();
                else {
                    allValid = false;
                    Notifications notification = Notifications.create()
                            .title("No ID given")
                            .text("Enter an Order/Subscription ID.")
                            .hideAfter(Duration.seconds(5))
                            .position(Pos.CENTER);
                    notification.showWarning();
                }
            } else {
                if (orderSubscriptionBox.getValue() != null)
                    order_subOrKiosk = orderSubscriptionBox.getValue();
                else {
                    allValid = false;
                    Notifications notification = Notifications.create()
                            .title("Choose Order/Subscription")
                            .text("Choose an Order/Subscription from the choice list.")
                            .hideAfter(Duration.seconds(5))
                            .position(Pos.CENTER);
                    notification.showWarning();
                }
            }
        }

        if (parkingLot.getValue() != null && allValid)
            pLotId = parkingLot.getValue().toString();
        else {
            if (allValid) {
                allValid = false;
                Notifications notification = Notifications.create()
                        .title("Choose Parking Lot")
                        .text("Choose a Parking lot ID from the choice list.")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notification.showError();
            }
        }
        if (allValid) {
            if (ComplaintSubject.getValue() != null) {
                if (complaintBody.getText().length() >= 10) {
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
                } else {
                    allValid = false;
                    Notifications notification = Notifications.create()
                            .title("Complaint message is too short")
                            .text("Complaint message must be at least 10 characters long.")
                            .hideAfter(Duration.seconds(5))
                            .position(Pos.CENTER);
                    notification.showError();
                }
            } else {
                allValid = false;
                Notifications notification = Notifications.create()
                        .title("Choose Complaint Subject")
                        .text("Choose a Complaint subject from the choice list.")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notification.showError();
            }
        }
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
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
