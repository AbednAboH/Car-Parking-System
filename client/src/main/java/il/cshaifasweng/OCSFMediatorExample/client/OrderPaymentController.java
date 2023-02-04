package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.Message;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    private Label successLbl;

    @FXML
    private Button homeBtn;

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
    private Label orderIDTxt;

    @FXML
    private Label warningMsg;

    @FXML
    private TextField ammountToPay;


    @FXML
    private TextField plateNumTxt;

    @FXML
    private ChoiceBox<Integer> yearInput;

    @FXML
    void backToOrder(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("orderGUI");
    }
    static void fillKnownOrder(Order order, TextField emailTxt, TextField plateNumTxt, TextField dateTxt, TextField pLaddress, TextField parkingHoursTxt, TextField ammountToPay) {
        emailTxt.setText(order.getEmail());
        plateNumTxt.setText(order.getPlateNum());
        dateTxt.setText(order.getDateOfOrder().toString());
        pLaddress.setText(order.getParkingLotID().getId()+"");
        parkingHoursTxt.setText(order.getEntering() + ":00 - " + order.getExiting() + ":00");
        ammountToPay.setText(Double.toString(order.getValue()));
    }
    private void fillOrderDetails(Order order) {
        fillKnownOrder(order, emailTxt, plateNumTxt, dateTxt, PLaddress, parkingHoursTxt, ammountToPay);
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
    void goToHome(ActionEvent event) {

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
                newOrder.setTransaction_method("Credit Card");
                newOrder.setTransactionStatus(true);

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
        fxmlHandl(event);
    }

    private void fxmlHandl(UpdateMessageEvent event) {
         paymentWindow.setVisible(false);
        orderIDTxt.setVisible(true);
        done.setDisable(false);
        back.setDisable(false);
        homeBtn.setVisible(true);
        successLbl.setVisible(true);
        System.out.println(event.getMessage().getObject());
        orderIDTxt.setText(event.getMessage().getObject() + "");
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
