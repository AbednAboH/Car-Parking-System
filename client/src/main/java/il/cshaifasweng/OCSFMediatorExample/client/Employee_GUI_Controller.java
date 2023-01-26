package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingSpotsSubscriber;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.List;

public class Employee_GUI_Controller {
    private List<ParkingSpot> pSpots;
    @FXML
    private Button DirectToAvailblePark;

    @FXML
    private Button SaveSpot;

    @FXML
    private TableView<ParkingSpot> Table;

    @FXML
    private TableColumn<ParkingSpot, Boolean> available;

    @FXML
    private TableColumn<ParkingSpot, Integer> floor;

    @FXML
    private ComboBox<Integer> floorLabel;

    @FXML
    private TableColumn<ParkingSpot, Integer> depth;

    @FXML
    private ComboBox<Integer> depthLabel;

    @FXML
    private Button intialize;

    @FXML
    private TableColumn<ParkingSpot, Integer> row;

    @FXML
    private ComboBox<Integer> rowLabel;

    @FXML
    private Button userOptBtn;

    @FXML
    private Button userOptBtn1;

    @FXML
    void DirectToAvailblePark(ActionEvent event) {
        try {
            Message message = new Message("#DirectToAvailblePark");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void SaveSpot(ActionEvent event) {
    int index;
     if (Table.getSelectionModel().getSelectedItem() != null) {
            Table.getSelectionModel().getSelectedItem().setOccupied(true);
            System.out.println(Table.getSelectionModel().getSelectedItem());
            Message message = new Message("#SetParkingSpots");
            message.setObject( Table.getSelectionModel().getSelectedItem());
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     else{
         // TODO: 26/01/2023 add alert to user that he didnt choose a spot
         System.out.println("No spot was chosen");
     }
    }

    @FXML
    void Table(ActionEvent event) {
//TODO: GET TABLE
        Message message = new Message("#PARKINGLOTTABLE");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initializeBtn(ActionEvent event) {
        Message message = new Message("#intializeParkingLot");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetParkingSpotsFromServer(ParkingSpotsSubscriber event){
        System.out.println("Got response from server");
        pSpots=(List<ParkingSpot>)event.getMessage().getObject();
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        depth.setCellValueFactory(new PropertyValueFactory<>("depth"));
        available.setCellValueFactory(data->new SimpleObjectProperty<>(!data.getValue().isOccupied()));
        pSpots.forEach(Table.getItems()::add);
        Table.refresh();
        System.out.println("Got Parking Spots");

    }
    @FXML
    void initialize()throws Exception{
        EventBus.getDefault().register(this);
        Message message = new Message("#GetParkingSpots");
        SimpleClient.getClient().sendToServer(message);




    }

}
















