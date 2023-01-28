package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CustomerCarsSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RegisteredCutomerSubscriber;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.customerCatalogEntities.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegisteredCustomerController {
    RegisteredCustomer rg = new RegisteredCustomer();

    private ObservableList<Order> observableOrders;
    private ObservableList<Subscription> observableSubs;
    private ObservableList<Car> carList;
    private List<Subscription> subs;
    @FXML
    private TableView<Car> KioskParchases;

    @FXML
    private Button addsubscriptionbutton1;

    @FXML
    private Button cancelorderbutton;

    @FXML
    private Button cancelsubsbutton1;

    @FXML
    private TableView<Car> car;

    @FXML
    private TableColumn<Car, String> cars;

    @FXML
    private Button complaintbutton;

    @FXML
    private Button editbutton;

    @FXML
    private TextField emailtxt;

    @FXML
    private TextField fnametxt;

    @FXML
    private TextField idtxt;

    @FXML
    private TableColumn<?, ?> kioskId;

    @FXML
    private TextField lnametxt;

    @FXML
    private Button logoutbutton;
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Boolean> orderActive;
    @FXML
    private TableColumn<Order, Integer> orderID;

    @FXML
    private TableColumn<Order, String> orderEmail;

    @FXML
    private TableColumn<Order, String> orderEntry;

    @FXML
    private TableColumn<Order, String> orderExit;

    @FXML
    private TableColumn<Order, String> orderLicense;

    @FXML
    private TableColumn<Order, String> orderPLotID;

    @FXML
    private TableColumn<Order, String> orderParchaseDate;

    @FXML
    private TableColumn<Order, String> orderPricePaid;

    @FXML
    private TextField passwordtxt;

    @FXML
    private Button placebutton;

    @FXML
    private Button savebutton;

    @FXML
    private TableView<Subscription> subsTable;

    @FXML
    private TableColumn<Subscription, String> subCars;

    @FXML
    private TableColumn<Subscription, String> subDaysToPark;

    @FXML
    private TableColumn<Subscription, LocalDate> subExpiration;

    @FXML
    private TableColumn<Subscription, Integer> subID;

    @FXML
    private TableColumn<Subscription, String> subPLot;

    @FXML
    private TableColumn<Subscription, LocalDate> subParchaseDate;

    @FXML
    private TableColumn<Subscription, String> subTypeTable;

    @FXML
    private TableColumn<Subscription, Integer> subHoursToPark;

    @FXML
    private ChoiceBox<Integer> subType;

    @FXML
    void saveInfo(ActionEvent event) throws IOException {
        savebutton.setVisible(false);
        editbutton.setVisible(true);
        idtxt.setEditable(false);
        lnametxt.setEditable(false);
        fnametxt.setEditable(false);
        emailtxt.setEditable(false);
        passwordtxt.setEditable(false);
        passwordtxt.setVisible(false);
        Message message = new Message("#updateUser");
        SimpleClient.getClient().sendToServer(message);
    }

    @FXML
    void editInfo(ActionEvent event) {
        savebutton.setVisible(true);
        passwordtxt.setVisible(true);
        editbutton.setVisible(false);
        idtxt.setEditable(true);
        lnametxt.setEditable(true);
        fnametxt.setEditable(true);
        emailtxt.setEditable(true);
        passwordtxt.setEditable(true);
    }


    @FXML
    void addComplaint(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot("complaint");
    }


    @FXML
    void placeOrder(ActionEvent event) throws IOException {
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot("orderGUI");
    }

    @FXML
    void logOut(ActionEvent event) throws Exception {
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot("logInScreen");
    }



    @FXML
    void cancelOrder(ActionEvent event) throws IOException {
        // TODO: 23/01/2023
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setCurrentOrder(ordersTable.getSelectionModel().getSelectedItem());
        System.out.println(SimpleChatClient.getCurrentOrder());
        SimpleChatClient.setRoot("CancelOrder");



    }

    @FXML
    void cancelSubscription(ActionEvent event) {
        // TODO: 23/01/2023

        try {
            Message message = new Message("#cancelSubscription");
            SimpleClient.getClient().sendToServer(message);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @FXML
    void AddSubscriptions(ActionEvent event) {
        // TODO: 23/01/2023 place subscription screen
    }


    @Subscribe
    public void showOrdersFromServer(OrderHistoryResponse event) {
        System.out.println("got here");

        observableOrders = FXCollections.observableArrayList((List<Order>) event.getMessage().getObject());
//        System.out.println(observableOrders.get(0));
        SetOrdersTable();

    }

    private void SetOrdersTable() {
        if (ordersTable != null && ordersTable.getItems() != null) {
            ordersTable.getItems().clear();
        }
        orderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        orderActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        orderEntry.setCellValueFactory(new PropertyValueFactory<>("entering"));
        orderExit.setCellValueFactory(new PropertyValueFactory<>("exiting"));
        orderLicense.setCellValueFactory(new PropertyValueFactory<>("plateNum"));
        orderParchaseDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderPLotID.setCellValueFactory(data -> new SimpleObjectProperty<>(Integer.toString(data.getValue().getParkingLotID().getId())));
        observableOrders.forEach(ordersTable.getItems()::add);
    }

    @Subscribe
    public void setCarsFromServer(CustomerCarsSubscriber event){
        carList=FXCollections.observableArrayList((List<Car>) event.getMessage().getObject());
        setCars();

    }

    private void setCars() {
        if (car != null && car.getItems() != null) {
            car.getItems().clear();
        }
        cars.setCellValueFactory(new PropertyValueFactory<>("carNum"));
        carList.forEach(car.getItems()::add);
    }

    @Subscribe
    public void showSubscriptionsFromServer(SubscriptionResponse event) {
        subs= (List<Subscription>) event.getMessage().getObject();
        setSubscriptionsTable();
    }

    private void setSubscriptionsTable() {
        observableSubs = FXCollections.observableArrayList(subs);
        if (subsTable != null && subsTable.getItems() != null) {
            subsTable.getItems().clear();
        }
        subID.setCellValueFactory(new PropertyValueFactory<>("id"));
        subTypeTable.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getClass().getSimpleName()));
        subDaysToPark.setCellValueFactory(new PropertyValueFactory<>("allowedDays"));
        subParchaseDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        subExpiration.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        subCars.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getCarsAsString()) );
        subHoursToPark.setCellValueFactory(new PropertyValueFactory<>("hoursPerMonth"));
        subPLot.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getParkingLotIdAsString()));
        observableSubs.forEach(subsTable.getItems()::add);
    }

    @Subscribe
    public void addSubscriptionResponse(UpdateMessageEvent event) {
        // TODO: 23/01/2023 go to place new subscription
    }

    @Subscribe
    public void cancelOrderResponse(UpdateMessageEvent event) {
        // TODO: go to cancel order with order id
    }

    @Subscribe
    public void cancelSubscriptionResponse(UpdateMessageEvent event) {
        // TODO: 23/01/2023 go to cancel Subscription
    }



    @Subscribe
    public void setRegisteredCustomerDataFromServer(RegisteredCutomerSubscriber event) {
        RegisteredCustomer result = (RegisteredCustomer) event.getMessage().getObject();
        System.out.println(result.getFirstName());
        fnametxt.setText(result.getFirstName());
        lnametxt.setText(result.getLastName());
        idtxt.setText(result.getId() + "");
        emailtxt.setText(result.getEmail());
        passwordtxt.setVisible(false);


    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        Message message = new Message("#getUser");
        SimpleClient.getClient().sendToServer(message);
        message = new Message("#getAllOrders");
        SimpleClient.getClient().sendToServer(message);
        message = new Message("#showSubscription");
        SimpleClient.getClient().sendToServer(message);
        message = new Message("#GetCustomerCars");
        SimpleClient.getClient().sendToServer(message);
    }

}