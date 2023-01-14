package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.Message;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class OrderPaymentController {

    @FXML
    private TextField PLaddress;

    @FXML
    private Button back;

    @FXML
    private TextField cvvInput;

    @FXML
    private TextField dateTxt;

    @FXML
    private Button done;

    @FXML
    private AnchorPane paymentWindow;

    @FXML
    private TextField emailTxt;

    @FXML
    private ChoiceBox<Integer> monthInput;

    @FXML
    private TextField numberInput;

    @FXML
    private TextField parkingHoursTxt;

    @FXML
    private TextField orderIDTxt;

    @FXML
    private Label warningMsg;

    @FXML
    private Label orderLbl;

    @FXML
    private TextField plateNumTxt;

    @FXML
    private ChoiceBox<Integer> yearInput;

    @FXML
    void backToOrder(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("orderGUI");
    }

    private void fillOrderDetails(Order order) {
        emailTxt.setText(order.getEmail());
        plateNumTxt.setText(order.getPlateNum());
        dateTxt.setText(order.getDate().toString());
        PLaddress.setText(order.getParkingLotID().getId()+"");
        parkingHoursTxt.setText(order.getEntering() + ":00 - " + order.getExiting() + ":00");
    }

    @FXML
    void cardNumTxtChange(ActionEvent event) {
        String txt = numberInput.getText();
        if (!txt.matches("-?([1-9][0-9]*)?")
                || txt.length() > 16){
            numberInput.setText(txt.substring(0, txt.length() - 1));
        }
    }

    @FXML
    void cvvTxtChange(ActionEvent event) {
        String txt = cvvInput.getText();
        if (!txt.matches("-?([1-9][0-9]*)?")
                || txt.length() > 3){
             cvvInput.setText(txt.substring(0, txt.length() - 1));
        }
    }

    private boolean orderPaymentValidation() {
        if (cvvInput.getText().length() < 3)
            return false;
        if (numberInput.getText().length() < 16)
            return false;
        return true;
    }

    @FXML
    void saveOrder(ActionEvent event) {
        try {
            if (orderPaymentValidation()) {
                done.setDisable(true);
                back.setDisable(true);
                Order newOrder = SimpleChatClient.getCurrentOrder();
                Message message = new Message("#placeOrder", newOrder);
                SimpleClient.getClient().sendToServer(message);
            } else {
                warningMsg.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
        paymentWindow.setVisible(false);
        orderLbl.setVisible(true);
        orderIDTxt.setVisible(true);
        done.setDisable(false);
        back.setDisable(false);
        //orderIDTxt.setText(event.getMessage().getObject() + "");
    }

    private void initPaymentControls() {
        ArrayList<Integer> months = new ArrayList<>();
        ArrayList<Integer> years = new ArrayList<>();
        int curYear = LocalDate.now().getYear();
        for (int i = 1; i < 13; i++) {
            months.add(i);
            years.add(curYear + i);
        }
        monthInput.getItems().setAll(months);
        monthInput.setValue(1);
        yearInput.getItems().setAll(years);
        yearInput.setValue(1);

        UnaryOperator<TextFormatter.Change> integerFilterNumber = change -> {
            String newText = change.getControlNewText();
            if (newText.length() < 17)
                if (newText.matches("-?([1-9][0-9]*)?"))
                    return change;
            return null;
        };

        UnaryOperator<TextFormatter.Change> integerFilterCVV = change -> {
            String newText = change.getControlNewText();
            if (newText.length() < 4)
                if (newText.matches("-?([1-9][0-9]*)?"))
                    return change;
            return null;
        };
    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        initPaymentControls();
        fillOrderDetails(SimpleChatClient.getCurrentOrder());
    }

}
