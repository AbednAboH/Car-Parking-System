package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.LogInEntities.Employees.CustomerServiceEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CompliantsSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.SubscriptionsChartResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.models.SubscriptionChartModel;
import il.cshaifasweng.customerCatalogEntities.AbstractOrder;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import il.cshaifasweng.customerCatalogEntities.OfflineOrder;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.application.Platform.runLater;


public class CompliantsListController {
    private OnlineOrder observableOnlineOrders;
    @FXML
    private TableColumn<AbstractOrder, Integer> orderID1;

    @FXML
    private TableColumn<AbstractOrder,String> orderLicense;

    @FXML
    private TableColumn<AbstractOrder, String> orderPLotID;

    @FXML
    private TableColumn<AbstractOrder, LocalDateTime> orderParchaseDate;

    @FXML
    private TableColumn<AbstractOrder, Double> orderPricePaid;

    @FXML
    private TableView<AbstractOrder> ordersTable;


    @FXML
    private TextField amount;

    @FXML
    private Button closeComplaint;

    @FXML
    private TableView<Complaint> complaints;

    @FXML
    private TableColumn<Complaint, Integer> complaintId;

    @FXML
    private TableColumn<Complaint, Integer> userId;

    @FXML
    private TableColumn<Complaint, Integer> pLotIdCol;

    @FXML
    private TableColumn<Complaint, String> complaintDescription;

    @FXML
    private TableColumn<Complaint, LocalDate> dateCol;

    @FXML
    private TableColumn<Complaint, LocalTime> lastDateCol;

    @FXML
    private Label messageLabel;

    @FXML
    private Button exitBtn;

    @FXML
    private Button fullRefund;

    @FXML
    private Button handleComplaint;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button onBack;

    @FXML
    private Button refundByAmountBtn;

    @FXML
    private Button userOptBtn;

    @FXML
    private Button closedCompliants;


    private ObservableList<Complaint> observableList;
    @FXML
    private Button HandleComplaintTab;

    @FXML
    private TextField HourlyIDTxt;

    @FXML
    private Button RequestChangeOfPrice;

    @FXML
    private GridPane SubmitChanges;

    @FXML
    private VBox SuccessStatus;

    @FXML
    private Button applyAmountBtn;

    @FXML
    private Button applyPriceBtn;

    @FXML
    private VBox errorMsgArea;

    @FXML
    private HBox handleComplaints;

    @FXML
    private TextField hourlyPriceTxt;

    @FXML
    private TableColumn<SubscriptionChartModel, Integer> hoursInMonthColumn;

    @FXML
    private TextField newAmountTxt;

    @FXML
    private Button okBtn;

    @FXML
    private Button resetPricngChartBtn;

    @FXML
    private Button sendRequest;

    @FXML
    private TableColumn<SubscriptionChartModel, Integer> subIDcolumn;

    @FXML
    private TableColumn<SubscriptionChartModel, Double> subPriceColumn;

    @FXML
    private TableView<SubscriptionChartModel> subTable;

    @FXML
    private TextField subToChangeIDtxt;

    @FXML
    private TableColumn<?, ?> subTypeColumn;

    @FXML
    private TableColumn<?, ?> totalPriceColumn;

    private Complaint selected;

    private int getComplaintId;

    private int getUserId;

    private int getParkingLotId;
    private boolean isRemoved;
    @FXML
    private TextArea Reply;
    @FXML
    private TextArea Content;
    @FXML
    private TableColumn<Complaint, String> orderID;


    PricingChart PCresult,ChangedPCresult;


    @FXML
    void HideErrorMsg(ActionEvent event) {
        errorMsgArea.setVisible(false);
    }

