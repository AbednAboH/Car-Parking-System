package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingSpotsSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ParkingLotManagerController {
    private List<OnlineOrder> orders;

    @FXML
    private Button ActiveOnlineOrders;

    @FXML
    private Button AllOnlineOrders;

    @FXML
    private Button acceptRequestByn;

    @FXML
    private TableView<OnlineOrder> ordersTable;

    @FXML
    private TableColumn<OnlineOrder, Integer> PLcolumn;

    @FXML
    private TableColumn<OnlineOrder, Integer> customerIDcolumn;

    @FXML
    private TableColumn<OnlineOrder, Date> dateColumn;

    @FXML
    private TableColumn<OnlineOrder, String> emailColumn;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderIDcolumn;

    @FXML
    private TableColumn<OnlineOrder, String> plateNumColumn;

    @FXML
    private TableColumn<OnlineOrder, String> statusColumn;

    @FXML
    private TableView<PricingChart> priceTable;

    @FXML
    private TableView<PricingChart> requestsTable;

    @FXML
    private Button rejectAllBtn;

    @FXML
    private Button rejectRequestBtn;


    @FXML
    private Label userNameLbl;

    @FXML
    private Button logOutBtn;

    @FXML
    private Label ordersCategpryLbl;

    @FXML
    void acceptRequest(ActionEvent event) {

    }

    @FXML
    void rejectAllRequests(ActionEvent event) {
        Message message = new Message("#RejectAllPriceRequests");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rejectRequest(ActionEvent event) {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            Message message = new Message("#RejectOnePriceRequest");
            message.setObject(requestsTable.getSelectionModel().getSelectedItem().getId());
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void showActiveOnlineOrders(ActionEvent event) {
        ordersCategpryLbl.setText("*Showing Active OnlineOrders.");
        Message message = new Message("#GetActiveOnlineOrders");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showAllOnlineOrders(ActionEvent event) {
        ordersCategpryLbl.setText("*Showing All OnlineOrders.");
        Message message = new Message("#GetAllOnlineOrdersForManager");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetOnlineOrdersFromServer(OrderHistoryResponse event) {
        System.out.println("Got response from server");
        orders = (List<OnlineOrder>) event.getMessage().getObject();
        orderIDcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PLcolumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getParkingLotID().getId()));
        customerIDcolumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRegisteredCustomer().getId()));
        emailColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getEmail()));
        plateNumColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getCar().getCarNum()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfOnlineOrder"));
        statusColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().isActive() ? "Active" : "Inactive")
        );
        refreshOnlineOrdersTable();
    }

    private void refreshOnlineOrdersTable() {
        Platform.runLater(() -> {
            ordersTable.getItems().clear();
            orders.forEach(ordersTable.getItems()::add);
            ordersTable.refresh();
            System.out.println("Got orders");
        });
    }

    @Subscribe
    public void GetOnlineOrdersFromServer(UpdateMessageEvent event) {
        System.out.println("Got response from server");
        if (event.getMessage().getMessage().compareTo("#RejectAllPriceRequests") == 0)
            requestsTable.getItems().clear();
        else {
            requestsTable.getItems().removeIf(data ->
                    ((int)event.getMessage().getObject()) == data.getId());
        }
        ordersTable.refresh();
    }

    @FXML
    void logOutUser(ActionEvent event) {
        try {
            EventBus.getDefault().unregister(this);
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        ordersCategpryLbl.setText("*Showing All OnlineOrders.");
        showAllOnlineOrders(null);
        String name = ((ParkingLotEmployee) SimpleChatClient.getUser()).getFirstName();
        userNameLbl.setText("Hello, " + name);
    }

}
