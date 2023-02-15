package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class ParkingLotManagerController {

    @FXML
    private Button ActiveOrders;

    @FXML
    private Button AllOrders;

    @FXML
    private Button acceptRequestByn;

    @FXML
    private TableView<?> ordersTable;

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

    }

    @FXML
    void showAllOrders(ActionEvent event) {

    }

}
