package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

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
    private TableColumn<?, ?> orderActive;

    @FXML
    private TableColumn<?, ?> orderActive1;

    @FXML
    private TableColumn<?, ?> orderEmail;

    @FXML
    private TableColumn<?, ?> orderEmail1;

    @FXML
    private TableColumn<?, ?> orderEntry;

    @FXML
    private TableColumn<?, ?> orderEntry1;

    @FXML
    private TableColumn<?, ?> orderExit;

    @FXML
    private TableColumn<?, ?> orderExit1;

    @FXML
    private GridPane orderGrid;

    @FXML
    private TableColumn<?, ?> orderID;

    @FXML
    private TableColumn<?, ?> orderID1;

    @FXML
    private TextField orderIDtext;

    @FXML
    private TextField orderIDtext1;

    @FXML
    private TableColumn<?, ?> orderLicense;

    @FXML
    private TableColumn<?, ?> orderLicense1;

    @FXML
    private TableColumn<?, ?> orderPLotID;

    @FXML
    private TableColumn<?, ?> orderPLotID1;

    @FXML
    private TableColumn<?, ?> orderParchaseDate;

    @FXML
    private TableColumn<?, ?> orderParchaseDate1;

    @FXML
    private TableColumn<?, ?> orderPricePaid;

    @FXML
    private TableColumn<?, ?> orderPricePaid1;

    @FXML
    private Button orderSearchButton;

    @FXML
    private Button orderSearchButton1;

    @FXML
    private TableView<?> ordersTable;

    @FXML
    private TableView<?> ordersTable1;

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

    @FXML
    void AddSubscriptions(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("SubscriptionScreen");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void backToMain(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("mainPage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        // TODO: 22/02/2023 check implementation
    }

    @FXML
    void placeOrder(ActionEvent event) {
        try {
            SimpleChatClient.setRoot("orderGUI");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void switchContext(ActionEvent event) {

    }

}
