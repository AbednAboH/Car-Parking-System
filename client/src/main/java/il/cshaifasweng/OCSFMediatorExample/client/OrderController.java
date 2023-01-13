package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class OrderController {

    @FXML
    private DatePicker dateChoice;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField endingTime;

    @FXML
    private ChoiceBox<Integer> plChoice;

    @FXML
    private TextField plateNum;

    @FXML
    private TextField startingTime;

    private boolean orderInfoValidation() {
        if (plateNum.getText().compareTo("") == 0)
            return false;
        if (startingTime.getText().compareTo("") == 0)
            return false;
        if (endingTime.getText().compareTo("") == 0)
            return false;
        if (emailInput.getText().compareTo("") == 0)
            return false;
        return true;
    }

    @FXML
    void saveOrder(ActionEvent event) {
        try {
            if (orderInfoValidation()) {
                Order newOrder = new Order();
                Message message = new Message("#placeOrder", newOrder);
                SimpleClient.getClient().sendToServer(message);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
        emailInput.setText("done");
    }

    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        ObservableList<ParkingLotModel> tmp = FXCollections.observableArrayList();
        for (ParkingLot PL :
                PLresults) {
            plChoice.getItems().add(PL.getId());
        }
    }

    @FXML
    void initialize() {
        try {
            EventBus.getDefault().register(this);
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
