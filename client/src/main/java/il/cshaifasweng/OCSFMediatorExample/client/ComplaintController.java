package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class ComplaintController {

    @FXML
    private Label ComplaintID;

    @FXML
    private ComboBox<?> ComplaintSubject;

    @FXML
    private TextField LastName;

    @FXML
    private ComboBox<?> Ordersubscription;

    @FXML
    private ComboBox<Integer> parkingLot;

    @FXML
    private CheckBox SpecificParkingLot;

    @FXML
    private TextArea complaintBody;

    @FXML
    private TextField customerID;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField firstName1;

    @FXML
    private Button submitComplaint;

    @FXML
    void submitComplaintAction(ActionEvent event) {

    }
    Object Plots;
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);

        try {
            // Check if the connection with the server is alive.
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        for (ParkingLot PL :PLresults)
            parkingLot.getItems().add(PL.getId());

    }
}
