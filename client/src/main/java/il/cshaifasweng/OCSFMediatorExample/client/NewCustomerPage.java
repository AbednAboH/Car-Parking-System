package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.*;
import il.cshaifasweng.ParkingLotEntities.Car;
import il.cshaifasweng.ParkingLotEntities.EntryAndExitLog;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static com.sun.javafx.application.PlatformImpl.runLater;

import java.io.IOException;
import java.sql.Ref;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class NewCustomerPage {
    private ObservableList<OnlineOrder> observableOnlineOrders;
    private ObservableList<Subscription> observableSubs;
    private ObservableList<Transactions> observableTransactions;
    private ObservableList<Refund> observableRefunds;
    private ObservableList<OfflineOrder> observableOfflineOrders;
    private ObservableList<EntryAndExitLog> observableLogs;
    private ObservableList<OnlineOrder> observableToBeConfirmed;
    
    private ObservableList<Car> carList;
    private List<Subscription> subs;
    @FXML
    private ComboBox<?> ComboOrder;

    @FXML
    private Pane Title;
    @FXML
    private Label titleOfPage;
    @FXML
    private Button addsubscriptionbutton;

    @FXML
    private TableColumn<OnlineOrder, Integer> approvalID;

    @FXML
    private TableColumn<OnlineOrder, String> approvalLicense;

    @FXML
    private TableColumn<OnlineOrder, LocalDate> approvalOrderParDate;

    @FXML
    private TableColumn<OnlineOrder, Integer> approvalParkingLot;

    @FXML
    private GridPane approvalsGrid;

    @FXML
    private Button approvalsTab;

    @FXML
    private Button approve;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> approvedEntry;

    @FXML
    private Button buttonApproval;

    @FXML
    private Button buttonLog;

    @FXML
    private Button buttonOffline;

    @FXML
    private Button buttonRefund;

    @FXML
    private Button buttonSubsGrid;

    @FXML
    private Button cancelorderbutton;

    @FXML
    private Button cancelsubsbutton;

    @FXML
    private ComboBox<?> comboApproval;

    @FXML
    private ComboBox<?> comboLog;

    @FXML
    private ComboBox<?> comboOffline;

    @FXML
    private ComboBox<?> comboRefund;

    @FXML
    private ComboBox<?> comboSubsGrid;

    @FXML
    private ComboBox<?> comboTransaction;

    @FXML
    private Button complaintsTab;

    @FXML
    private Button deny;

    @FXML
    private TableColumn<EntryAndExitLog, LocalDateTime> enteredAt;

    @FXML
    private TableColumn<EntryAndExitLog, String> exitedAt;

    @FXML
    private GridPane logGrid;

    @FXML
    private TableColumn<EntryAndExitLog, Integer> logID;

    @FXML
    private TableColumn<EntryAndExitLog, Integer> logParkingLot;

    @FXML
    private TableColumn<EntryAndExitLog, String> logType;

    @FXML
    private TableColumn<EntryAndExitLog, String> loggedLicense;

    @FXML
    private Button logsTab;

    @FXML
    private TableView<EntryAndExitLog> logsTable;

    @FXML
    private TableView<OnlineOrder> needApprovalTable;

    @FXML
    private TableColumn<OfflineOrder, String> offlineEmail;

    @FXML
    private TableColumn<OfflineOrder, LocalDateTime> offlineExit;
     @FXML
    private TableColumn<OfflineOrder, LocalDateTime> offlineEntry;

    @FXML
    private TableColumn<OfflineOrder, String> offlineLisence;

    @FXML
    private TableColumn<OfflineOrder, LocalDateTime> offlineOrderDate;

    @FXML
    private TableColumn<OfflineOrder, Integer> offlineOrderID;

    @FXML
    private GridPane offlineOrdersGrid;

    @FXML
    private Button offlineOrdersTab;

    @FXML
    private TableView<OfflineOrder> offlineOrdersTable;

    @FXML
    private TableColumn<OfflineOrder, Integer> offlineParkingLot;

    @FXML
    private TableColumn<OfflineOrder, Double> offlinePricePaid;

    @FXML
    private Button onlineOrdersTab;

    @FXML
    private TableColumn<OnlineOrder, Boolean> orderActive;


    @FXML
    private Button orderButton;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderID;

    @FXML
    private TableColumn<OnlineOrder, String> orderEmail;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderEntry;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderExit;

    @FXML
    private TableColumn<OnlineOrder, String> orderLicense;

    @FXML
    private TableColumn<OnlineOrder, String> orderPLotID;

    @FXML
    private TableColumn<OnlineOrder, String> orderParchaseDate;

    @FXML
    private TableColumn<OnlineOrder, String> orderPricePaid;

    @FXML
    private GridPane orderGrid;


    @FXML
    private TableView<OnlineOrder> ordersTable;

    @FXML
    private TableColumn<EntryAndExitLog, Double> overStayedHours;

    @FXML
    private Button placebutton;

    @FXML
    private TableColumn<Refund, LocalDate> refundDate;

    @FXML
    private GridPane refundGrid;

    @FXML
    private TableColumn<Refund, Integer> refundID;

    @FXML
    private TableView<Refund> refundTable;

    @FXML
    private TableColumn<Refund, String> refundType;

    @FXML
    private TableColumn<Refund,Double> refundValue;

    @FXML
    private Button refundsTab;

    @FXML
    private TextField searchBarApproval;

    @FXML
    private TextField searchBarLog;

    @FXML
    private TextField searchBarOrder;

    @FXML
    private TextField searchBarRefund;

    @FXML
    private TextField searchBarSubsGrid;

    @FXML
    private TextField searchBarTransaction;

    @FXML
    private TextField searchTabOffline;

    @FXML
    private StackPane stackedpane;
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
    private GridPane subsGrid;

    @FXML
    private Button subscriptionsTab;

    @FXML
    private Button transactionButton;

    @FXML
    private TableColumn<Transactions, LocalDate> transactionDate;

    @FXML
    private TableColumn<Transactions,String> transactionMethod;

    @FXML
    private GridPane transactionGrid;

    @FXML
    private TableColumn<Transactions,Integer> transactionID;

    @FXML
    private TableColumn<Transactions,String> transactionType;

    @FXML
    private TableColumn<Transactions, Double> transactionValue;

    @FXML
    private TableView<Transactions> transactions;

    @FXML
    private Button transactionsTab;
    @FXML
    private Button LogOut;

    @FXML
    private TableColumn<EntryAndExitLog, Integer> logsTransactiontypeID;
    Notifications notificationBuilder;
    @FXML
    void AddSubscriptions(ActionEvent event) {
        try {
            SimpleChatClient.addScreen(((RegisteredCustomer)SimpleChatClient.getUser()).getGUI());
            SimpleChatClient.setRoot("SubscriptionScreen");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void cancelOrder(ActionEvent event) {

        notificationBuilder = Notifications.create()
                .title("Error")
                .text("Please select a row")
                .hideAfter(Duration.seconds(3))
                .position(Pos.CENTER);
        if (ordersTable.getSelectionModel().getSelectedItem()!=null){
            SimpleChatClient.setCurrentOrder(ordersTable.getSelectionModel().getSelectedItem());


        try {
            SimpleChatClient.addScreen(((RegisteredCustomer)SimpleChatClient.getUser()).getGUI());

            SimpleChatClient.setRoot("CancelOrder");
            EventBus.getDefault().unregister(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        else{
            notificationBuilder.showWarning();
        }
    }

    @FXML
    void cancelSubscription(ActionEvent event) {
        // TODO: 21/02/2023 cancel subscription
        try {
            SimpleChatClient.addScreen(((RegisteredCustomer)SimpleChatClient.getUser()).getGUI());
            Message message = new Message("#cancelSubscription");
            SimpleClient.getClient().sendToServer(message);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void placeOrder(ActionEvent event) {
        EventBus.getDefault().unregister(this);
        try {
            SimpleChatClient.addScreen(((RegisteredCustomer)SimpleChatClient.getUser()).getGUI());
            SimpleChatClient.setRoot("orderGUI");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        try {
            disableAll();
            EventBus.getDefault().register(this);
            Message message = new Message("#getUser");
            SimpleClient.getClient().sendToServer(message);
            message = new Message("#getAllOrders");
            SimpleClient.getClient().sendToServer(message);
            message = new Message("#showSubscription");
            SimpleClient.getClient().sendToServer(message);
            // TODO: 21/02/2023 handleThese
            message=new Message("#getRefunds");
            SimpleClient.getClient().sendToServer(message);
            message=new Message("#getTransactions");
            SimpleClient.getClient().sendToServer(message);
            message=new Message("#getEntryAndExitLogs");
            SimpleClient.getClient().sendToServer(message);
            message=new Message("#getOfflineOrders");
            SimpleClient.getClient().sendToServer(message);
            message=new Message("#getToBeConfirmed");
            SimpleClient.getClient().sendToServer(message);
            
//            message = new Message("#GetCustomerCars");
//            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void showOrdersFromServer(OrderHistoryResponse event) {
        System.out.println("showOrdersFromServer");
        observableOnlineOrders = FXCollections.observableArrayList((List<OnlineOrder>) event.getMessage().getObject());
        SetOrdersTable();
        System.out.println(observableOnlineOrders);

    }
    private void SetOrdersTable() {
        if (ordersTable != null && ordersTable.getItems() != null) {
            ordersTable.getItems().clear();
        }
        orderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        orderActive.setCellValueFactory(new PropertyValueFactory<>("active"));

        orderEntry.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getDateOfOrder()));
        orderExit.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getExiting()));
        orderLicense.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getCar().getCarNum()));
        orderParchaseDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderPLotID.setCellValueFactory(data -> new SimpleObjectProperty<>(Integer.toString(data.getValue().getParkingLotID().getId())));
        orderPricePaid.setCellValueFactory(data -> new SimpleObjectProperty<>(Double.toString(data.getValue().getValue())));
        observableOnlineOrders.forEach(ordersTable.getItems()::add);
    }
    @FXML
    void approveOrDenyArrival(ActionEvent event) {
        Message message=new Message("#confirmCustomerArrival");
        if (event.getSource()==approve){
            if(needApprovalTable.getSelectionModel().getSelectedItem()!=null){
                needApprovalTable.getSelectionModel().getSelectedItem().setAgreedToPayPenalty(true);
                message.setObject(needApprovalTable.getSelectionModel().getSelectedItem());
                try {
                    SimpleClient.getClient().sendToServer(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Please select a row")
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.CENTER);
                notificationBuilder.show();
            }

        }
        else if (event.getSource()==deny){
            if(needApprovalTable.getSelectionModel().getSelectedItem()!=null){
                needApprovalTable.getSelectionModel().getSelectedItem().setAgreedToPayPenalty(false);
                message.setObject(needApprovalTable.getSelectionModel().getSelectedItem());

                try {
                    SimpleClient.getClient().sendToServer(message);
                } catch (IOException e) {
                     e.printStackTrace();
                }
            }
            else{
                notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Please select a row")
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.CENTER);
                notificationBuilder.show();
            }
        }
    }
    @Subscribe
    public void showTransactions(TransactionsSubscirber event) {
        observableTransactions = FXCollections.observableArrayList((List<Transactions>) event.getMessage().getObject());
        setTransactionsTable();
    }

    private void setTransactionsTable() {
        if(transactions!=null&&transactions.getItems()!=null){
            transactions.getItems().clear();
        }
        transactionID.setCellValueFactory(new PropertyValueFactory<>("id"));
        transactionType.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getClass().getSimpleName()));
        transactionValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        transactionDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
        transactionMethod.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getTransaction_method()));
        observableTransactions.forEach(transactions.getItems()::add);
    }

    @Subscribe
    public void showwRefunds(CustomerRefundsSubscriber event) {
        observableRefunds = FXCollections.observableArrayList((List<Refund>) event.getMessage().getObject());
        setRefundsTable();
    }

    private void setRefundsTable() {
        if(refundTable!=null&&refundTable.getItems()!=null){
            refundTable.getItems().clear();
        }
        refundID.setCellValueFactory(new PropertyValueFactory<>("id"));
        refundValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        refundType.setCellValueFactory(data->new SimpleObjectProperty<>(data.getValue().getRefundType()));
        refundDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
        observableRefunds.forEach(refundTable.getItems()::add);
    }

    @Subscribe
    public void showLogs(LogsSubscriber event) {
        observableLogs = FXCollections.observableArrayList((List<EntryAndExitLog>) event.getMessage().getObject());
        setLogsTable();
    }

    private void setLogsTable() {
        if(logsTable!=null&&logsTable.getItems()!=null){
            logsTable.getItems().clear();
        }
        logID.setCellValueFactory(new PropertyValueFactory<>("id"));
        logType.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getOrderSubKioskEntity().getClass().getSimpleName()));
        logsTransactiontypeID.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getOrderSubKioskEntity().getId()));
        logParkingLot.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getParkingLotScheduler().getId()));
        loggedLicense.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getActiveCar()));
        enteredAt.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getAcutallEntryTime()));
        exitedAt.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getAcutallExitTime()!=null?data.getValue().getAcutallExitTime().toString():"Pending"));
        // TODO: 21/02/2023 fix this
