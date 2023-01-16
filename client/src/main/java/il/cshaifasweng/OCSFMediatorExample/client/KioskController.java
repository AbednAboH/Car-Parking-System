package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.KioskSubscriber;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class KioskController {

    @FXML
    private Button Exit;

    @FXML
    private Button dontHaveOrder;

    @FXML
    private Button haveOrder;

    @FXML
    private Button haveSub;

    final CountDownLatch latch = new CountDownLatch(1);

    protected
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");


    @FXML
    void onDontHaveOrder(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("orderGUI");
    }


    @FXML
    void onHaveOrder(ActionEvent event) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userTextField = new TextField("Please enter your ID");
        PasswordField passTextField = new PasswordField();
        Label idLabel = new Label("ID:");
        idLabel.setText("ID");
        Label subscriptionLabel = new Label("Order:");
        subscriptionLabel.setText(" Please enter Order number");
        grid.add(idLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(subscriptionLabel, 0, 1);
        grid.add(passTextField, 1, 1);
        Label errorLabel = new Label("Invalid ID or Order number");
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
            sendMessagesToServer("#verifyOrder&"+"12&"+ userTextField.getText() + "&" + passTextField.getText());
            try {
                latch.await();
                if(SimpleChatClient.getUser() != null){
                    popUp.close();
                    SimpleChatClient.setRoot("primary");
                }else{
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

    @FXML
    void onHaveSub(ActionEvent event) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userTextField = new TextField("Please enter your ID");
        PasswordField passTextField = new PasswordField();
        Label idLabel = new Label("ID:");
        idLabel.setText("ID");
        Label subscriptionLabel = new Label("Subscription:");
        subscriptionLabel.setText(" Please enter Subscription number");
        grid.add(idLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(subscriptionLabel, 0, 1);
        grid.add(passTextField, 1, 1);
        Label errorLabel = new Label("Invalid ID or Subscription number");
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
            sendMessagesToServer("#verifySubscription&"+"12&"+ userTextField.getText() + "&" + passTextField.getText());
            try {
                latch.await();
                if(SimpleChatClient.getUser() != null){
                    popUp.close();
                    SimpleChatClient.setRoot("primary");
                }else{
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
    @Subscribe
    public void getMessage(KioskSubscriber event){
        if(event.getMessage().getMessage().startsWith("#verifySubscription") || event.getMessage().getMessage().startsWith("#verifyOrder") ){
            System.out.println("Message");
            System.out.println((event.getMessage().getObject()));
            if(event.getMessage().getObject() != null)
                SimpleChatClient.setUser(event.getMessage().getObject());
            latch.countDown();
            return;
        }
    }

    void sendMessagesToServer(String request){
        try {
            // Check if the connection with the server is alive.
            Message message = new Message(request);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {;
            e.printStackTrace();
        }

    }
    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }
}
