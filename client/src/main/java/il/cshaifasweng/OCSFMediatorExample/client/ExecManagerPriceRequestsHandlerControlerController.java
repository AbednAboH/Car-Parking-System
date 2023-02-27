package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CurrentPriceSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.LogoutSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.PriceRequestsSubscriber;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static com.sun.javafx.application.PlatformImpl.runLater;
import java.io.IOException;
import java.util.List;

public class ExecManagerPriceRequestsHandlerControlerController {
    PricingChart currPC;
    List<PricingChart> reqs;

    @FXML
    private Button ActiveOrders;

    @FXML
    private Button AllOrders;

    @FXML
    private TableColumn<?, ?> PLcolumn;

    @FXML
    private Button acceptRequestBtn;

    @FXML
    private TableColumn<?, ?> customerIDcolumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private Button logOutBtn;

    @FXML
    private TableView<?> priceTable;

    @FXML
    private TableView<PricingChart> requestsTable;

    @FXML
    private TableColumn<PricingChart, Double> regularSubColumn;

    @FXML
    private TableColumn<PricingChart, Double> orderBeforeColumn;

    @FXML
    private TableColumn<PricingChart, Double> multRegSubColumn;

    @FXML
    private TableColumn<PricingChart, Double> fullSubColumn;

    @FXML
    private TableColumn<PricingChart, Double> idColumn;

    @FXML
    private TableColumn<PricingChart, Double> kioskColumn;

    @FXML
    private TableView<PricingChart> currentPriceTable;

    @FXML
    private TableColumn<PricingChart, Double> regularSubColumn1;

    @FXML
    private TableColumn<PricingChart, Double> orderBeforeColumn1;

    @FXML
    private TableColumn<PricingChart, Double> multRegSubColumn1;

    @FXML
    private TableColumn<PricingChart, Double> fullSubColumn1;

    @FXML
    private TableColumn<PricingChart, Double> idColumn1;

    @FXML
    private TableColumn<PricingChart, Double> kioskColumn1;

    @FXML
    private Label ordersCategpryLbl;

    @FXML
    private TableView<?> ordersTable;


    @FXML
    private TableColumn<?, ?> orderIDcolumn;


    @FXML
    private TableColumn<?, ?> plateNumColumn;


    @FXML
    private Button refreshRequestsBtn;


    @FXML
    private Button rejectAllBtn;

    @FXML
    private Button rejectRequestBtn;


    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private Label userNameLbl;
    @Subscribe
    public void LogOutStatus(LogoutSubscriber event){
        String msg= (String) event.getMessage().getObject();
        if(msg.startsWith("Success")){

            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            } catch (IOException e) {
                System.out.println("Failed to go back to previous screen");
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
    @FXML
    void logOutUser(ActionEvent event) {
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

    @FXML
    void acceptRequest(ActionEvent event) {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            PricingChart pc = requestsTable.getSelectionModel().getSelectedItem();
            try {
                Message message = new Message("#AcceptPriceRequest");
                message.setObject(pc);
                SimpleClient.getClient().sendToServer(message);
                updatePriceCharts();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Notifications notification = Notifications.create()
                    .title("Choose a request")
                    .text("Choose a Request from the table.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
        }
    }

    @FXML
    void rejectAllRequests(ActionEvent event) {
        if (requestsTable.getItems().size() > 0) {
            try {
                Message message = new Message("#RejectAllPriceRequests");
                SimpleClient.getClient().sendToServer(message);
                updatePriceCharts();

                message.setMessage("#GetPriceRequests");
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Notifications notification = Notifications.create()
                    .title("No requests")
                    .text("There are no requests to reject.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
        }

    }

    @FXML
    void rejectRequest(ActionEvent event) {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            PricingChart pc = requestsTable.getSelectionModel().getSelectedItem();
            pc.setWaitForPermission(false);
            try {
                Message message = new Message("#RejectPriceRequest");
                message.setObject(pc);
                SimpleClient.getClient().sendToServer(message);
                message.setMessage("#GetPriceRequests");
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Notifications notification = Notifications.create()
                    .title("Choose a request")
                    .text("Choose a Request from the table.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notification.showError();
        }
    }


    @FXML
    void refreshRequestsTable(ActionEvent event) {
        try {
            Message message = new Message("#GetPriceRequests");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetPriceRequestsFromServer(PriceRequestsSubscriber event) {
        runLater(() -> {
            System.out.println("Got response for reqs");
            reqs = (List<PricingChart>) event.getMessage().getObject();
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            fullSubColumn.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getFullSubHours() * data.getValue().getOrderBeforeHandPrice()));
            multRegSubColumn.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getMultipleCarRegularSubHours()
                            * data.getValue().getOrderBeforeHandPrice()));
            regularSubColumn.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getRegularSubHours()
                            * data.getValue().getOrderBeforeHandPrice()));
            kioskColumn.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getKioskPrice()));
            orderBeforeColumn.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getOrderBeforeHandPrice()));

            requestsTable.getItems().clear();
            reqs.forEach(requestsTable.getItems()::add);
            requestsTable.refresh();
        });
    }

    @Subscribe
    public void GetCurrentPriceFromServer(CurrentPriceSubscriber event) {
        runLater(() -> {
            System.out.println("Got response for current");
            currPC = (PricingChart) event.getMessage().getObject();
            idColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
            fullSubColumn1.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getFullSubHours() * data.getValue().getOrderBeforeHandPrice()));
            multRegSubColumn1.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getMultipleCarRegularSubHours()
                            * data.getValue().getOrderBeforeHandPrice()));
            regularSubColumn1.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getRegularSubHours()
                            * data.getValue().getOrderBeforeHandPrice()));
            kioskColumn1.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getKioskPrice()));
            orderBeforeColumn1.setCellValueFactory(data ->
                    new SimpleObjectProperty<>(data.getValue().getOrderBeforeHandPrice()));

            currentPriceTable.getItems().clear();
            currentPriceTable.getItems().add(currPC);
            currentPriceTable.refresh();
        });
    }


    @FXML
    void backClick(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            EventBus.getDefault().unregister(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        updatePriceCharts();
    }

    private void updatePriceCharts() {
        try {
            Message message = new Message("#GetPriceRequests");
            SimpleClient.getClient().sendToServer(message);

            message.setMessage("#GetCurrentPrice");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
