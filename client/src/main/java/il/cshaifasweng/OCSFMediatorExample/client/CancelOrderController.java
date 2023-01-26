package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.Refund;
import il.cshaifasweng.MoneyRelatedServices.RefundChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RefundChartSubscriber;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class CancelOrderController {

    @FXML
    private Label OrderID;

    @FXML
    private TextField PLaddress;

    @FXML
    private TextField ammountToPay;

    @FXML
    private Button back;

    @FXML
    private TextField dateTxt;

    @FXML
    private Button done;

    @FXML
    private TextField emailTxt;

    @FXML
    private Button homeBtn;

    @FXML
    private TextField orderIDTxt;

    @FXML
    private Label orderLbl;

    @FXML
    private TextField parkingHoursTxt;

    @FXML
    private AnchorPane paymentWindow;

    @FXML
    private TextField plateNumTxt;

    @FXML
    private TextField refundAmmount;

    @FXML
    private ListView<String> refundList;

    @FXML
    private Label successLbl;

    @FXML
    private Label warningMsg;

    @FXML
    void backToOrder(ActionEvent event) {

    }

    @FXML
    void cancelOrder(ActionEvent event) {

    }

    @FXML
    void goToHome(ActionEvent event) {
        Message message = new Message();
        message.setMessage("CancelOrder");
    }
    @FXML
    public void initialize() throws IOException {

        EventBus.getDefault().register(this);
        Message message = new Message();
        Order order=SimpleChatClient.getCurrentOrder();
        message.setMessage("#GetRefundChart");
        SimpleClient.getClient().sendToServer(message);
    }
    @Subscribe
    public void getRefundList(RefundChartSubscriber event){
        List<RefundChart> refundList = (List<RefundChart>) event.getMessage().getObject();
        for (RefundChart refund: refundList) {
//            this.refundList.getItems().add(refu()+" "+refund.getValue());
        }
        System.out.println(refundList);
    }

}
