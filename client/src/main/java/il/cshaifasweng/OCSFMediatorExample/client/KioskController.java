package il.cshaifasweng.OCSFMediatorExample.client;

import com.sun.xml.bind.v2.TODO;
import il.cshaifasweng.LogInEntities.User;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.EntranceExitResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.KioskSubscriber;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import il.cshaifasweng.customerCatalogEntities.AbstractOrder;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

import static il.cshaifasweng.OCSFMediatorExample.client.currentClientScreenRequest.*;
import static javafx.application.Platform.runLater;

public class KioskController {
    int KioskID = SimpleChatClient.getCurrentKioskID().getId();
    @FXML
    private Button Exit;

    @FXML
    private Button dontHaveOrder;

    @FXML
    private Button haveOrder;

    @FXML
    private Button haveSub;
    @FXML
    private Button back;


    final CountDownLatch latch = new CountDownLatch(1);

    protected
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");


    @FXML
    void onDontHaveOrder(ActionEvent event) throws IOException {
        // TODO: 25/02/2023 check this one out in the Server
        popupWindow("Enter Parking Lot Using order", "OfflineOrder", "#verifyOfflineOrder");
    }

    @FXML
    void onBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
    }


    // TODO: 15/02/2023 Bare in mind that the client here is the kiosk window,not the client
    //  , the client thus shouldn't be registered in the server as a customer at all !
    @FXML
    void onHaveOrder(ActionEvent event) {
        popupWindow("Enter Parking Lot Using order", "OnlineOrder", "#verifyOrder");
    }

    private void popupWindow(String title, String type, String verifyType) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userTextField = new TextField("Please enter your ID");
        PasswordField passTextField = new PasswordField();
        TextField carTextField = new TextField("Please enter your Car number");
        Label idLabel = new Label("ID:");
        idLabel.setText("ID");
        Label subscriptionLabel = new Label(type + ":");
        subscriptionLabel.setText(" Please enter" + type + " number");
        Label CarLabel = new Label("Car:");
        CarLabel.setText(" Please enter Car number");
        grid.add(idLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(subscriptionLabel, 0, 1);
        grid.add(passTextField, 1, 1);
        grid.add(CarLabel, 0, 2);
        grid.add(carTextField, 1, 2);
        Label errorLabel = new Label("Invalid ID or " + type + " number");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        grid.add(errorLabel, 0, 5, 2, 1);
        Button login = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(login);
        grid.add(hbBtn, 1, 4);
        Scene popUpScene = new Scene(grid);
        popUp.setScene(popUpScene);
        popUp.show();
        login.setOnAction(event1 -> {
            sendMessagesToServer(verifyType + "&" + KioskID + "&" + userTextField.getText() + "&" + passTextField.getText() + "&" + carTextField.getText());
            try {
                latch.await();
                if (SimpleChatClient.getOrderToBePaid() != null) {
                    popUp.close();
                    sendParkingRequest(KioskID + "&" + userTextField.getText() + "&" + passTextField.getText() + "&" + carTextField.getText() + "&" + type);
                } else {
                    errorLabel.setVisible(true);
                    userTextField.setStyle(errorStyle);
                    passTextField.setStyle(errorStyle);
                    userTextField.setText("");
                    passTextField.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendParkingRequest(String request) throws Exception {
        if (SimpleChatClient.getCurrentRequest() == ENTER_PARKING.ordinal()) {
            sendMessagesToServerWithObject("#EnterParkingLot&" + request);

        } else if (SimpleChatClient.getCurrentRequest() == EXIT_PARKING.ordinal()) {
            // TODO: 25/02/2023 make customer pay for difference in price !!!!!!!!!
            if (SimpleChatClient.getOrderToBePaid() instanceof OnlineOrder) {
                OnlineOrder order = (OnlineOrder) SimpleChatClient.getOrderToBePaid();
                if (order.getEntryAndExitLog() != null && order.getEntryAndExitLog().getEstimatedExitTime().isBefore(LocalDateTime.now())) {
                    EventBus.getDefault().unregister(this);
                    SimpleChatClient.setRoot("orderPaymentGUI");
                    SimpleChatClient.addScreen("KioskScreen");

                } else {
                    sendMessagesToServerWithObject("#ExitParkingLot&" + request);
                }
            } else if (SimpleChatClient.getOrderToBePaid() instanceof OfflineOrder) {
                if (((OfflineOrder) SimpleChatClient.getOrderToBePaid()).getEntryAndExitLog() != null) {
                    EventBus.getDefault().unregister(this);
                    SimpleChatClient.setRoot("orderPaymentGUI");
                    SimpleChatClient.addScreen("KioskScreen");
                } else {
                    sendMessagesToServerWithObject("#ExitParkingLot&" + request);
                }

            } else
                sendMessagesToServerWithObject("#ExitParkingLot&" + request);


        } else throw new Exception("Invalid invalid Screen Request");
    }

    @FXML
    void onHaveSub(ActionEvent event) {
        popupWindow("Enter Parking Lot Using Subscription", "Subscription", "#verifySubscription");
    }

    @Subscribe
    public void getMessage(KioskSubscriber event) {
        if (event.getMessage().getMessage().startsWith("#verifySubscription") || event.getMessage().getMessage().startsWith("#verifyOrder") || event.getMessage().getMessage().startsWith("#verifyOfflineOrder")) {
            System.out.println("Message");
            System.out.println((event.getMessage().getObject()));
            if (event.getMessage().getObject() != null) {
                SimpleChatClient.setOrderToBePaid((Transactions) event.getMessage().getObject());
            }
            latch.countDown();
            return;
        }
    }

    @Subscribe
    public void getResponse(EntranceExitResponse response) {
        if (response.getMessage().getObject() instanceof EntryAndExitLog) {
            if (response.getMessage().getMessage().startsWith("#EnterParkingLot")) {
                runLater(() -> {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Parking Lot")
                            .text("You have entered the parking lot successfully")
                            .graphic(null)
                            .hideAfter(Duration.seconds(30))
                            .position(Pos.CENTER);
                    notificationBuilder.showConfirm();
                    SimpleChatClient.setCurrentRequest(NONE.ordinal());
                    try {
                        EventBus.getDefault().unregister(this);
                        SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                runLater(() -> {
                    Notifications notificationBuilder = Notifications.create()
                            .title("Parking Lot")
                            .text("You have exited the parking lot successfully")
                            .graphic(null)
                            .hideAfter(Duration.seconds(30))
                            .position(Pos.CENTER);
                    notificationBuilder.showConfirm();
                    SimpleChatClient.setCurrentRequest(NONE.ordinal());
                    try {
                        EventBus.getDefault().unregister(this);
                        SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            runLater(() -> {
                Notifications notificationBuilder = Notifications.create()
                        .title("Parking Lot")
                        .text((String) response.getMessage().getObject())
                        .graphic(null)
                        .hideAfter(Duration.seconds(30))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
                try {
                    EventBus.getDefault().unregister(this);
                    SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    void sendMessagesToServer(String request) {
        try {
            // Check if the connection with the server is alive.
            Message message = new Message(request);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            ;
            e.printStackTrace();
        }

    }

    void sendMessagesToServerWithObject(String request) {
        try {
            // Check if the connection with the server is alive.
            Message message = new Message(request);
            message.setObject(SimpleChatClient.getUser());
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            ;
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }
}
