package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RegisteredCutomerSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
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
    private RegisteredCustomer rg = new RegisteredCustomer();
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

    private boolean orderInfoValidation() {

        return true;
    }

    @FXML
    void saveOrder(ActionEvent event) {
        try {
            if (orderInfoValidation()) {
                int idx = plChoice.getSelectionModel().getSelectedItem();
                Order newOrder = new Order(rg, PLresults.get(idx), dateChoice.getValue(),
                        arrivalTime.getValue().toString(), exitTime.getValue().toString()
                ,plateNum.getText(), emailInput.getText());
                Message message = new Message("#placeOrder", newOrder);
                SimpleClient.getClient().sendToServer(message);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToPayment(ActionEvent event) throws Exception {
       // SimpleChatClient.setRoot("orderPaymentGUI");
        try {
            if (orderInfoValidation()) {
                int idx = plChoice.getSelectionModel().getSelectedItem();
                Order newOrder = new Order(rg, PLresults.get(idx), dateChoice.getValue(),
                        arrivalTime.getValue().toString(), exitTime.getValue().toString()
                        ,plateNum.getText(), emailInput.getText());
                Message message = new Message("#placeOrder", newOrder);
                SimpleClient.getClient().sendToServer(message);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backToOrder(ActionEvent event) throws IOException {

    }

    @Subscribe
    public void placeOrderResponse(UpdateMessageEvent event) {
        emailInput.setText("done");
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
    }

    @FXML
    void initialize() {
        try {
            EventBus.getDefault().register(this);
            initInfoView();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initInfoView() throws IOException {
        Message message = new Message("#getAllParkingLots");
        SimpleClient.getClient().sendToServer(message);

        message.setMessage("#getRegisteredCustomer");
        //TODO: customerID
        message.setObject(1);
        SimpleClient.getClient().sendToServer(message);

        emailInput.setText(rg.getEmail());

        initInfoControls();
    }

    private void initPaymentView(){}

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
        }));

    }

    private void initPaymentControls(){
        ArrayList<Integer> months = new ArrayList<>();
        ArrayList<Integer> years = new ArrayList<>();
        int curYear = LocalDate.now().getYear();
        for(int i = 1; i < 13; i++){
            months.add(i);
            years.add(curYear + i);
        }
        monthInput.getItems().setAll(months);
        yearInput.getItems().setAll(years);
    }
}



