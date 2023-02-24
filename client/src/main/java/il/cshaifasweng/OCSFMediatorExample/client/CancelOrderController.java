package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.LogInEntities.User;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.RefundChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CancelationRefundSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.RefundChartSubscriber;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.runLater;
import static il.cshaifasweng.OCSFMediatorExample.client.PaymentController.fillKnownOrder;

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
    private TableColumn<RefundChart, String> hoursBeforeParking;
    @FXML
    private TableView<RefundChart> refundCriteriaTable;

    @FXML
    private TableColumn<RefundChart, String> refundPercentage;
    @FXML
    private ProgressIndicator backProgress;
    @FXML
    private ProgressIndicator progressCancelation;
    @FXML
    void backToOrder(ActionEvent event) {
        try{
            backProgress.setVisible(true);
            SimpleChatClient.setRoot(((RegisteredCustomer)SimpleChatClient.getUser()).getGUI());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void cancelOrder(ActionEvent event) throws IOException {
        Message message = new Message();
        message.setMessage("#CancelOrderAndGetRefund&"+refundAmmount.getText()+"&"+ SimpleChatClient.getCurrentOrder().getId());
        SimpleClient.getClient().sendToServer(message);
        done.setDisable(true);
        progressCancelation.setVisible(true);

    }

    @Subscribe
    public void getRefund(CancelationRefundSubscriber event){
        displaySuccessStatus();
    }
    private void displaySuccessStatus() {
        runLater(() -> {
            progressCancelation.setVisible(false);
            successLbl.setVisible(true);
            warningMsg.setVisible(false);
            try {
                SimpleChatClient.setRoot(((User)SimpleChatClient.getUser()).getGUI());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
        fillKnownOrder(SimpleChatClient.getCurrentOrder(), emailTxt,
                plateNumTxt, dateTxt, PLaddress,
                parkingHoursTxt, ammountToPay);
        message.setMessage("#GetRefundChart");
        SimpleClient.getClient().sendToServer(message);
    }



    @Subscribe
    public void getRefundList(RefundChartSubscriber event){
        ObservableList<RefundChart> observableRefundList = FXCollections.observableArrayList((List<RefundChart>) event.getMessage().getObject());

        if (refundCriteriaTable != null && refundCriteriaTable.getItems() != null) {
            refundCriteriaTable.getItems().clear();

        }
        hoursBeforeParking.setCellValueFactory(data-> new SimpleObjectProperty<>(switch (data.getValue().getFromTime() + "-" + data.getValue().getToTime()) {
            case "0-1" -> "0-1 hour";
            case "1-3" -> "1-3 hours";
            case "3--1" -> "more than 3 hours";
            default -> "not Valid Info";

        }));
        refundPercentage.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getValue()*100 + "%"));
        observableRefundList.forEach(refundCriteriaTable.getItems()::add);
        setRefund(observableRefundList);
    }
    public void setRefund(List<RefundChart> refundChart){
        OnlineOrder onlineOrder = SimpleChatClient.getCurrentOrder();
        double ammountPaid= onlineOrder.getValue();
        LocalDateTime date =LocalDateTime.now();
        if(onlineOrder.getDateOfOrder().getDayOfYear()-date.getDayOfYear()>0){
            refundAmmount.setText(refundChart.get(2).getValue()*ammountPaid+"");

        }
        else if(onlineOrder.getDateOfOrder().getDayOfYear()-date.getDayOfYear()==0){
            if(onlineOrder.getHoursOfResidency()>3){
                refundAmmount.setText(refundChart.get(2).getValue()*ammountPaid+"");
            }
            else if(onlineOrder.getHoursOfResidency()>1){
                refundAmmount.setText(refundChart.get(1).getValue()*ammountPaid+"");
            }
            else if(onlineOrder.getHoursOfResidency()>0){
                refundAmmount.setText(refundChart.get(0).getValue()*ammountPaid+"");
            }
            else{
                refundAmmount.setText("0");
            }
        }
        else{
            refundAmmount.setText("0");
        }
    }

}
