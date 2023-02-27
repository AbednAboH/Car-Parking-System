package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionsChartResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.models.SubscriptionChartModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static javafx.application.Platform.runLater;

public class PlManagerPriceRequestController {

    @FXML
    private TextField HourlyIDTxt;

    @FXML
    private GridPane SubmitChanges;

    @FXML
    private VBox SuccessStatus;

    @FXML
    private Button applyAmountBtn;

    @FXML
    private Button applyPriceBtn;

    @FXML
    private VBox errorMsgArea;

    @FXML
    private TextField hourlyPriceTxt;

    @FXML
    private TableColumn<?, ?> hoursInMonthColumn;

    @FXML
    private TextField newAmountTxt;

    @FXML
    private Button okBtn;

    @FXML
    private Button resetPricngChartBtn;

    @FXML
    private Button sendRequest;

    @FXML
    private TableColumn<SubscriptionChartModel, Integer> subIDcolumn;

    @FXML
    private TableColumn<SubscriptionChartModel, Double> subPriceColumn;

    @FXML
    private TableView<SubscriptionChartModel> subTable;

    @FXML
    private TextField subToChangeIDtxt;

    @FXML
    private TableColumn<?, ?> subTypeColumn;

    @FXML
    private TableColumn<?, ?> totalPriceColumn;

    PricingChart PCresult,ChangedPCresult;


    @FXML
    void backClick(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            EventBus.getDefault().unregister(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void HideErrorMsg(ActionEvent event) {
        errorMsgArea.setVisible(false);
    }


    boolean isAmountValid() {
        String subID = subToChangeIDtxt.getText();
        String newAmount = newAmountTxt.getText();

        if (subID.compareTo("") == 0||newAmount.compareTo("") == 0) {
            return false;
        }
        try {
            if (Integer.parseInt(newAmount) <= 0||Integer.parseInt(subID) < 3 || Integer.parseInt(subID) > 5)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean isPriceValid() {
        String subID = HourlyIDTxt.getText();
        String newPrice = hourlyPriceTxt.getText();


        if (subID.compareTo("") == 0||newPrice.compareTo("") == 0)
            return false;
        try {
            if (Double.parseDouble(newPrice) <= 0||Integer.parseInt(subID) <= 0 || Integer.parseInt(subID) > 2)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @FXML
    void SendAmountChange(ActionEvent event) {
        String subID = subToChangeIDtxt.getText();
        String newPrice = newAmountTxt.getText();

        if (!isAmountValid()) {
            errorMsgArea.setVisible(true);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
            switch (Integer.parseInt(subID)) {
                case 3 -> ChangedPCresult.setRegularSubHours(Double.parseDouble(newPrice));
                case 4 -> ChangedPCresult.setMultipleCarRegularSubHours(Double.parseDouble(newPrice));
                case 5 -> ChangedPCresult.setFullSubHours(Double.parseDouble(newPrice));
                default -> System.out.println("error");

            }
            fillPricingChart(ChangedPCresult);
            System.out.println("was here ammount change");

        }
    }

    @FXML
    void SendPriceChange(ActionEvent event) {
        String subID = HourlyIDTxt.getText();
        String newAmount = hourlyPriceTxt.getText();

        if (!isPriceValid()) {
            errorMsgArea.setVisible(true);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
            switch (Integer.parseInt(subID)) {
                case 1 -> ChangedPCresult.setKioskPrice(Double.parseDouble(newAmount));
                case 2 -> ChangedPCresult.setOrderBeforeHandPrice(Double.parseDouble(newAmount));
                default -> System.out.println("error");
            }

            fillPricingChart(ChangedPCresult);
            System.out.println("was here price change");

        }
    }

    @FXML
    void resetPricingChart(ActionEvent event) {
        ChangedPCresult=new PricingChart(PCresult);
        fillPricingChart(ChangedPCresult);
    }

    @FXML
    void sendPricingChartRequest(ActionEvent event) {
        if(ChangedPCresult.equals(PCresult)){
            return;
        }
        else{
            try {


                Message msg = new Message("#RequestChangingPCResult", ChangedPCresult);
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void fillPricingChart(PricingChart PCresult) {
        runLater(()->{
            subTable.getItems().clear();
            ObservableList<SubscriptionChartModel> tmp = FXCollections.observableArrayList();
            double rate;
            double[] prices = {PCresult.getKioskPrice(), PCresult.getOrderBeforeHandPrice(), PCresult.getRegularSubHours()
                    , PCresult.getMultipleCarRegularSubHours(), PCresult.getFullSubHours()};
            String[] names = {"Kiosk", "OrderBeforeHand", "RegularSubscription", "RegularSubscriptionMultipl", "FullSubscription"};
            int[] ids = {1, 2, 2, 2, 2};
            for (int i = 0; i < 5; i++) {
                if (i < 2)
                    tmp.add(new SubscriptionChartModel(i + 1, names[i], prices[i], 1, prices[i]));
                else
                    tmp.add(new SubscriptionChartModel(i + 1, names[i], prices[i] * prices[1], (int) Double.parseDouble(String.valueOf(prices[i])), prices[i] * prices[1]));

            }
            subIDcolumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
            subTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            subPriceColumn.setCellValueFactory(new PropertyValueFactory<>("HourlyPrice"));
            hoursInMonthColumn.setCellValueFactory(new PropertyValueFactory<>("HoursInMonth"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Total"));
            subTable.setItems(tmp);
        });
    }

    @Subscribe
    public void getSuccessFromServer(UpdateMessageEvent event) {
        runLater(()->{
            Notifications notificationBuilder = Notifications.create()
                    .title("Success")
                    .text("Your Request Has Been Sent To The Manager")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notificationBuilder.showConfirm();});
    }

    @Subscribe
    public void setSubChartDataFromServer(SubscriptionsChartResults event) {
        PCresult = (PricingChart) event.getMessage().getObject();
        ChangedPCresult=new PricingChart(PCresult);
        fillPricingChart(ChangedPCresult);
    }

    public void initialize() {
        // TODO: 13/01/2023 Need to change the hierarchy of Users in our entities ASAP.
        try {
            System.out.println("init");
            EventBus.getDefault().register(this);
            sendMessagesToServer("#getPricingChart");
            System.out.println("Send to server");
            // update table values
        } catch (Exception e){

        }
    }

    void sendMessagesToServer(String request){
        try {
            // Check if the connection with the server is alive.
            Message message = new Message(request);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
