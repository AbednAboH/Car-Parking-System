package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscriptionController {

    @FXML
    private ChoiceBox<Integer> ParkingLotList;

    @FXML
    private TextField emailInput;

    @FXML
    private Spinner<Integer> exitTime;

    @FXML
    private TableView<PricingChart> pricingChart;

    @FXML
    private TableColumn<PricingChart, String> nameCol;

    @FXML
    private TableColumn<PricingChart, Boolean> byHourCol;

    @FXML
    private TableColumn<PricingChart, Integer> rateCol;

    @FXML
    private TextField plateNum;

    @FXML
    private TextField plateNumTwo;

    @FXML
    private ChoiceBox<String> subType;

    @FXML
    private Button submit;

    @FXML
    private Label totalPrice;

    @FXML
    private Label plateNumSec;

    @FXML
    private Label expectedTime;

    @FXML
    private Label minFormat;

    @FXML
    private Label timeFormat;

    @FXML
    private Label rePrice;

    @FXML
    private Label multiPrice;

    @FXML
    private Label fullPrice;


    private ObservableList<PricingChart> pricingChartsList = FXCollections.observableArrayList();
    private ObservableList<Integer> parkingLotsIds = FXCollections.observableArrayList();
    private ObservableList<String> subBoxData = FXCollections.observableArrayList(Constants.REGULAR_SUBSCRIPTION.getMessage(),Constants.REGULAR_MULTI_SUBSCRIPITON.getMessage(),Constants.FULL_SUBSCRIPTION.getMessage());
    private List<ParkingLot> parkingLots;
    private ParkingLot currParkingLot;
    private HashMap<Integer,ParkingLot> pLotsMap = new HashMap<>();

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        sendMessagesToServer("#getPricingChart"); // returns the pricingChart data.
        sendMessagesToServer("#getAllParkingLots"); // returns the id's for the parking lots, or can be defined for names later.
        plateNumSec.setDisable(true);
        plateNumTwo.setDisable(true);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        byHourCol.setCellValueFactory(new PropertyValueFactory<>("byHourOrSubscription"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        subType.setItems(subBoxData);
        exitTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        // Enable disable the according to the selected subscription type.
        subType.valueProperty().addListener((observable, oldValue, newValue) -> {
            updatePrice();
            if (newValue.equals(Constants.FULL_SUBSCRIPTION.getMessage())) {
                disableTime(false);
                ParkingLotList.setDisable(true);
                plateNumSec.setDisable(true);
                plateNumTwo.setDisable(true);
            } else {
                disableTime(true);
                ParkingLotList.setDisable(false);
                if(newValue.equals(Constants.REGULAR_MULTI_SUBSCRIPITON.getMessage())){
                    plateNumTwo.setDisable(false);
                    plateNumSec.setDisable(false);
                }else{
                    plateNumSec.setDisable(true);
                    plateNumTwo.setDisable(true);
                }
            }
        });

    }

    private void updatePrice() {
        totalPrice.setVisible(true);
        totalPrice.setText("200");
    }

    private void disableTime(boolean flag) {
        expectedTime.setVisible(flag);
        minFormat.setVisible(flag);
        exitTime.setVisible(flag);
        timeFormat.setVisible(flag);
    }


    @Subscribe
    public void SubscriptionEvents(ParkingLotResults event){
        if(event.getMessage() != null) {
            if (event.getMessage().getMessage().startsWith("#getPricingChart")){
                pricingChartsList.addAll((ArrayList<PricingChart>) event.getMessage().getObject());
                pricingChart.setItems(pricingChartsList);
            }
            System.out.println("Hello Parkings");
            if(event.getMessage().getMessage().startsWith("#getAllParkingLots")){
                parkingLots = (ArrayList<ParkingLot>) event.getMessage().getObject();
                addParkingLotsNames();

            }
        }
    }

    private void addParkingLotsNames() {
        for(ParkingLot plot : parkingLots) {
            parkingLotsIds.add(plot.getId());
            pLotsMap.put(plot.getId(),plot);
        }
        ParkingLotList.setItems(parkingLotsIds);
    }

    @Subscribe
    public void SubscriptionEvents(SubscriptionsChartResults event){
        if(event.getMessage() != null) {
            if (event.getMessage().getMessage().startsWith("#getPricingChart")) {
                pricingChartsList.addAll((ArrayList<PricingChart>) event.getMessage().getObject());
                pricingChart.setItems(pricingChartsList);
            }
        }
    }




    @FXML
    void goToPayment(ActionEvent event) throws IOException {

        if(!subType.getValue().equals(Constants.FULL_SUBSCRIPTION.getMessage())){
            currParkingLot = pLotsMap.get(ParkingLotList.getValue());
        }
        if(validateInfo()) {
            SimpleChatClient.setRoot("orderPaymentGUI");
        }else{

        }
    }

    private boolean validateInfo() {
        String email = emailInput.getText();
        // TODO: 17/01/2023 Abo Abied car plate validator.
        return InputValidator.isValidEmail(email);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("registeredCustomer");
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