    boolean isAmountValid() {
        String subID = subToChangeIDtxt.getText();
        String newAmount = newAmountTxt.getText();

        if (subID.compareTo("") == 0||newAmount.compareTo("") == 0) {
            return false;
        }
        try {
            if (Integer.parseInt(newAmount) <= 0||Integer.parseInt(subID) < 3 || Integer.parseInt(subID) > 5)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean isPriceValid() {
        String subID = HourlyIDTxt.getText();
        String newPrice = hourlyPriceTxt.getText();


        if (subID.compareTo("") == 0||newPrice.compareTo("") == 0)
            return false;
        try {
            if (Double.parseDouble(newPrice) <= 0||Integer.parseInt(subID) <= 0 || Integer.parseInt(subID) > 2)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @FXML
    void SendPriceChange(ActionEvent event) {
        String subID = HourlyIDTxt.getText();
        String newAmount = hourlyPriceTxt.getText();

        if (!isPriceValid()) {
            errorMsgArea.setVisible(true);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            hourlyPriceTxt.clear();
            HourlyIDTxt.clear();
            switch (Integer.parseInt(subID)) {
                case 1 -> ChangedPCresult.setKioskPrice(Double.parseDouble(newAmount));
                case 2 -> ChangedPCresult.setOrderBeforeHandPrice(Double.parseDouble(newAmount));
                default -> System.out.println("error");
            }

            fillPricingChart(ChangedPCresult);
            System.out.println("was here price change");

        }
    }
    @Subscribe
    public void showOrdersFromServer(OrderHistoryResponse event) {
        if (event.getMessage().getObject() instanceof OnlineOrder){
            SetOrdersTable((OnlineOrder) event.getMessage().getObject());
        }else  if (event.getMessage().getObject() instanceof OfflineOrder){
            SetOrdersTable((OfflineOrder) event.getMessage().getObject());
        }
        else{
            ordersTable.getItems().clear();
        }

    }
    @FXML
    void SendAmountChange(ActionEvent event) {
        String subID = subToChangeIDtxt.getText();
        String newPrice = newAmountTxt.getText();

        if (!isAmountValid()) {
            errorMsgArea.setVisible(true);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
        }
        else {
            errorMsgArea.setVisible(false);
            subToChangeIDtxt.clear();
            newAmountTxt.clear();
            switch (Integer.parseInt(subID)) {
                case 3 -> ChangedPCresult.setRegularSubHours(Double.parseDouble(newPrice));
                case 4 -> ChangedPCresult.setMultipleCarRegularSubHours(Double.parseDouble(newPrice));
                case 5 -> ChangedPCresult.setFullSubHours(Double.parseDouble(newPrice));
                default -> System.out.println("error");

            }
            fillPricingChart(ChangedPCresult);
            System.out.println("was here ammount change");

        }
    }


    @Subscribe
    public void setSubChartDataFromServer(SubscriptionsChartResults event) {
        PCresult = (PricingChart) event.getMessage().getObject();
        ChangedPCresult=new PricingChart(PCresult);
        fillPricingChart(ChangedPCresult);
    }

    private void fillPricingChart(PricingChart PCresult) {
        runLater(()->{
            subTable.getItems().clear();
            ObservableList<SubscriptionChartModel> tmp = FXCollections.observableArrayList();
            double rate;
            double[] prices = {PCresult.getKioskPrice(), PCresult.getOrderBeforeHandPrice(), PCresult.getRegularSubHours()
                    , PCresult.getMultipleCarRegularSubHours(), PCresult.getFullSubHours()};
            String[] names = {"Kiosk", "OrderBeforeHand", "RegularSubscription", "RegularSubscriptionMultipl", "FullSubscription"};
            int[] ids = {1, 2, 2, 2, 2};
            for (int i = 0; i < 5; i++) {
                if (i < 2)
                    tmp.add(new SubscriptionChartModel(i + 1, names[i], prices[i], 1, prices[i]));
                else
                    tmp.add(new SubscriptionChartModel(i + 1, names[i], prices[i] * prices[1], (int) Double.parseDouble(String.valueOf(prices[i])), prices[i] * prices[1]));

            }
            subIDcolumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
            subTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
            subPriceColumn.setCellValueFactory(new PropertyValueFactory<>("HourlyPrice"));
            hoursInMonthColumn.setCellValueFactory(new PropertyValueFactory<>("HoursInMonth"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Total"));
            subTable.setItems(tmp);
        });
    }

    // new handlers for the pricing chart
    @FXML
    void resetPricingChart(ActionEvent event) {
        ChangedPCresult=new PricingChart(PCresult);
        fillPricingChart(ChangedPCresult);
    }
    @Subscribe
    public void getSuccessFromServer(UpdateMessageEvent event) {
        runLater(()->{Notifications notificationBuilder = Notifications.create()
                .title("Success")
                .text("Your Request Has Been Sent To The Manager")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER);
            notificationBuilder.showConfirm();});
    }

    @FXML
    void sendPricingChartRequest(ActionEvent event) {
        if(ChangedPCresult.equals(PCresult)){
            return;
        }
        else{
            try {


                Message msg = new Message("#RequestChangingPCResult", ChangedPCresult);
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @FXML
    void ChangeView(ActionEvent event) {
        if (event.getSource() == HandleComplaintTab)
        {
            handleComplaints.setVisible(true);
            handleComplaint.toFront();
            SubmitChanges.setVisible(false);
            
        }
        else if (event.getSource() == RequestChangeOfPrice)
        {
            handleComplaints.setVisible(false);
            SubmitChanges.setVisible(true);
            SubmitChanges.toFront();
        }
        else {
            handleComplaints.setVisible(true);
            handleComplaint.toFront();
            SubmitChanges.setVisible(false);
        }
    }

// Handle the complaints table and stuff

    @FXML
    void onBack(ActionEvent event) throws Exception {
//        SimpleChatClient.setRoot();
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot("CustomerServiceEmployeeScreen");
    }

    @FXML
    void onSave(ActionEvent event) throws Exception {
        if(checkIfSelected()){
            String out="";
            if(complaints.getSelectionModel().getSelectedItem().getText()!=null){
                if (complaints.getSelectionModel().getSelectedItem().getParkingLot()!=null)
                    out+="Parking lot: "+complaints.getSelectionModel().getSelectedItem().getParkingLot().getId()+"\n";
                if (complaints.getSelectionModel().getSelectedItem().getCustomer()!=null)
                    out+="Customer: ID "+complaints.getSelectionModel().getSelectedItem().getCustomer().getId()+"\n";
                if (complaints.getSelectionModel().getSelectedItem().getOrderSubKioskID()!=null){
                    out+="Transaction: ID "+complaints.getSelectionModel().getSelectedItem().getOrderSubKioskID()+"\n";
                    String[] are=complaints.getSelectionModel().getSelectedItem().getOrderSubKioskID().split(" ");
                    Message msg=new Message("#getSubOrder",Integer.parseInt(are[0]));
                    sendMessagesToServer(msg);
                }
                out+=complaints.getSelectionModel().getSelectedItem().getText();
                Content.setText(out);

            }
            else
                Content.setText("Complaint is empty");
        }
    }
    private void SetOrdersTable(AbstractOrder order) {
        if (ordersTable != null && ordersTable.getItems() != null) {
            ordersTable.getItems().clear();
        }
        orderID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderLicense.setCellValueFactory(date ->new SimpleObjectProperty<>(date.getValue().getCar().getCarNum()));
        orderParchaseDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderPLotID.setCellValueFactory(data -> new SimpleObjectProperty<>(Integer.toString(data.getValue().getParkingLotID().getId())));
        orderPricePaid.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFullOrderValue()));
        ordersTable.getItems().add(order);
    }
    @FXML
    void onLogout(ActionEvent event) throws Exception {
        EventBus.getDefault().unregister(this);
        SimpleChatClient.setRoot("LogInScreen");
        Message message = new Message("#LogOut");
        SimpleClient.getClient().sendToServer(message);
    }

    @FXML
    void onHandleComplaint(ActionEvent event) throws Exception {
        // if not complaint selected from the database
        if (complaints.getSelectionModel() == null || complaints.getSelectionModel().getSelectedItem() == null) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please choose a complaint");
            return;
        }

        // get the data from the table
        messageLabel.setText("");
        getComplaintId = complaints.getSelectionModel().getSelectedItem().getId();
        getUserId = complaints.getSelectionModel().getSelectedItem().getCustomer().getId();
        getParkingLotId = complaints.getSelectionModel().getSelectedItem().getParkingLot()!=null?
                complaints.getSelectionModel().getSelectedItem().getParkingLot().getId():-1;
        selected = complaints.getSelectionModel().getSelectedItem();


        // set the button to be useable
        closeComplaint.setDisable(false);
        fullRefund.setDisable(false);
        refundByAmountBtn.setDisable(false);
        amount.setEditable(true);
    }

    @FXML
    void onCloseComplaint(ActionEvent event) throws Exception {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close complaint");
        alert.setHeaderText("Complaint closed");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            sendMessagesToServer("#CloseComplaint&" + selected.getId() + "&" + selected.getCustomer().getId());

            // if the task completed successfully
            if (isRemoved) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Complaint closed");
                isRemoved = false;
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Could not close complaint");
            }

            // update the table
            observableList.remove(selected);
            updateTable(true);
        }

        // close the complaint


        // set the buttons to be not useable
        closeComplaint.setDisable(true);
        fullRefund.setDisable(true);
        refundByAmountBtn.setDisable(true);
        amount.clear();
        amount.setEditable(false);


    }


    @FXML
    void onFullRefund(ActionEvent event) throws Exception {
//        Check first if we have a selected compliant.
        if(checkIfSelected()) {

            sendMessagesToServer("#CloseComplaintWithFullRefund&"+ complaintId + "&"  + selected.getCustomer().getId());
            // The refund was done successfully.
            if (isRemoved) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Order refunded");
                isRemoved = false;
                observableList.remove(selected);
                updateTable(true);
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Could not refund order");
            }

            // set the buttons to be not useable
            closeComplaint.setDisable(true);
            fullRefund.setDisable(true);
            refundByAmountBtn.setDisable(true);
            amount.clear();
            amount.setEditable(false);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Close complaint");
            alert.setHeaderText("Complaint was closed, and client got a message for getting full refund.");
            alert.showAndWait();
        }
    }

    @FXML
    void onRefundByAmount(ActionEvent event) throws Exception {
        System.out.println("clicked On refund by ammount");
        // if the amount to refund is empty
        complaints.requestFocus();
        if(checkIfSelected()) {
            System.out.println("clicked On handle complaint and entered");
            if (amount.getText().isBlank()) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Please enter amount to refund!");
                return;
            }
            try {
                Double.parseDouble(amount.getText());
            }catch (NumberFormatException e){
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Please enter a valid amount to refund!");
            }
            // The refund was done successfully.
            sendMessagesToServer("#CloseComplaintWithRefund&" +complaintId + "&" +selected.getCustomer().getId() + "&" + amount.getText());
            if (isRemoved) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Order refunded");
                isRemoved = false;
                observableList.remove(selected);
                updateTable(true);
            } else {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Could not refund order");
            }

            // set the buttons to be not useable
            closeComplaint.setDisable(true);
            fullRefund.setDisable(true);
            refundByAmountBtn.setDisable(true);
            amount.clear();
            amount.setEditable(false);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Close complaint");
            alert.setHeaderText("Complaint was closed, and client got a message for getting full refund.");
            alert.showAndWait();
        }    }

