package il.cshaifasweng.OCSFMediatorExample.client;

<<<<<<< HEAD
import il.cshaifasweng.LocalDateAttributeConverter;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
=======
import il.cshaifasweng.Message;
import il.cshaifasweng.customerCatalogEntities.Order;
>>>>>>> origin/orderBranch
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
<<<<<<< HEAD
import javafx.scene.control.cell.PropertyValueFactory;
import net.bytebuddy.asm.Advice;
=======
>>>>>>> origin/orderBranch
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javafx.event.ActionEvent;
<<<<<<< HEAD

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
=======
import java.io.IOException;
import java.text.SimpleDateFormat;
>>>>>>> origin/orderBranch

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
<<<<<<< HEAD
                Order newOrder = new Order(2, 2,
                        true, dateChoice.getValue(), dateChoice.getValue(),
                        plateNum.getText(), emailInput.getText());
=======
                SimpleDateFormat date = new SimpleDateFormat();
                Order newOrder = new Order(1, 1,
                        true, date, date, plateNum.getText(), emailInput.getText());
>>>>>>> origin/orderBranch
                Message message = new Message("#placeOrder", newOrder);
                SimpleClient.getClient().sendToServer(message);
            } else {

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
<<<<<<< HEAD
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

=======
        System.out.println("done");
>>>>>>> origin/orderBranch
    }

    @FXML
    void initialize() {
<<<<<<< HEAD
        try {
            EventBus.getDefault().register(this);
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
=======
        EventBus.getDefault().register(this);
>>>>>>> origin/orderBranch
    }


}
