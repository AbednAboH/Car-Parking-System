package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.Transactions;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionSubscriber;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

public class DirectToAvailbleParkController {
    private int ifFound;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderID;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderID1;
    @FXML
    private TableColumn<OnlineOrder, String> orderEmail;

    @FXML
    private TableColumn<OnlineOrder, String> orderEmail1;









    @FXML
    private ComboBox<Integer> PLid;

    @FXML
    private Button backBtn;


    @FXML
    private TextField carNumberTxt;


    @FXML
    private Button directBtn;


    @FXML
    private TextField transactionIDtxt;



    @FXML
    private Button searchBtn;

    @FXML
    void back(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void direct(ActionEvent event) {

        directBtn.setDisable(true);
        try {
            if (orderTable.getSelectionModel().getSelectedItem() != null && PLid.getValue()!=null ){
                Message msg=new Message ("#DirectToAvailblePark" );
                msg.setObject(transactionIDtxt.getText()+"&"+carNumberTxt.getText());
                OneTimePass newOneTimePass = new OneTimePass();
                Transactions transaction = orderTable.getSelectionModel().getSelectedItem();
                if(ifFound == 1)
                    ((Subscription)transaction).setOneTimePass(newOneTimePass);
                if(ifFound == 2)
                    ((OnlineOrder)transaction).setOneTimePass(newOneTimePass);
                SimpleClient.getClient().sendToServer(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search(ActionEvent event) {
        try {
            if(transactionIDtxt.getText().compareTo("") != 0 && carNumberTxt.getText().compareTo("") != 0) {
                ifFound = 0;
                Message msg = new Message("#getOnlineOrder");
                msg.setObject(transactionIDtxt.getText() + "&" + carNumberTxt.getText());
                SimpleClient.getClient().sendToServer(msg);

                msg.setMessage("#getSubscriptionOrder");
                msg.setObject(transactionIDtxt.getText() + "&" + carNumberTxt.getText());
                SimpleClient.getClient().sendToServer(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Subscribe
    public void FoundSubscription(SubscriptionSubscriber event){
        System.out.println(event.getMessage().getObject());
        if (event.getMessage().getObject()!=null){

            Object transation =event.getMessage().getObject();
            if( transation instanceof Subscription) {
                orderTable=new TableView<Subscription>();

                ifFound = 1;
                orderTable.getItems().clear();

                orderIDcolumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
                carNumberColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
                PLcolumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
//            orderParchaseDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
//            orderPricePaid.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getValue()));
//            orderEntry.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
//            orderExit.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getExiting()));
//            orderActive.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().isActive()));
//            orderEmail.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getEmail()));
                orderTable.getItems().add((OnlineOrder) event.getMessage().getObject());
            }
        }
        else if(ifFound==0){
            Notifications notification = Notifications.create()
                    .title("No transaction found")
                    .text("No transaction found with the given details")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
        }
    }

    @Subscribe
    public void FoundOrder(visitorsSubscriberEvent event){
        System.out.println(event.getMessage().getObject());

        if (event.getMessage().getObject()!=null ){
            ifFound=2;
            orderTable.getItems().clear();
            orderIDcolumn.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getId()));
            carNumberColumn.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
            PLcolumn.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
//            orderParchaseDate.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDate()));
//            orderPricePaid.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getValue()));
//            orderEntry.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
//            orderExit.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getExiting()));
//            orderActive.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().isActive()));
//            orderEmail.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getEmail()));
            orderTable.getItems().add((OnlineOrder) event.getMessage().getObject());
        }


    }

    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event){
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        ObservableList<ParkingLotModel> tmp = FXCollections.observableArrayList();
        for (ParkingLot PL :
                PLresults) {
            if(!PL.isFull())
              PLid.getItems().add(PL.getId());
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