    public void initialize() {
        // TODO: 13/01/2023 Need to change the hierarchy of Users in our entities ASAP.
        try {
            System.out.println("init");
            EventBus.getDefault().register(this);
//            userOptBtn.setText("Hello, " + ((CustomerServiceEmployee) SimpleChatClient.getUser()).getFirstName());
            userOptBtn.setText("Hello, " + "Boy");
            sendMessagesToServer("#GetAllCompliants");
            sendMessagesToServer("#getPricingChart");
            System.out.println("Send to server");
            // update table values
        } catch (Exception e){

        }
    }

    @FXML
    void showClosedCompliants(ActionEvent event) throws InterruptedException {
        sendMessagesToServer("#GetAllCompliantsForClose");

    }

    @Subscribe
    public void getCompliants(CompliantsSubscriber event) throws InterruptedException {
        System.out.println("Was here");
        String messegeContent = event.getMessage().getMessage();
        if(messegeContent.startsWith("#GetAllCompliants")){
            System.out.println(event.getMessage().getObject());
            observableList = FXCollections.observableArrayList((ArrayList<Complaint>)event.getMessage().getObject());
            if(messegeContent.contains("Close")){
                updateTheTable();
            }else {
                updateTable(true);
            }
            return;
        }
        if(messegeContent.startsWith("#CloseComplaint")){
            isRemoved = true;
        }

    }
    private void updateTheTable() throws InterruptedException {
        runLater(() -> {
            handleComplaint.setDisable(!handleComplaint.isDisabled());
            if(closedCompliants.getText().equals("Closed Complaint")){
                System.out.println("here1");
                closedCompliants.setText("Opened Complaints");
                updateTable(false);
            }else{
                closedCompliants.setText("Closed Complaint");
                updateTable(true);
            }
        });
    }

