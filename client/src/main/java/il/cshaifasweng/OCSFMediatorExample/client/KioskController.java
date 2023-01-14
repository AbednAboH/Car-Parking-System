package il.cshaifasweng.OCSFMediatorExample.client;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class KioskController {

    @FXML
    private Button Exit;

    @FXML
    private Button dontHaveOrder;

    @FXML
    private Button haveOrder;

    @FXML
    private Button haveSub;

    @FXML
    void onDontHaveOrder(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("orderGUI");
    }


    @FXML
    void onHaveOrder(ActionEvent event) {

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

        TextField userTextField = new TextField();
        PasswordField passTextField = new PasswordField();
        Label idLabel = new Label("ID:");
        idLabel.setText("Please Enter your ID");
        Label subscriptionLabel = new Label("Subscription:");
        subscriptionLabel.setText("Please enter your subscription number");
        grid.add(idLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(subscriptionLabel, 0, 1);
        grid.add(passTextField, 1, 1);

        Button login = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(login);
        grid.add(hbBtn, 1, 4);

        Scene popUpScene = new Scene(grid);
        popUp.setScene(popUpScene);
        popUp.show();
        login.setOnAction(event1 -> {
        });
    }

}
