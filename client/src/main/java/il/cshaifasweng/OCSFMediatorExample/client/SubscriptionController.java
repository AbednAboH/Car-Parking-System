package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionsChartResults;
import il.cshaifasweng.OCSFMediatorExample.client.models.SubscriptionChartModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.FullSubscription;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscriptionController {
    @FXML
    private TextField cusomerID;

    @FXML
    private ChoiceBox<Integer> ParkingLotList;

    @FXML
    private TextField emailInput;

    @FXML
    private Spinner<Integer> exitTime;

    @FXML
    private TableView<SubscriptionChartModel> pricingChart;

    @FXML
    private TableColumn<PricingChart, String> nameCol;

    @FXML
    private TableColumn<PricingChart, Double> byHourCol;

    @FXML
    private TableColumn<PricingChart, Integer> rateCol;

    @FXML
    private TableColumn<PricingChart, Double> totalPriceCol;

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
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;


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
        if (SimpleChatClient.getUser()!=null){
            Customer customer = (Customer) SimpleChatClient.getUser();
            cusomerID.setText(customer.getId()+"");
            emailInput.setText(customer.getEmail());
        }
        plateNum.setDisable(false);
        plateNumSec.setDisable(true);
        plateNumTwo.setDisable(true);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        byHourCol.setCellValueFactory(new PropertyValueFactory<>("byHourOrSubscription"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        subType.setItems(subBoxData);
        exitTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        // Enable disable the according to the selected subscription type.
        subType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals(Constants.FULL_SUBSCRIPTION.getMessage())) {
                disableTime(false);
                ParkingLotList.setDisable(true);
                plateNumSec.setDisable(true);
                plateNumTwo.setDisable(true);
                updatePrice(pricingChart.getItems().filtered(sub -> sub.getType().contains("Full")).get(0).getTotal());
            } else {
                disableTime(true);
                ParkingLotList.setDisable(false);
                if(newValue.equals(Constants.REGULAR_MULTI_SUBSCRIPITON.getMessage())){
                    plateNumTwo.setDisable(false);
                    plateNumSec.setDisable(false);
                    updatePrice(pricingChart.getItems().filtered(sub -> sub.getType().contains("Two")).get(0).getTotal());
                }else{
                    plateNumSec.setDisable(true);
                    plateNumTwo.setDisable(true);
                    updatePrice(pricingChart.getItems().filtered(sub -> sub.getType().contains("Regular Subscription")).get(0).getTotal());
                }
            }
        });

    }




    @Subscribe
    public void SubscriptionEvents(ParkingLotResults event){
        if(event.getMessage() != null) {
            if (event.getMessage().getMessage().startsWith("#getPricingChart")){
                System.out.println("Hello Parkings");
                updatePricesTable((PricingChart) event.getMessage().getObject());
            }
            if(event.getMessage().getMessage().startsWith("#getAllParkingLots")){
                parkingLots = (ArrayList<ParkingLot>) event.getMessage().getObject();
                addParkingLotsNames();

            }
        }
    }


    @Subscribe
    public void SubscriptionEvents(SubscriptionsChartResults event){
        if(event.getMessage() != null) {
            if (event.getMessage().getMessage().startsWith("#getPricingChart")) {
                updatePricesTable((PricingChart) event.getMessage().getObject());
            }
        }
    }


    @FXML
    void goToPayment(ActionEvent event) throws IOException {

        if (!subType.getValue().equals(Constants.FULL_SUBSCRIPTION.getMessage())) {
            currParkingLot = pLotsMap.get(ParkingLotList.getValue());
        }
        if (validateInfo()) {
            Customer customer = (RegisteredCustomer) SimpleChatClient.getUser();
            Subscription subscription ;
            List<String> cars = new ArrayList<>();
            cars.add(plateNum.getText());
            if (customer==null){
                SimpleChatClient.setRegisteredCustomerDetails( new RegisteredCustomer(Integer.parseInt(cusomerID.getText()),emailInput.getText(),firstName.getText(),lastName.getText()));
                customer= SimpleChatClient.getRegisteredCustomerDetails();
            }
            if (!subType.getValue().equals(Constants.FULL_SUBSCRIPTION.getMessage())) {
                if (subType.getValue().equals(Constants.REGULAR_MULTI_SUBSCRIPITON.getMessage()))
                    cars.add(plateNumTwo.getText());

                subscription = new RegularSubscription(customer, 100, LocalDate.now(),
                        LocalDate.now().plusMonths(1), parkingLots.get(parkingLots.indexOf(currParkingLot)), LocalTime.of(exitTime.getValue(), 0), cars);


            }
            else {
                subscription = new FullSubscription(customer, 100, LocalDate.now(),
                        LocalDate.now().plusMonths(1),cars,16);

            }
            subscription.setEmail(emailInput.getText());

            SimpleChatClient.setCurrentSubscription(subscription);
            SimpleChatClient.setUserID(Integer.parseInt(this.cusomerID.getText()));
            SimpleChatClient.addScreen("SubscriptionScreen");
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot("orderPaymentGUI");
        }
        else {
                checkInputs();
            }

    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
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

    private void updatePrice(double full) {
        totalPrice.setVisible(true);
        totalPrice.setText(String.valueOf(full));
    }

    private void disableTime(boolean flag) {
        expectedTime.setVisible(flag);
        minFormat.setVisible(flag);
        exitTime.setVisible(flag);
        timeFormat.setVisible(flag);
    }

    private void addParkingLotsNames() {
        for(ParkingLot plot : parkingLots) {
            parkingLotsIds.add(plot.getId());
            pLotsMap.put(plot.getId(),plot);
        }
        ParkingLotList.setItems(parkingLotsIds);
    }


    private Callback<TableColumn<PricingChart, Double>, TableCell<PricingChart, Double>> createDoubleCellFactory(int decimalPlaces) {
        return tc -> new TableCell<PricingChart, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    int intValue = value.intValue();
                    if (value == intValue) {
                        setText(Integer.toString(intValue));
                    } else {
                        setText(String.format("%." + decimalPlaces + "f", value));
                    }
                }
            }
        };
    }
    private boolean validateInfo() {
        String email = emailInput.getText();
        boolean secCar = true;
        if(subType.getValue().equals(Constants.REGULAR_MULTI_SUBSCRIPITON.getMessage()))
            secCar = InputValidator.isValidPlateNumber(plateNumTwo.getText());
        return InputValidator.isValidEmail(email) && InputValidator.isValidPlateNumber(plateNum.getText()) && secCar;
    }

    public void updatePricesTable(PricingChart PCresult) {
        ObservableList<SubscriptionChartModel> tmp = FXCollections.observableArrayList();
        System.out.println(PCresult.getOrderBeforeHandPrice() + "This is the price for one hour");
        System.out.println(PCresult.getKioskPrice() + "This is the price for one hour in kiosk");
        double rate;
        double[] prices={PCresult.getRegularSubHours()
                ,PCresult.getMultipleCarRegularSubHours(),PCresult.getFullSubHours()};
        String[] names={"Regular Subscription","Regular Two Cars","Full Subscription"};
        int[] ids={1,1,1};
        for(int i=0;i<3;i++){
            if(names[i].contains("Two"))
                tmp.add(new SubscriptionChartModel(i+1,names[i], PCresult.getOrderBeforeHandPrice(),(int)Double.parseDouble(String.valueOf(prices[i])), prices[i]* PCresult.getOrderBeforeHandPrice() * 2));
            else
                tmp.add(new SubscriptionChartModel(i+1,names[i], PCresult.getOrderBeforeHandPrice(),(int)Double.parseDouble(String.valueOf(prices[i])), prices[i]* PCresult.getOrderBeforeHandPrice()));
        }
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        byHourCol.setCellValueFactory(new PropertyValueFactory<>("HourlyPrice"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("HoursInMonth"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        byHourCol.setCellFactory(createDoubleCellFactory(2));
        totalPriceCol.setCellFactory(createDoubleCellFactory(2));

        pricingChart.setItems(tmp);
    }

    public void checkInputs() {
        String validplateNumTwo="";

        String validEmail=InputValidator.isValidEmail(emailInput.getText())?"-fx-border-color: green;":"-fx-border-color: red;";
        String validplateNum=InputValidator.isValidPlateNumber(plateNum.getText())?"-fx-border-color: green;":"-fx-border-color: red;";
        String validateID=InputValidator.isValidNumber(cusomerID.getText())?"-fx-border-color: green;":"-fx-border-color: red;";
        String validateName=InputValidator.isValidName(firstName.getText())?"-fx-border-color: green;":"-fx-border-color: red;";
        String validateLast=InputValidator.isValidName(lastName.getText())?"-fx-border-color: green;":"-fx-border-color: red;";

        if(!plateNumTwo.isDisable()){
            validplateNumTwo=InputValidator.isValidPlateNumber(plateNumTwo.getText())?"-fx-border-color: green;":"-fx-border-color: red;";
            plateNumTwo.setStyle(validplateNumTwo);
        }
        emailInput.setStyle(validEmail);
        plateNum.setStyle(validplateNum);
        cusomerID.setStyle(validateID);
        firstName.setStyle(validateName);
        lastName.setStyle(validateLast);
        // Show an error message
        Label errorLabel = new Label("There is something wrong with your inputs. Please correct the red fields.");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setLayoutX(35);
        errorLabel.setLayoutY(460);
        ((AnchorPane) plateNum.getParent()).getChildren().add(errorLabel);

    }



}
