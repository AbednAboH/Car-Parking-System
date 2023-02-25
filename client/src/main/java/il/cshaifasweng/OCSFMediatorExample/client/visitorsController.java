package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UnconfirmedArrivalSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.visitorsSubscriberEvent;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.OneTimePass;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import il.cshaifasweng.customerCatalogEntities.Subscription;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class visitorsController {

    @FXML
    private Pane Title;

    @FXML
    private Button addsubscriptionbutton;

    @FXML
    private GridPane approvalGrid;

    @FXML
    private Button approvalsTab;

    @FXML
    private Button approve;

    @FXML
    private Button back;

    @FXML
    private Button cancelorderbutton;

    @FXML
    private TextField carIDorderText;

    @FXML
    private TextField carIDorderText1;

    @FXML
    private Button complaintsTab;

    @FXML
    private Button decline;

    @FXML
    private Button onlineOrdersTab;

    @FXML
    private TableColumn<OnlineOrder, Boolean> orderActive;

    @FXML
    private TableColumn<OnlineOrder, Boolean> orderActive1;

    @FXML
    private TableColumn<OnlineOrder, String> orderEmail;

    @FXML
    private TableColumn<OnlineOrder, String> orderEmail1;

    @FXML
    private TableColumn<OnlineOrder,LocalDateTime> orderEntry;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderEntry1;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderExit;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> orderExit1;

    @FXML
    private GridPane orderGrid;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderID;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderID1;

    @FXML
    private TextField orderIDtext;

    @FXML
    private TextField orderIDtext1;

    @FXML
    private TableColumn<OnlineOrder, String> orderLicense;

    @FXML
    private TableColumn<OnlineOrder, String> orderLicense1;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderPLotID;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderPLotID1;

    @FXML
    private TableColumn<OnlineOrder, LocalDate> orderParchaseDate;

    @FXML
    private TableColumn<OnlineOrder, LocalDate> orderParchaseDate1;

    @FXML
    private TableColumn<OnlineOrder, Double> orderPricePaid;

    @FXML
    private TableColumn<OnlineOrder, Double> orderPricePaid1;

    @FXML
    private Button orderSearchButton1;

    @FXML
    private Button searchOrderButton;

    @FXML
    private TableView<OnlineOrder> ordersTable;

    @FXML
    private TableView<OnlineOrder> ordersTable1;

    @FXML
    private Button placebutton;

    @FXML
    private StackPane stackedpane;

    @FXML
    private GridPane subsGrid;

    @FXML
    private Button subscriptionsTab;

    @FXML
    private Label titleOfPage;

    @Subscribe
    public void confimationDone(UnconfirmedArrivalSubscriber event){
        if (event.getMessage().getObject()!=null){
            notifySuccess();
        }
        else {
            notifyFailure();
        }
    }

    private static void notifyFailure() {
        runLater(() -> {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("The System Wasn't updated with your Decision\n Please try again later")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notificationBuilder.showError();
        });
    }

    private static void notifySuccess(){
        runLater(()->{Notifications notificationBuilder = Notifications.create()
                .title("Success")
                .text("The System Was updated with your Decision")
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER);
            notificationBuilder.showConfirm();});
    }

    @FXML
    void approveArrival(ActionEvent event) {
        // TODO: 22/02/2023 fix this , you didn't finish it till the end ! but a great concept !
            Message msg = new Message("#confirmCustomerArrival");
            if (ordersTable1.getSelectionModel().getSelectedItem()!=null){
                if (event.getSource()==approve)
                    ordersTable1.getSelectionModel().getSelectedItem().setAgreedToPayPenalty(true);
                else
                    ordersTable1.getSelectionModel().getSelectedItem().setAgreedToPayPenalty(false);
                msg.setObject(ordersTable1.getSelectionModel().getSelectedItem());
                try {
                    SimpleClient.getClient().sendToServer(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Notifications notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Please select an order to approve")
                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
            }

    }
    @Subscribe
    public void FoundOrderUnconfiremdArrival(visitorsSubscriberEvent event){
        System.out.println(event.getMessage().getObject());

      if (event.getMessage().getObject()!=null){
          ordersTable1.getItems().clear();
          orderID1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getId()));
          orderLicense1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
          orderPLotID1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
          orderParchaseDate1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
          orderPricePaid1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getValue()));
          orderEntry1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
          orderExit1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getExiting()));
          orderActive1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().isActive()));
          orderEmail1.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getEmail()));
          ordersTable1.getItems().add((OnlineOrder) event.getMessage().getObject());
      }
      else{
            Notifications notification = Notifications.create()
                    .title("No order found")
                    .text("No order found with the given details")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
      }

    }
    @Subscribe
    public void FoundOrder(visitorsSubscriberEvent event){
        System.out.println(event.getMessage().getObject());
        if (event.getMessage().getObject()!=null){
            ordersTable.getItems().clear();
            orderID.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getId()));
            orderLicense.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
            orderPLotID.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
            orderParchaseDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
            orderPricePaid.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getValue()));
            orderEntry.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
            orderExit.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getExiting()));
            orderActive.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().isActive()));
            orderEmail.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getEmail()));
            ordersTable.getItems().add((OnlineOrder) event.getMessage().getObject());
        }
        else{
            Notifications notification = Notifications.create()
                    .title("No order found")
                    .text("No order found with the given details")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
        }
    }
    @FXML
    void searchForOrder(ActionEvent event) {
        if (event.getSource()==searchOrderButton){
            validateInputForSearch(orderIDtext, carIDorderText);
        }
        //i.e the other search botton , for validation and stuff !
        else{
            validateInputForSearch(orderIDtext1, carIDorderText1);
        }

    }

    private void validateInputForSearch(TextField orderIDtext1, TextField carIDorderText1) {
        if (!InputValidator.isValidNumber(orderIDtext1.getText())&&!InputValidator.isValidPlateNumber(carIDorderText1.getText())){
            System.out.println(InputValidator.isValidNumber(orderIDtext1.getText()));
            System.out.println(InputValidator.isValidPlateNumber(carIDorderText1.getText()));
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("please Enter a Valid Order ID or Car ID")
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.CENTER);
            notificationBuilder.showWarning();
        }
        else{
            try {
                Message msg=new Message ("#getOnlineOrder" );
                msg.setObject(orderIDtext1.getText()+"&"+carIDorderText1.getText());
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    void AddSubscriptions(ActionEvent event) {
        try {
            SimpleChatClient.addScreen("visitorsController");
            SimpleChatClient.setRoot("SubscriptionScreen");
            EventBus.getDefault().unregister(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void backToMain(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            EventBus.getDefault().unregister(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        Notifications notificationBuilder = Notifications.create()
                .title("Error")
                .text("Please select a row")
                .hideAfter(Duration.seconds(3))
                .position(Pos.CENTER);
        if (ordersTable.getSelectionModel().getSelectedItem()!=null){
            SimpleChatClient.setCurrentOrder(ordersTable.getSelectionModel().getSelectedItem());

            try {
                SimpleChatClient.addScreen("visitorsController");
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
    void placeOrder(ActionEvent event) {
        try {
            SimpleChatClient.addScreen("visitorsController");
            SimpleChatClient.setRoot("orderGUI");
            EventBus.getDefault().unregister(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void switchContext(ActionEvent event) {
        disableAll();
        if (event.getSource() == approvalsTab) {
            approvalGrid.setVisible(true);
            approvalGrid.toFront();
            titleOfPage.setText("Approvals");
        } else if (event.getSource() == subscriptionsTab) {
            subsGrid.setVisible(true);
            subsGrid.toFront();
            titleOfPage.setText("Subscriptions");
        } else if (event.getSource() == onlineOrdersTab) {
            orderGrid.setVisible(true);
            orderGrid.toFront();
            titleOfPage.setText("Online Orders");
        } else if (event.getSource() == complaintsTab) {
            try {
                SimpleChatClient.addScreen("visitorsController");
                SimpleChatClient.setRoot("complaint");
                EventBus.getDefault().unregister(this);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void disableAll(){
        approvalGrid.setVisible(false);
        subsGrid.setVisible(false);
        orderGrid.setVisible(false);
    }
    @FXML
    void initialize(){
        try {
            EventBus.getDefault().register(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}


