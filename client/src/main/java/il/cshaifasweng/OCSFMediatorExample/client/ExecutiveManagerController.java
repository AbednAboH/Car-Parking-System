package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class ExecutiveManagerController {

    @FXML
    private Button handleRequestsBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Label userNameLbl;

    @FXML
    void logOutUser(ActionEvent event) {
        Message msg=new Message("#LogOut");
        try {
            SimpleClient.getClient().sendToServer(msg);
        }
        catch (IOException e) {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("Error while trying to log out, please try again later")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    void openRequests(ActionEvent event) {
        try {
            SimpleChatClient.addScreen("ExecutiveManager");
            SimpleChatClient.setRoot("ExecManagerPriceRequestsHandler");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
