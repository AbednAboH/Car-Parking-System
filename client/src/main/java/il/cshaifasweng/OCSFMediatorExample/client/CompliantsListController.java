package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.LogInEntities.Employees.CustomerServiceEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.CompliantsSubscriber;
import il.cshaifasweng.customerCatalogEntities.Complaint;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;


public class CompliantsListController {

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

    private Complaint selected;

    private int getComplaintId;

    private int getUserId;

    private int getParkingLotId;
    private boolean isRemoved;


    @FXML
    void onBack(ActionEvent event) throws Exception {
        // TODO: 13/01/2023 Return to the previous screen.
//        SimpleChatClient.setRoot();
    }

    @FXML
    void onSave(ActionEvent event) throws Exception {
        // TODO: 13/01/2023 Save the data changes to the server. 
    }

    @FXML
    void onLogout(ActionEvent event) throws Exception {
        SimpleChatClient.setRoot("LogInScreen");
        // TODO: 22/01/2023 disconnect from server 
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
        getParkingLotId = complaints.getSelectionModel().getSelectedItem().getParkingLot().getId();
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
        if(!checkIfSelected()) {

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
        // if the amount to refund is empty
        if(!checkIfSelected()) {

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
        Platform.runLater(() -> {
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

    /**
     * update the table values
     */
    private void updateTable(Boolean showOpened) {
        if(complaints != null && complaints.getItems() != null) {
            complaints.getItems().clear();
        }
        complaintId.setCellValueFactory(new PropertyValueFactory<>("id"));
        userId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCustomer().getId()));
        pLotIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getParkingLot().getId()));
        userId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        pLotIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        complaintDescription.setCellValueFactory(new PropertyValueFactory<>("text"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        lastDateCol.setCellValueFactory(new PropertyValueFactory<>("durationToAnswer"));
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
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please choose a complaint");
            return false;
        }
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

    }

}