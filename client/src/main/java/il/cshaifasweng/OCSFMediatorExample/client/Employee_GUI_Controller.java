package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingSpotsSubscriber;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TableColumn<ParkingSpot, String> statusColumn;

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
    private Label errorMsg;

    @FXML
    private Button markFaultyBtn;

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
        if (Table.getSelectionModel().getSelectedItem() != null) {
            errorMsg.setVisible(false);
            Table.getSelectionModel().getSelectedItem().setOccupied(true);
            System.out.println(Table.getSelectionModel().getSelectedItem());
            Message message = new Message("#SetParkingSpots");
            message.setObject(Table.getSelectionModel().getSelectedItem());
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMsg.setVisible(true);
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
            message.setMessage("#GetParkingSpots");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetParkingSpotsFromServer(ParkingSpotsSubscriber event) {
        System.out.println("Got response from server");
        pSpots = (List<ParkingSpot>) event.getMessage().getObject();
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        depth.setCellValueFactory(new PropertyValueFactory<>("depth"));
        available.setCellValueFactory(data -> new SimpleObjectProperty<>(!data.getValue().isOccupied()));
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(getSpotStatus(data.getValue())));
        pSpots.forEach(Table.getItems()::add);
        Table.refresh();
        System.out.println("Got Parking Spots");
    }

    private String getSpotStatus(ParkingSpot ps) {
        if (ps.free())
            return "Free";
        if (ps.isFaulty())
            return "Faulty";
        if (ps.isOccupied())
            return "Occupied";
        return "Saved";
    }

    @FXML
    void markSpotAsFaulty(ActionEvent event) {
        if (Table.getSelectionModel().getSelectedItem() != null) {
            errorMsg.setVisible(false);
            Table.getSelectionModel().getSelectedItem().setFaulty(true);
            System.out.println(Table.getSelectionModel().getSelectedItem());
            Message message = new Message("#SetParkingSpots");
            message.setObject(Table.getSelectionModel().getSelectedItem());
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMsg.setVisible(true);
        }
    }


    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        errorMsg.setVisible(false);
        Message message = new Message("#GetParkingSpots");
        SimpleClient.getClient().sendToServer(message);
    }

}
















