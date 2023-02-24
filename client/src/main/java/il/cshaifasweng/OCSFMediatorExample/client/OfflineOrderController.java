package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Screen;
import org.greenrobot.eventbus.EventBus;

import javax.persistence.criteria.CriteriaBuilder;

public class OfflineOrderController {

    @FXML
    private Button back;

    @FXML
    private TextField customerID;

    @FXML
    private TextField customerLastName;

    @FXML
    private TextField customerName;

    @FXML
    private TextField emailInput;

    @FXML
    private Spinner<Integer> exitTime;

    @FXML
    private Spinner<Integer> exitTime1;

    @FXML
    private TextField plateNum;

    @FXML
    private Label pricePerHour;

    @FXML
    private Button submit;

    @FXML
    private Label warningMsg;

    @FXML
    void backToRegisteredCustomer(ActionEvent event) {
        SimpleChatClient.getPreviousScreen();
    }

    @FXML
    void goToPayment(ActionEvent event) {


    }

    @FXML
    void plateNumTxtChange(InputMethodEvent event) {

    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);


    }
}
