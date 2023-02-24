package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class DirectToAvailbleParkController {

    @FXML
    private TableColumn<OnlineOrder, Integer> PLcolumn;

    @FXML
    private ComboBox<Integer> PLid;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<OnlineOrder, String> carNumberColumn;

    @FXML
    private TextField carNumberTxt;

    @FXML
    private TableColumn<OnlineOrder, Integer> customerIDcolumn;

    @FXML
    private Button directBtn;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderIDcolumn;

    @FXML
    private TextField orderIDtxt;

    @FXML
    private TableView<OnlineOrder> orderTable;

    @FXML
    private Button searchBtn;

    @FXML
    void back(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void direct(ActionEvent event) {
        directBtn.setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {

    }


    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        ObservableList<ParkingLotModel> tmp = FXCollections.observableArrayList();
        for (ParkingLot PL :
                PLresults) {
         //TODO: remove current parkingLot id from list and full parkinglots
            PLid.getItems().add(PL.getId());
        }
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
      directBtn.setDisable(true);
        Message message = new Message("#getAllParkingLots");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
