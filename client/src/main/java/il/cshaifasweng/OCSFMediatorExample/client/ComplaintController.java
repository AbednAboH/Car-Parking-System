package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Customers.Customer;
import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.models.ParkingLotModel;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;


public class ComplaintController {

    @FXML
    private Label ComplaintID;

    @FXML
    private ComboBox<String> ComplaintSubject;

    @FXML
    private TextField LastName;

    @FXML
    private ComboBox<String> Ordersubscription;

    @FXML
    private TextArea complaintBody=new TextArea();

    @FXML
    private TextField customerID;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField orderSubIdText;

    @FXML
    private ComboBox<String> orderSubscriptionBox;

    @FXML
    private ComboBox<Integer> parkingLot;

    @FXML
    private Button submitComplaint;
    @FXML
    @Subscribe
    void getOrder(ActionEvent event) {

    }


    @FXML
    void submitComplaintAction(ActionEvent event) {

    }

    @FXML
    void typeOfSubscription(ActionEvent event) {
        if (Ordersubscription.getValue()!=null&&Ordersubscription.getValue().equals("Kiosk")){
            orderSubIdText.setDisable(true);
            orderSubscriptionBox.setDisable(true);
        }
        else{
            if (SimpleChatClient.getUser() != null) {
                orderSubIdText.setDisable(true);
                orderSubscriptionBox.setDisable(false);
            } else {
                orderSubIdText.setDisable(false);
                orderSubscriptionBox.setDisable(true);
            }
        }


    }



    @FXML
    void initialize(){
        EventBus.getDefault().register(this);

        try {
            // Check if the connection with the server is alive.
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
            RegisteredCustomer user=(RegisteredCustomer) SimpleChatClient.getUser();
            if (user!=null){

                firstName.setText(user.getFirstName());
                LastName.setText(user.getLastName());
                email.setText(user.getEmail());
                customerID.setText(Integer.toString(user.getId()));

                message.setMessage("#getAllOrders");
                SimpleClient.getClient().sendToServer(message);

            }
            ComplaintSubject.getItems().add("Complaint about an employee");
            ComplaintSubject.getItems().add("Complaint about costumer service");
            ComplaintSubject.getItems().add("Complaint about a parking lot");
            ComplaintSubject.getItems().add("I can't get my car out/in the parking lot");
            ComplaintSubject.getItems().add("Problem with my order");
            ComplaintSubject.getItems().add("Problem with my subscription");
            ComplaintSubject.getItems().add("Other");

            Ordersubscription.getItems().add("Kiosk");
            Ordersubscription.getItems().add("Order");
            Ordersubscription.getItems().add("Subscription");
            Ordersubscription.setDisable(true);
            orderSubIdText.setDisable(true);
            orderSubscriptionBox.setDisable(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void subjectChange(ActionEvent event) {
        String subject=ComplaintSubject.getValue();
        switch(subject){
            case "Complaint about an employee":
            case "Complaint about a parking lot":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(true);
                orderSubIdText.setDisable(true);
                orderSubscriptionBox.setDisable(true);
                break;
            case "Complaint about costumer service":
                parkingLot.setDisable(true);
                Ordersubscription.setDisable(true);
                orderSubIdText.setDisable(true);
                orderSubscriptionBox.setDisable(true);
                break;
            case  "I can't get my car out/in the parking lot":
            case  "Problem with my subscription":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(false);
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }

                break;
            case "Problem with my order":
                parkingLot.setDisable(false);
                Ordersubscription.setDisable(false);
                Ordersubscription.setValue("Order");
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }
                break;
            case "other":
                parkingLot.setDisable(true);
                Ordersubscription.setDisable(true);
                if (SimpleChatClient.getUser() != null) {
                    orderSubIdText.setDisable(true);
                    orderSubscriptionBox.setDisable(false);
                } else {
                    orderSubIdText.setDisable(false);
                    orderSubscriptionBox.setDisable(true);
                }
                break;

        }
        if (parkingLot.isDisable())
            parkingLot.getSelectionModel().clearSelection();
        if (Ordersubscription.isDisable())
            Ordersubscription.getSelectionModel().clearSelection();
        if(orderSubscriptionBox.isDisable())
            orderSubIdText.clear();
        if(orderSubscriptionBox.isDisable())
          orderSubscriptionBox.getSelectionModel().clearSelection();


    }
    @FXML
    @Subscribe
    public void setParkingLotDataFromServer(ParkingLotResults event) {
        List<ParkingLot> PLresults = (List<ParkingLot>) event.getMessage().getObject();
        for (ParkingLot PL :PLresults)
            parkingLot.getItems().add(PL.getId());
    }
    @FXML
    @Subscribe
    public void getAllOrders(OrderHistoryResponse event) {
        List<Order> orders = (List<Order>) event.getMessage().getObject();
        System.out.println("Washere");
        for (Order or :orders)
            orderSubscriptionBox.getItems().add(or.toString());
    }

}
