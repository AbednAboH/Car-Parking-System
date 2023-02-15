package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingSpotsSubscriber;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class ParkingLotManagerController {
    private List<Order> orders;

    @FXML
    private Button ActiveOrders;

    @FXML
    private Button AllOrders;

    @FXML
    private Button acceptRequestByn;

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableView<?> priceTable;

    @FXML
    private Button rejectAllBtn;

    @FXML
    private Button rejectRequestBtn;

    @FXML
    private TableView<?> requestsTable;

    @FXML
    private Button userOptBtn;

    @FXML
    private Button userOptBtn1;

    @FXML
    void acceptRequest(ActionEvent event) {

    }

    @FXML
    void rejectAllRequests(ActionEvent event) {

    }

    @FXML
    void rejectRequest(ActionEvent event) {

    }

    @FXML
    void showActiveOrders(ActionEvent event) {
        Message message = new Message("#GetActiveOrders");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showAllOrders(ActionEvent event) {
        Message message = new Message("#GetAllOrdersForManager");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetOrdersFromServer(OrderHistoryResponse event) {
        System.out.println("Got response from server");
        orders = (List<Order>) event.getMessage().getObject();
    }

}
