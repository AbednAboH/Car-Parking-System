package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;

import static il.cshaifasweng.OCSFMediatorExample.client.currentClientScreenRequest.ENTER_PARKING;
import static il.cshaifasweng.OCSFMediatorExample.client.currentClientScreenRequest.EXIT_PARKING;

public class EntranceExitController {

    @FXML
    private Button Enter;

    @FXML
    private Button Exit;

    @FXML
    void Enter(ActionEvent event) {
        SimpleChatClient.setCurrentRequest(ENTER_PARKING.ordinal());
        goToScreen();

    }

    @FXML
    void exit(ActionEvent event) {
        SimpleChatClient.setCurrentRequest(EXIT_PARKING.ordinal());
        goToScreen();
    }

    private static void goToScreen() {
        try{
            SimpleChatClient.setRoot("KioskScreen");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
//        EventBus.getDefault().register(this);

    }

}
