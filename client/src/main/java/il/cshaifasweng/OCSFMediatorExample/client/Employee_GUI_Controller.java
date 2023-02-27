package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.LogInEntities.Employees.ParkingLotEmployee;
import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.LogoutSubscriber;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingSpotsSubscriber;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.sun.javafx.application.PlatformImpl.runLater;


import javax.persistence.criteria.CriteriaBuilder;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Employee_GUI_Controller {
    private List<ParkingSpot> pSpots;
    @FXML
    private Button DirectToAvailablePark;

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
    private Label userNameLbl;

    @FXML
    private Button faultyBtn;

    @FXML
    private Button markFaultyBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    void DirectToAvailablePark(ActionEvent event) {
        try {
            SimpleChatClient.addScreen("Employee_GUI");
            SimpleChatClient.setRoot("DirectToAvailablePark");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void SaveSpot(ActionEvent event) {
        if (Table.getSelectionModel().getSelectedItem() != null) {
            Table.getSelectionModel().getSelectedItem().ChangeSavedStatus();
            updateSpot();
            initControlsState();
        }
    }

    @FXML
    void markUnmarkFaulty(ActionEvent event) {
        if (Table.getSelectionModel().getSelectedItem() != null) {
            Table.getSelectionModel().getSelectedItem().ChangeFaultyStatus();
            updateSpot();
            initControlsState();
        }
    }

    private void updateSpot() {
        Message message = new Message("#SetParkingSpots");
        message.setObject(Table.getSelectionModel().getSelectedItem());
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSpotStatus(ParkingSpot ps) {
        if (ps.isOccupied())
            return "Occupied";
        if (ps.isSaved())
            return "Saved";
        if (ps.isFaulty())
            return "Faulty";
        return "Free";
    }

    private boolean isAvailable(ParkingSpot ps) {
        return !(ps.isSaved() || ps.isOccupied() || ps.isFaulty());
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
        initControlsState();
        Message message = new Message("#intializeParkingLot");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void GetParkingSpotsFromServer(ParkingSpotsSubscriber event) {
        System.out.println("Got response from server");
        pSpots = (List<ParkingSpot>) event.getMessage().getObject();
        initComboBox();
        pSpots.forEach(ps -> System.out.println(isAvailable(ps)));
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        depth.setCellValueFactory(new PropertyValueFactory<>("depth"));
        available.setCellValueFactory(data -> new SimpleObjectProperty<>(isAvailable(data.getValue())));
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(getSpotStatus(data.getValue())));
        refreshTable();
    }

    private void refreshTable() {
        Platform.runLater(() -> {
            Table.getItems().clear();
            pSpots.forEach(Table.getItems()::add);
            Table.refresh();
            System.out.println("Got Parking Spots");
        });
    }

    @FXML
    void initialize() throws Exception {
        EventBus.getDefault().register(this);
        Message message = new Message("#GetParkingSpots");
        SimpleClient.getClient().sendToServer(message);
        String name = ((ParkingLotEmployee) SimpleChatClient.getUser()).getFirstName();
        userNameLbl.setText("Hello, " + name);
        faultyBtn.setDisable(true);
        SaveSpot.setDisable(true);
    }

    private void initComboBox() {
        pSpots.forEach(spot -> {
            if (rowLabel.getItems().lastIndexOf(spot.getRow()) == -1)
                rowLabel.getItems().add(spot.getRow(), spot.getRow());
            if (floorLabel.getItems().lastIndexOf(spot.getFloor()) == -1)
                floorLabel.getItems().add(spot.getFloor(), spot.getFloor());
            if (depthLabel.getItems().lastIndexOf(spot.getDepth()) == -1)
                depthLabel.getItems().add(spot.getDepth(), spot.getDepth());
        });

    }

    @FXML
    public void spotSelectedTable(javafx.scene.input.MouseEvent mouseEvent) {
        ParkingSpot ps = Table.getSelectionModel().getSelectedItem();
        rowLabel.getSelectionModel().select(ps.getRow());
        floorLabel.getSelectionModel().select(ps.getFloor());
        depthLabel.getSelectionModel().select(ps.getDepth());
        enableButtons();
    }

    @FXML
    void spotSelectedComboBox(ActionEvent event) {
        if (rowLabel.getValue() != null && floorLabel.getValue() != null
                && depthLabel.getValue() != null) {
            Table.getSelectionModel().select(Table.getItems().get(findSpotIndexInTable()));
            enableButtons();
        }
    }

    private int findSpotIndexInTable() {
        int i = 0;
        List<ParkingSpot> parkingSpots = Table.getItems();
        for (ParkingSpot sp :
                parkingSpots) {
            if (rowLabel.getSelectionModel().getSelectedItem() == sp.getRow() &&
                    floorLabel.getSelectionModel().getSelectedItem() == sp.getFloor() &&
                    depthLabel.getSelectionModel().getSelectedItem() == sp.getDepth()) {
                sp.toString();
                return i;
            }
            i++;
        }
        return 0;
    }

    private void enableButtons(){
        faultyBtn.setDisable(true);
        SaveSpot.setDisable(true);
        ParkingSpot ps = Table.getSelectionModel().getSelectedItem();
        if (ps != null) {
            if (!ps.isOccupied()){
                faultyBtn.setDisable(false);
                if (!ps.isFaulty() && !ps.isSaved())
                    SaveSpot.setDisable(false);
            }
        }
    }

    @FXML
    void logOutUser(ActionEvent event) {
        Message msg=new Message("#LogOut");
        try {
            SimpleClient.getClient().sendToServer(msg);
        }
        catch (IOException e) {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("Error while trying to log out, please try again later")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT);
        }
    }

    private void initControlsState() {
        faultyBtn.setDisable(true);
        SaveSpot.setDisable(true);
        rowLabel.getSelectionModel().clearSelection();
        depthLabel.getSelectionModel().clearSelection();
        floorLabel.getSelectionModel().clearSelection();
    }
    @Subscribe
    public void LogOutStatus(LogoutSubscriber event){
        String msg= (String) event.getMessage().getObject();
        System.out.println(msg);
        if(msg.startsWith("Success")){

            try {
                EventBus.getDefault().unregister(this);
                SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            } catch (IOException e) {
                System.out.println("Failed to go back to previous screen");
            }

        }
        else{
            runLater(()->{
                Notifications notificationBuilder;
                notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Error while trying to log out, please try again later")
                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
            });

        }

    }

}
















