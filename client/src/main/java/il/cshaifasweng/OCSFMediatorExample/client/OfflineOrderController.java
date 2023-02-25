package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.OneTimeCustomer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionsChartResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.offlineOrderSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.offlineOrdersSubscriber;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Screen;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javafx.application.Platform.runLater;

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
    private double perHourPrice;

    @Subscribe
    public void onEvent(offlineOrdersSubscriber subscriber) {
        Message msg = subscriber.getMessage();
        if (msg.getObject() instanceof Integer){
           runLater(()->{
               Notifications notificationBuilder = Notifications.create()
                       .title("Kiosk Order Submitted")
                       .text("Your Kiosk Order has been submitted successfully\n Your Order ID is: "+msg.getObject()+"\n"+
                               "please note that if you don't use this order within 10 minutes it will be canceled")
                       .graphic(null)
                       .hideAfter(javafx.util.Duration.seconds(400))
                       .position(Pos.CENTER);
               notificationBuilder.showConfirm();
               customerLastName.clear();
               customerName.clear();
               customerID.clear();
               emailInput.clear();
               plateNum.clear();
               initInfoControls();
           });
        }
        else {
            runLater(()->{Notifications notificationBuilder = Notifications.create()
                    .title("There was an error submitting your order")
                    .text((String) msg.getObject())
                    .graphic(null)
                    .hideAfter(javafx.util.Duration.seconds(400))
                    .position(Pos.CENTER);
            notificationBuilder.showError();});

        }
    }
    @FXML
    void backToRegisteredCustomer(ActionEvent event) {
    }

    @FXML
    void goToPayment(ActionEvent event) {
        try {
            if (orderInfoValidation()) {
                System.out.println("Message");

                LocalDateTime exit=LocalDateTime.now();
                exit=exit.withHour(exitTime.getValue());
                exit=exit.withMinute(exitTime1.getValue());
                System.out.println(SimpleChatClient.getCurrentKioskID().getId());
                OneTimeCustomer oneTimeCustomer = new OneTimeCustomer(Integer.parseInt( customerID.getText()),emailInput.getText(),customerName.getText(), customerLastName.getText(),null);
                OfflineOrder newOnlineOrder = new OfflineOrder(oneTimeCustomer,SimpleChatClient.getCurrentKioskID(),exit,plateNum.getText(),emailInput.getText());
                Message msg = new Message("#setOfflineOrder", newOnlineOrder);
                SimpleClient.getClient().sendToServer(msg);
//                SimpleChatClient.addScreen("orderGUI");

            } else {
                Notifications notificationBuilder = Notifications.create()
                        .title("There was an error submitting your order")
                        .text(orderInfoValidationShowNotification())
                        .graphic(null)
                        .hideAfter(javafx.util.Duration.seconds(20))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void setSubChartDataFromServer(SubscriptionsChartResults event) {
        PricingChart PCresult = (PricingChart) event.getMessage().getObject();
        perHourPrice=PCresult.getOrderBeforeHandPrice();
        runLater(new Runnable() {
            @Override
            public void run() {
                pricePerHour.setText("Price Per Hour is: "+perHourPrice+" NIS");

            }
        });
    }

    @FXML
    void plateNumTxtChange(InputMethodEvent event) {
        String txt = plateNum.getText();
        if (!txt.matches("-?([1-9][0-9]*)?")
                || txt.length() > 10){
            plateNum.setText(txt.substring(0, txt.length() - 1));
        }
    }
    @FXML
    public void initialize(){
        EventBus.getDefault().register(this);
        initInfoControls();
    }
    private void initInfoControls() {
        LocalDateTime minDate = LocalDateTime.now();;
        exitTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        exitTime.getValueFactory().setValue(minDate.getHour()+1);
        exitTime1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        exitTime1.getValueFactory().setValue(minDate.getMinute()+1);
        Message message = new Message("#getPricingChart");

        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private boolean orderInfoValidation() {
        // TODO: 23/02/2023 add validation indicators for all fields , this is not a job well done !
        return InputValidator.isValidPlateNumber(plateNum.getText()) &&
                InputValidator.isValidEmail(emailInput.getText()) &&
                InputValidator.isValidNumber(customerID.getText()) &&
                InputValidator.isValidName(customerName.getText()) &&
                InputValidator.isValidName(customerLastName.getText());
    }
    private String orderInfoValidationShowNotification() {
        // TODO: 23/02/2023 add validation indicators for all fields , this is not a job well done !
        String out="";

        out+= InputValidator.isValidPlateNumber(plateNum.getText())? "" : "Invalid Plate Number\n";
        out+= InputValidator.isValidEmail(emailInput.getText())? "" : "Invalid Email\n";
        out+= InputValidator.isValidNumber(customerID.getText())? "" : "Invalid ID\n";
        out+= InputValidator.isValidName(customerName.getText())? "" : "Invalid Name\n";
        out+= InputValidator.isValidName(customerLastName.getText())? "" : "Invalid Last Name\n";
        return out;
    }
}