    private void updateTable(Boolean showOpened) {
        if(complaints != null && complaints.getItems() != null) {
            complaints.getItems().clear();
        }
        complaintId.setCellValueFactory(new PropertyValueFactory<>("id"));
        userId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCustomer().getId()));
        pLotIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLot()!=null?data.getValue().getParkingLot().getId():-1));
        complaintDescription.setCellValueFactory(new PropertyValueFactory<>("text"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        lastDateCol.setCellValueFactory(new PropertyValueFactory<>("durationToAnswer"));
        orderID.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOrderSubKioskID()));
        if(showOpened) {
            observableList.stream().filter(complaint -> complaint.isActive()).
                    forEach(complaints.getItems()::add);
        }else{
            observableList.stream().filter(complaint -> !complaint.isActive()).
                    forEach(complaints.getItems()::add);
        }
//        complaints.setItems(observableList);
    }
    private boolean checkIfSelected(){
        if (complaints.getSelectionModel() == null || complaints.getSelectionModel().getSelectedItem() == null) {
//            messageLabel.setTextFill(Color.RED);
//            messageLabel.setText("Please choose a complaint");
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("Please choose a complaint")
                    .graphic(null)
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.CENTER);
            notificationBuilder.showError();
            return false;
        }
        System.out.println();
        return true;
    }
    void sendMessagesToServer(String request){
        try {
            // Check if the connection with the server is alive.
            Message message = new Message(request);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText(Constants.INTERNAL_ERROR.getMessage());
            e.printStackTrace();
        }

    }void sendMessagesToServer(Message request){
        try {
            // Check if the connection with the server is alive.
            SimpleClient.getClient().sendToServer(request);
        } catch (IOException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText(Constants.INTERNAL_ERROR.getMessage());
            e.printStackTrace();
        }

    }

}