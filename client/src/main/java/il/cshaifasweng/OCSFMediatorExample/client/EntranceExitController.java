package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.sun.javafx.application.PlatformImpl.runLater;
import static il.cshaifasweng.OCSFMediatorExample.client.currentClientScreenRequest.ENTER_PARKING;
import static il.cshaifasweng.OCSFMediatorExample.client.currentClientScreenRequest.EXIT_PARKING;

public class EntranceExitController {

    @FXML
    private Button Enter;

    @FXML
    private Button Exit;


    @FXML
    private Text parkingLotNumber;
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
            SimpleChatClient.addScreen("EntranceAndExit");
            SimpleChatClient.setRoot("KioskScreen");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        parkingLotNumber.setText("Parking Lot Number: " + SimpleChatClient.getCurrentKioskID().getId());

    }


}
