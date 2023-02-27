package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AllUsagesController {

    @FXML
    private Button dontHaveOrder;

    @FXML
    private Button haveOrder;

    @FXML
    private Button haveSub111;

    @FXML
    void KioskScreen(ActionEvent event) {
        SimpleChatClient.addScreen("allUsages");
        try {
            SimpleChatClient.setRoot("KioskSetupPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void entranceAndExitParkingLot(ActionEvent event) {
        SimpleChatClient.addScreen("allUsages");
        try {
            SimpleChatClient.setRoot("EntranceAndExitPageSetup");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void mainPage(ActionEvent event) {
        SimpleChatClient.addScreen("allUsages");
        try {
            SimpleChatClient.setRoot("mainPage");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
