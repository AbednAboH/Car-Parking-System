package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.LogoutSubscriber;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static com.sun.javafx.application.PlatformImpl.runLater;

public class ExecutiveManagerController {

    @FXML
    private Button handleRequestsBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Label userNameLbl;

    @FXML
    private Button viewPLstatusBtn;

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
    @Subscribe
    public void LogOutStatus(LogoutSubscriber event){
        String msg= (String) event.getMessage().getObject();
        if(msg.startsWith("Success")){

            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            } catch (IOException e) {
                System.out.println("Failed to go back to previous screen");
            }

        }
        else{
            runLater(()->{
                Notifications notificationBuilder;
                notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Error while trying to log out, please try again later")
                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
            });

        }
    }
    @FXML
    void openRequests(ActionEvent event) {
        try {
            EventBus.getDefault().register(this);
            SimpleChatClient.addScreen("ExecutiveManager");
            SimpleChatClient.setRoot("ExecManagerPriceRequestsHandler");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void viewPLstatus(ActionEvent event) {
        try {
            SimpleChatClient.addScreen("ExecutiveManager");
            SimpleChatClient.setRoot("parkingLotStatus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
