package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.visitorsSubscriberEvent;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.OneTimePass;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.RegularSubscription;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.sun.javafx.application.PlatformImpl.runLater;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DirectToAvailableParkController {
    List<ParkingLot> PLresults;

    @FXML
    private ComboBox<Integer> PLid;

    @FXML
    private GridPane SubscriptionsGrid;

    @FXML
    private Pane Title;

    @FXML
    private Button subsTab;

    @FXML
    private Button backBtn;

    @FXML
    private TextField carIDorderText1;

    @FXML
    private TextField carNumberTxt;

    @FXML
    private Button directBtn;

    @FXML
    private Button onlineOrdersTab;

    @FXML
    private TableColumn<OnlineOrder, String> carNumberColumn;

    @FXML
    private TableColumn<OnlineOrder, Integer> PLcolumn;

    @FXML
    private TableColumn<OnlineOrder, String> orderEmail;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderEntry;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderExit;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderIDcolumn;

    @FXML
    private TableColumn<OnlineOrder, LocalDate> orderParchaseDate;

    @FXML
    private TableView<OnlineOrder> orderTable;

    @FXML
    private GridPane orderGrid;

    @FXML
    private TextField subIDtext;

    @FXML
    private TextField orderIDtxt;

    @FXML
    private Button orderSearchButton1;

    @FXML
    private Button searchBtn;

    @FXML
    private StackPane stackedpane;

    @FXML
    private TableColumn<RegularSubscription, String> subCars;

    @FXML
    private TableColumn<RegularSubscription, LocalDate> subExpiration;

    @FXML
    private TableColumn<RegularSubscription, String> emailSub;

    @FXML
    private TableColumn<RegularSubscription, Integer> subID;

    @FXML
    private TableColumn<RegularSubscription, Integer> subPLot;

    @FXML
    private TableColumn<RegularSubscription, LocalDate> subParchaseDate;

    @FXML
    private TableView<RegularSubscription> subsTable;

    @FXML
    private Label titleOfPage;


    @FXML
    void back(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            EventBus.getDefault().unregister(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void direct(ActionEvent event) {
        try {
            RegularSubscription tmpSub = subsTable.getSelectionModel().getSelectedItem();
            OnlineOrder tmpOrder = orderTable.getSelectionModel().getSelectedItem();
            if ((tmpOrder != null || tmpSub != null) && PLid.getValue() != null) {
                directBtn.setDisable(true);
                ParkingLot tmpPL = null;
                for (ParkingLot p :
                        PLresults) {
                    if (p.getId() == PLid.getValue()) {
                        tmpPL = p;
                        break;
                    }
                }
                Message msg = new Message("#DirectToAvailblePark");
                msg.setObject(orderIDtxt.getText() + "&" + carNumberTxt.getText());
                if (tmpOrder != null) {
                    OneTimePass newOneTimePass = new OneTimePass(tmpOrder, tmpPL);
                    tmpOrder.setOneTimePass(newOneTimePass);
                    msg.setObject(tmpOrder);
                } else {
                    OneTimePass newOneTimePass = new OneTimePass(tmpSub, tmpPL);
                    tmpSub.setOneTimePass(newOneTimePass);
                    msg.setObject(tmpSub);
                }
                SimpleClient.getClient().sendToServer(msg);
            } else {
                Notifications notification = Notifications.create()
                        .title("Choose item")
                        .text("Please choose an item from the table and a parking lot.")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notification.showError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void search(ActionEvent event) {
        try {
            if (event.getSource() == searchBtn) {
                if (orderIDtxt.getText().compareTo("") != 0 && carNumberTxt.getText().compareTo("") != 0) {
                    Message msg = new Message("#getOnlineOrder");
                    msg.setObject(orderIDtxt.getText() + "&" + carNumberTxt.getText());
                    SimpleClient.getClient().sendToServer(msg);
                }
            } else {
                if (subIDtext.getText().compareTo("") != 0 && carIDorderText1.getText().compareTo("") != 0) {
                    Message msg = new Message("#getSubscriptionOrder");
                    msg.setObject(subIDtext.getText() + "&" + carIDorderText1.getText());
                    SimpleClient.getClient().sendToServer(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void FoundOrder(visitorsSubscriberEvent event) {
        runLater(() -> {
            System.out.println(event.getMessage().getObject());
            if (event.getMessage().getObject() != null) {
                directBtn.setDisable(false);
                OnlineOrder tmp = (OnlineOrder)event.getMessage().getObject();
                updateComboBox(tmp.getParkingLotID().getId());
                orderTable.getItems().clear();
                orderIDcolumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
                carNumberColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
                PLcolumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
                orderParchaseDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
                orderEntry.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
                orderExit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getExiting()));
                orderEmail.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEmail()));
                orderTable.getItems().add((OnlineOrder) event.getMessage().getObject());
            } else {
                Notifications notification = Notifications.create()
                        .title("No order found")
                        .text("No order found with the given details")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notification.showError();
            }
        });
    }

    @Subscribe
    public void FoundSubscription(SubscriptionSubscriber event) {
        runLater(() -> {
            System.out.println(event.getMessage().getObject());
            if (event.getMessage().getObject() != null) {
                directBtn.setDisable(false);
                RegularSubscription tmp = (RegularSubscription)event.getMessage().getObject();
                updateComboBox(tmp.getDesegnatedParkingLot().getId());
                subsTable.getItems().clear();
                subID.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
                subCars.setCellValueFactory(data -> new SimpleObjectProperty<>(carIDorderText1.getText()));
                subPLot.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDesegnatedParkingLot().getId()));
                subParchaseDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartDate()));
                subExpiration.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getExpirationDate()));
                emailSub.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEmail()));
                subsTable.getItems().add((RegularSubscription) event.getMessage().getObject());
            } else {
                Notifications notification = Notifications.create()
                        .title("Not found")
                        .text("No Regeular Subscription found with the given details, or customer already have full subscription")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notification.showError();
            }
        });
    }


    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        PLresults = (List<ParkingLot>) event.getMessage().getObject();
    }

    private void updateComboBox(int customerPL) {
        PLid.getItems().clear();
        for (ParkingLot PL :
                PLresults) {
            if (!PL.isFull() && PL.getId() != customerPL)
                PLid.getItems().add(PL.getId());
        }
    }

    private void initControls(){
        directBtn.setDisable(true);
        SubscriptionsGrid.setVisible(false);
        orderGrid.setVisible(false);
        orderTable.getItems().clear();
        PLid.getItems().clear();
        subsTable.getItems().clear();
        orderIDtxt.setText("");
        carNumberTxt.setText("");
        subIDtext.setText("");
        carIDorderText1.setText("");
    }
    @FXML
    void switchContext(ActionEvent event) {
        initControls();
        if (event.getSource() == subsTab) {
            SubscriptionsGrid.setVisible(true);
            SubscriptionsGrid.toFront();
            titleOfPage.setText("Search in Subscriptions");
        } else {
            orderGrid.setVisible(true);
            orderGrid.toFront();
            titleOfPage.setText("Search in Online Orders");
        }
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        directBtn.setDisable(true);
        Message message = new Message("#getAllParkingLots");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
