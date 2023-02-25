package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class PaymentController {

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
    private ProgressIndicator doneIndecator;



    @FXML
    private Pane orderPane;
 @FXML
    private Pane subscriptionPane;


    @FXML
    private TextField subAmmountToPay;

    @FXML
    private TextField subEmail;

    @FXML
    private TextField subEndDay;

    @FXML
    private Label subIDLabel;

    @FXML
    private TextField subLicense;

    @FXML
    private TextField subParkLotID;

    @FXML
    private TextField subStartDay;

    @FXML
    private TextField subType;




    @FXML
    void backToOrder(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
      SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
    }

    static void fillKnownOrder(OnlineOrder onlineOrder, TextField emailTxt, TextField plateNumTxt, TextField dateTxt, TextField pLaddress, TextField parkingHoursTxt, TextField ammountToPay) {
        emailTxt.setText(onlineOrder.getEmail());
        plateNumTxt.setText(onlineOrder.getCar().toString());
        dateTxt.setText(onlineOrder.getDateOfOrder().toString());
        pLaddress.setText(onlineOrder.getParkingLotID().getId()+"");
        parkingHoursTxt.setText(onlineOrder.getDateOfOrder() + " - " + onlineOrder.getExiting() );
        ammountToPay.setText(Double.toString(onlineOrder.getValue()));
    }
    static void fillKnownSubscription(Subscription subscription, TextField emailTxt, TextField plateNumTxt, TextField dateTxt, TextField endDateTxt, TextField pLaddress, TextField ammountToPay,TextField subType) {
        emailTxt.setText(subscription.getEmail());
        plateNumTxt.setText(subscription.getCarsAsString());
        dateTxt.setText(subscription.getStartDate().toString());
        pLaddress.setText(subscription.getParkingLotIdAsString());
        endDateTxt.setText(subscription.getExpirationDate().toString());
        ammountToPay.setText(Double.toString(subscription.getValue()));
        subType.setText(subscription.getClass().getSimpleName());
    }
    private void fillOrderDetails(OnlineOrder onlineOrder) {
        fillKnownOrder(onlineOrder, emailTxt, plateNumTxt, dateTxt, PLaddress, parkingHoursTxt, ammountToPay);
    }
    private void fillSubscriptionDetails(Subscription subscription) {
        fillKnownSubscription(subscription, subEmail, subLicense, subStartDay, subEndDay, subParkLotID, subAmmountToPay,subType);
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
        // TODO: 23/02/2023 this might be the problem , check it out please
        String txt = cvvInput.getText();
        if (!txt.matches("-?([1-9][0-9]*)?")
                || txt.length() > 3){
             cvvInput.setText(txt.substring(0, txt.length() - 1));
        }
    }

    private boolean orderPaymentValidation() {
        // TODO: 23/02/2023 what is these crappy checks !!!!! do it again or i'll fix it
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
                RegisteredCustomer customer =(RegisteredCustomer) SimpleChatClient.getUser();
                if(customer == null)
                    customer = SimpleChatClient.getRegisteredCustomerDetails();
                String customerDet =customer.getId()+"&"+customer.getEmail()+"&"+customer.getFirstName()+"&"+customer.getLastName();
                if (SimpleChatClient.getCurrentOrder() != null) {
                    OnlineOrder newOnlineOrder = SimpleChatClient.getCurrentOrder();
                    newOnlineOrder.setTransaction_method("Credit Card");
                    newOnlineOrder.setTransactionStatus(true);
                    Message message = new Message("#placeOrder&"+customerDet, newOnlineOrder);
                    SimpleClient.getClient().sendToServer(message);

                } else {
                    Subscription subscription = SimpleChatClient.getCurrentSubscription();
                    subscription.setTransaction_method("Credit Card");
                    subscription.setTransactionStatus(true);
                    subscription.setDate(LocalDate.now());
                    Message message = new Message("#addSubscription&"+customerDet, subscription);
                    SimpleClient.getClient().sendToServer(message);

                }
                doneIndecator.setVisible(true);

            } else {
                warningMsg.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
       Platform.runLater(()-> fxmlHandl(event.getMessage()));
       
    }

    private void fxmlHandl(Message event) {

        orderIDTxt.setVisible(true);
        done.setDisable(false);
        back.setDisable(false);
        homeBtn.setVisible(true);
        successLbl.setVisible(true);
        orderIDTxt.setText(event.getObject() + "");
        doneIndecator.setVisible(false);
        try {
            SimpleChatClient.setRegisteredCustomerDetails(null);
            System.out.println(SimpleChatClient.peekScreen());
            String temp= SimpleChatClient.getPreviousScreen();
            SimpleChatClient.setRoot(temp);
            EventBus.getDefault().unregister(this);
            if (SimpleChatClient.getCurrentOrder()!=null){
                Notifications notificationBuilder = Notifications.create()
                    .title("Order Placed")
                        .hideAfter(Duration.seconds(400))
                    .text("Your order has been placed successfully\nYour Order ID is: "+(int)event.getObject())
                    .position(Pos.CENTER);
                notificationBuilder.showConfirm();
            }
            else {
                Notifications notificationBuilder = Notifications.create()
                    .title("Subscription Added")
                    .text("Your subscription has been added successfully\nYour Subscription ID is: "+(int)event.getObject())
                    .position(Pos.CENTER).hideAfter(Duration.seconds(400));
                notificationBuilder.showConfirm();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
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
        System.out.println(SimpleChatClient.peekScreen());
        initPaymentControls();
        if (SimpleChatClient.getCurrentOrder() != null){
            orderPane.setVisible(true);
            subscriptionPane.setVisible(false);
            orderPane.toFront();
            fillOrderDetails(SimpleChatClient.getCurrentOrder());
        }
        else if (SimpleChatClient.getCurrentSubscription()!=null){
            orderPane.setVisible(false);
            subscriptionPane.setVisible(true);
            subscriptionPane.toFront();
            fillSubscriptionDetails(SimpleChatClient.getCurrentSubscription());}
        else
            System.out.println("Error: no order or subscription to pay for");
        doneIndecator.setVisible(false);
    }

}
