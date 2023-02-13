package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RegisteredCutomerSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionsChartResults;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    RegisteredCustomer rg = new RegisteredCustomer();
    List<ParkingLot> PLresults = new ArrayList<>();

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
    private TextField emailTxt;

    @FXML
    private ChoiceBox<Integer> monthInput;

    @FXML
    private Label warningMsg;

    @FXML
    private TextField numberInput;

    @FXML
    private TextField parkingHoursTxt;

    @FXML
    private TextField plateNumTxt;

    @FXML
    private ChoiceBox<Integer> yearInput;

    @FXML
    private Spinner<Integer> arrivalTime;

    @FXML
    private DatePicker dateChoice;

    @FXML
    private TextField emailInput;

    @FXML
    private Spinner<Integer> exitTime;

    @FXML
    private ChoiceBox<Integer> plChoice;

    @FXML
    private TextField plateNum;

    @FXML
    private Button submit;
    @FXML
    private Label pricePerHour;
    @FXML
    private Label totalPrice;

    private double perHourPrice;
    private boolean orderInfoValidation() {
        if (exitTime.getValue() < arrivalTime.getValue())
            return false;
        if (plateNum.getText().length() < 6)
            return false;
        return true;
    }
    @FXML
    void backToRegisteredCustomer(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("registeredCustomer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToPayment(ActionEvent event) throws Exception {
        try {
            if (orderInfoValidation()) {
                int idx = plChoice.getSelectionModel().getSelectedItem();
                String start = arrivalTime.getValue() + "";
                String end = exitTime.getValue() + "";
                // TODO: 1/17/2023 get might get us in trouble , indexes of parking lots aren't linear and don't always start with 1  
                ParkingLot pl = PLresults.get((int)idx - 1);
                Order newOrder = new Order(rg, pl, dateChoice.getValue(), start, end,
                        plateNum.getText(), emailInput.getText());
                System.out.println(dateChoice.getValue());
                newOrder.setValue((exitTime.getValue()-arrivalTime.getValue())*perHourPrice);
                System.out.println(newOrder.getCar());
                SimpleChatClient.setCurrentOrder(newOrder);
                SimpleChatClient.setRoot("orderPaymentGUI");
            } else {
                warningMsg.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        PLresults = (List<ParkingLot>) event.getMessage().getObject();
        ObservableList<ParkingLotModel> tmp = FXCollections.observableArrayList();
        for (ParkingLot PL :
                PLresults) {
            plChoice.getItems().add(PL.getId());
        }
    }

    @Subscribe
    public void setRegisteredCustomerDataFromServer(RegisteredCutomerSubscriber event) {
        RegisteredCustomer result = (RegisteredCustomer) event.getMessage().getObject();
        rg.setId(result.getId());
        rg.setSubscriptions(rg.getSubscriptions());
        rg.setComplaint(result.getComplaint());
        rg.setEmail(result.getEmail());
        rg.setFirstName(result.getFirstName());
        rg.setLastName(result.getLastName());
        rg.setPassword(result.getPassword());
        rg.setCars(result.getCars());

        emailInput.setText(result.getEmail());
    }


    @FXML
    void plateNumTxtChange(ActionEvent event) {
        String txt = plateNum.getText();
        if (!txt.matches("-?([1-9][0-9]*)?")
                || txt.length() > 10){
            plateNum.setText(txt.substring(0, txt.length() - 1));
        }
    }

    @FXML
    void initialize() {
        try {
            EventBus.getDefault().register(this);
            Order myOrder = SimpleChatClient.getCurrentOrder();
            initInfoView();
            if (myOrder != null)
                fillOrderDetails(myOrder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillOrderDetails(Order order) {
        emailInput.setText(order.getEmail());
        plateNum.setText(order.getCar().toString());
        dateChoice.setValue(order.getDate());
        exitTime.getValueFactory().setValue(Integer.parseInt(order.getExiting()));
        arrivalTime.getValueFactory().setValue(Integer.parseInt(order.getEntering()));
        plChoice.setValue(order.getParkingLotID().getId());
    }

    private void initInfoView() throws IOException {
        Message message = new Message("#getAllParkingLots");
        SimpleClient.getClient().sendToServer(message);

        //RegisteredCustomer user=(RegisteredCustomer) SimpleChatClient.getUser();
        message.setMessage("#getUser");
        SimpleClient.getClient().sendToServer(message);

        message.setMessage("#getPricingChart");
        SimpleClient.getClient().sendToServer(message);

        initInfoControls();
    }

    @Subscribe
    public void setSubChartDataFromServer(SubscriptionsChartResults event) {
        PricingChart PCresult = (PricingChart) event.getMessage().getObject();
        perHourPrice=PCresult.getOrderBeforeHandPrice();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pricePerHour.setText("Price Per Hour is: "+perHourPrice+" NIS");

            }
        });
    }

    private void initInfoControls() {
        LocalDate minDate = LocalDate.now();
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }

        };

        dateChoice.setDayCellFactory(dayCellFactory);
        arrivalTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        exitTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        arrivalTime.getValueFactory().setValue(8);
        exitTime.getValueFactory().setValue(9);
        arrivalTime.valueProperty().addListener(((obs, oldValue, newValue) ->
        {
            if (newValue == 23)
                exitTime.getValueFactory().setValue(0);
            else
                exitTime.getValueFactory().setValue(newValue + 1);
            totalPrice.setText("Total Price is: "+(exitTime.getValue()-arrivalTime.getValue())*perHourPrice+" NIS");
        }));
        exitTime.valueProperty().addListener(((obs, oldValue, newValue) ->
        {
            if(arrivalTime.getValue()>=newValue)
                arrivalTime.getValueFactory().setValue(newValue - 1);
            // TODO: 24/01/2023 check 24 hour format
            totalPrice.setText("Total Price is: "+(exitTime.getValue()-arrivalTime.getValue())*perHourPrice+" NIS");
        }));
    }

}



