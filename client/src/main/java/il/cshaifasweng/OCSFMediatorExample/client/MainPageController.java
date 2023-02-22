package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Button EnterAsVisitor;

    @FXML
    private Button LogIn;

    @FXML
    void LogInScreen(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("logInScreen");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void visitor(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("visitorsController");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
