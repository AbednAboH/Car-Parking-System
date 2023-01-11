package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;

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

//    @FXML
//    void saveOrder(ActionEvent event) {
//        try {
//            if (orderInfoValidation()) {
//                SimpleDateFormat date = new SimpleDateFormat();
//                Order newOrder = new Order(1, 1,
//                        true, date, date, plateNum.getText(), emailInput.getText());
//                Message message = new Message("#placeOrder", newOrder);
//                SimpleClient.getClient().sendToServer(message);
//            } else {
//
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
        System.out.println("done");
    }

//    @FXML
//    void initialize() {
//        EventBus.getDefault().register(this);
//    }


}