//        overStayedHours.setCellValueFactory(data-> new SimpleObjectProperty<>( data.getValue().getAcutallExitTime().minusHours(data.getValue().getAcutallExitTime().getHour())));
        observableLogs.forEach(logsTable.getItems()::add);
    }

    @Subscribe
    public void showOfflineOrders(offlineOrdersSubscriber event) {
        observableOfflineOrders = FXCollections.observableArrayList((List<OfflineOrder>) event.getMessage().getObject());
        setOfflineOrdersTable();
    }

    private void setOfflineOrdersTable() {
        if(offlineOrdersTable!=null&&offlineOrdersTable.getItems()!=null){
            offlineOrdersTable.getItems().clear();
        }
        offlineOrderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        offlineEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
//        offlineEntry.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().));
        offlineExit.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getExiting()));
        offlineLisence.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getCar().getCarNum()));
        offlineOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        offlineParkingLot.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
        offlinePricePaid.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue()));
        observableOfflineOrders.forEach(offlineOrdersTable.getItems()::add);
    }

    @Subscribe
    public void showToBeConfirmed(UnconfirmedArrivalSubscriber event) {
        observableToBeConfirmed = FXCollections.observableArrayList((List<OnlineOrder>) event.getMessage().getObject());
        setToBeConfirmedTable();
    }

    private void setToBeConfirmedTable() {
        if (needApprovalTable != null && needApprovalTable.getItems() != null) {
            needApprovalTable.getItems().clear();
        }
        approvalID.setCellValueFactory(new PropertyValueFactory<>("id"));
        approvalOrderParDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
        approvedEntry.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getDateOfOrder()));
        approvalLicense.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getCar().getCarNum()));
        approvalParkingLot.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
    }

    @Subscribe
    public void setCarsFromServer(CustomerCarsSubscriber event){
        carList=FXCollections.observableArrayList((List<Car>) event.getMessage().getObject());
        setCars();

    }
    private void setCars() {
//        if (car != null && car.getItems() != null) {
//            car.getItems().clear();
//        }
//        cars.setCellValueFactory(new PropertyValueFactory<>("carNum"));
//        carList.forEach(car.getItems()::add);
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
    public void setRegisteredCustomerDataFromServer(RegisteredCutomerSubscriber event) {
        RegisteredCustomer result = (RegisteredCustomer) event.getMessage().getObject();
        System.out.println(result.getFirstName());
//        fnametxt.setText(result.getFirstName());
//        lnametxt.setText(result.getLastName());
//        idtxt.setText(result.getId() + "");
//        emailtxt.setText(result.getEmail());
//        passwordtxt.setVisible(false);


    }

    @FXML
    void switchContext(ActionEvent event) {
        disableAll();
        if (event.getSource() == subscriptionsTab) {
            subsGrid.toFront();
            subsGrid.setVisible(true);
            titleOfPage.setText("Subscriptions");

        }
        else if (event.getSource() == approvalsTab) {
            approvalsGrid.toFront();
            approvalsGrid.setVisible(true);
            titleOfPage.setText("Online Orders that need confirmation of arrival");


        }
        else if (event.getSource() == logsTab) {
            logGrid.toFront();
            logGrid.setVisible(true);
            titleOfPage.setText("Your Logs Of Entering And Exiting Parking Lots");

        }
        else if (event.getSource() == offlineOrdersTab) {
            offlineOrdersGrid.toFront();
            offlineOrdersGrid.setVisible(true);
            titleOfPage.setText("Offline Orders that were made at the Kiosk");

        }
        else if (event.getSource() == refundsTab) {
            refundGrid.toFront();
            refundGrid.setVisible(true);
            titleOfPage.setText("Your Refunds");

        }
        else if (event.getSource() == transactionsTab) {
            transactionGrid.toFront();
            transactionGrid.setVisible(true);
            titleOfPage.setText("All of The transactions that where made by either you or the System");
        }
        else if (event.getSource() == onlineOrdersTab) {
            orderGrid.toFront();
            orderGrid.setVisible(true);
            titleOfPage.setText("Online Orders that were made by you");
        }
    }


    private void disableAll() {
        subsGrid.setVisible(false);
        approvalsGrid.setVisible(false);
        logGrid.setVisible(false);
        offlineOrdersGrid.setVisible(false);
        refundGrid.setVisible(false);
        transactionGrid.setVisible(false);
        orderGrid.setVisible(false);
    }


    @FXML
    void logOutAction(ActionEvent event) {
        Message msg=new Message("#LogOut");
        try {
            SimpleClient.getClient().sendToServer(msg);
        }
        catch (IOException e) {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("Error while trying to log out, please try again later")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT);
        }
    }
    @Subscribe
    public void LogOutStatus(LogoutSubscriber event){
        String msg= (String) event.getMessage().getObject();
        if(msg.equals("Success")){
            runLater(()->{
                Notifications notificationBuilder;
                notificationBuilder = Notifications.create()
                    .title("Success")
                    .text("You have been logged out successfully")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
                notificationBuilder.showConfirm();
            });
            try {
                SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            } catch (IOException e) {
                runLater(()->{
                    Notifications notificationBuilder;
                    notificationBuilder = Notifications.create()
                            .title("Failed")
                            .text("Failed to go back to previous screen, please try again later")
                            .graphic(null)
                            .hideAfter(Duration.seconds(5))
                            .position(Pos.CENTER);
                    notificationBuilder.showError();
                });

            }

        }
        else{
            runLater(()->{
                Notifications notificationBuilder;
                notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Error while trying to log out, please try again later")
                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
            });

        }

    }

}
