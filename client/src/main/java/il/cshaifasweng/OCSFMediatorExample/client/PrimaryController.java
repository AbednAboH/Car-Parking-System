package il.cshaifasweng.OCSFMediatorExample.client;


import java.io.IOException;
import java.util.ArrayList;

import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.OCSFMediatorExample.client.models.SubscriptionChartModel;
import il.cshaifasweng.Message;
import il.cshaifasweng.ParkingLot;
import il.cshaifasweng.PricingChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {
    @FXML
    private TableColumn<ParkingLotModel, Integer> PLcolsColumn;

    @FXML
    private TableColumn<ParkingLotModel, Integer> PLdepthColumn;

    @FXML
    private TableColumn<ParkingLotModel, Integer> PLidColumn;

    @FXML
    private TableColumn<ParkingLotModel, Integer> PLrowsColumn;

    @FXML
    private TableView<ParkingLotModel> PLtable;

    @FXML
    private Button applyAmountBtn;

    @FXML
    private Button applyPriceBtn;

    @FXML
    private AnchorPane errorMsgArea;

    @FXML
    private Button okBtn;

    @FXML
    private TableColumn<SubscriptionChartModel, Integer> subIDcolumn;

    @FXML
    private TableColumn<SubscriptionChartModel, String> subTypeColumn;

    @FXML
    private TableColumn<SubscriptionChartModel, Double> subPriceColumn;

    @FXML
    private TableColumn<SubscriptionChartModel, Integer> hoursInMonthColumn;

    @FXML
    private TableColumn<SubscriptionChartModel, Double> totalPriceColumn;

    @FXML
    private TableView<SubscriptionChartModel> subTable;

    @FXML
    private TextField subToChangeIDtxt;

    @FXML
    private TextField newAmountTxt;

    @FXML
    private TextField hourlyPriceTxt;

    @FXML
    private TextField HourlyIDTxt;


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
    void SendPriceChange(ActionEvent event) {
        String subID = HourlyIDTxt.getText();
        String newPrice = hourlyPriceTxt.getText();
        if (!isPriceValid()) {
            errorMsgArea.setVisible(true);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
            try {
                Message message = new Message("#updatePrice:" + subID,
                        Double.parseDouble(newPrice));
                SimpleClient.getClient().sendToServer(message);

                message.setMessage("#getPricingChart");
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    void SendAmountChange(ActionEvent event) {
        String subID = subToChangeIDtxt.getText();
        String newAmount = newAmountTxt.getText();
        if (!isAmountValid()) {
            errorMsgArea.setVisible(true);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
            try {
                Message message = new Message("#updateAmount:" + subID,
                        Integer.parseInt(newAmount));
                SimpleClient.getClient().sendToServer(message);

                message.setMessage("#getPricingChart");
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        try {
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);

            message.setMessage("#getPricingChart");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        ArrayList<ParkingLot> PLresults = (ArrayList<ParkingLot>) event.getMessage().getObject();
        ObservableList<ParkingLotModel> tmp = FXCollections.observableArrayList();
        int i = 1;
        for (ParkingLot PL :
                PLresults) {

            tmp.add(new ParkingLotModel(i, PL.getRowCapacity()));
            i++;
        }

        PLidColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PLrowsColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        PLcolsColumn.setCellValueFactory(new PropertyValueFactory<>("rowsInEachFloor"));
        PLdepthColumn.setCellValueFactory(new PropertyValueFactory<>("rowCapacity"));
        PLtable.setItems(tmp);
    }

    @Subscribe
    public void setSubChartDataFromServer(SubscriptionsChartResults event) {
        ArrayList<PricingChart> PCresult = (ArrayList<PricingChart>) event.getMessage().getObject();
        ObservableList<SubscriptionChartModel> tmp = FXCollections.observableArrayList();
        int i=0,hours;
        double rate;
        for (PricingChart price :PCresult){
            if(price.isByHourOrSubscription()){
                rate=price.getRate();
                hours=1;

            }
            else {
                rate = price.getRate() * PCresult.get(price.getRateId()).getRate();
                hours=(int)price.getRate();
            }
            tmp.add(new SubscriptionChartModel(
                    ++i, price.getName(), rate
                    ,hours, rate));
        }
        System.out.println(PCresult.get(1));





        subIDcolumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        subTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        subPriceColumn.setCellValueFactory(new PropertyValueFactory<>("HourlyPrice"));
        hoursInMonthColumn.setCellValueFactory(new PropertyValueFactory<>("HoursInMonth"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Total"));
        subTable.setItems(tmp);
    }
}
