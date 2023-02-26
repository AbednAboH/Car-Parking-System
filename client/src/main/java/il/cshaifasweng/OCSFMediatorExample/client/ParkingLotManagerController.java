package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ParkingLotManagerController {
    private List<OnlineOrder> orders;

    @FXML
    private Button ActiveOrders;

    @FXML
    private Label userNameLbl;

    @FXML
    private Button AllOrders;

    @FXML
    private TableColumn<OnlineOrder, Integer> PLcolumn;

    @FXML
    private TableColumn<OnlineOrder, Integer> customerIDcolumn;

    @FXML
    private TableColumn<OnlineOrder, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<OnlineOrder, String> emailColumn;

    @FXML
    private Button logOutBtn;

    @FXML
    private TableColumn<OnlineOrder, Integer> orderIDcolumn;

    @FXML
    private TableView<OnlineOrder> ordersTable;

    @FXML
    private TableColumn<OnlineOrder, String> plateNumColumn;

    @FXML
    private Button requestPriceChangeBtn;

    @FXML
    private TableColumn<OnlineOrder, String> statusColumn;

    @FXML
    private Label ordersCategoryLbl;

    @FXML
    void logOutUser(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("logInScreen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showActiveOrders(ActionEvent event) {
        ordersCategoryLbl.setText("*Showing Active Orders.");
        Message message = new Message("#GetActiveOrders");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showAllOrders(ActionEvent event) {
        ordersCategoryLbl.setText("*Showing All Orders.");
        Message message = new Message("#GetAllOrdersForManager");
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
        dateColumn.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDateOfOrder()));
        statusColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().isActive() ? "Active" : "Inactive")
        );
        refreshOrdersTable();
    }

    private void refreshOrdersTable() {
        Platform.runLater(() -> {
            ordersTable.getItems().clear();
            orders.forEach(ordersTable.getItems()::add);
            ordersTable.refresh();
            System.out.println("Got orders");
        });
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        ordersCategoryLbl.setText("*Showing All OnlineOrders.");
        showAllOrders(null);

    }

}
