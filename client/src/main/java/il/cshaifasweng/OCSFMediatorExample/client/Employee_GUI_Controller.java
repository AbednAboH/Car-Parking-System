package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class Employee_GUI_Controller {

    @FXML
    private Button DirectToAvailblePark;

    @FXML
    private Button SaveSpot;

    @FXML
    private TableView<?> Table;

    @FXML
    private TableColumn<?, ?> available;

    @FXML
    private TableColumn<?, ?> col;

    @FXML
    private ComboBox<?> colLabel;

    @FXML
    private TableColumn<?, ?> depth;

    @FXML
    private ComboBox<?> depthLabel;

    @FXML
    private Button intialize;

    @FXML
    private TableColumn<?, ?> row;

    @FXML
    private ComboBox<?> rowLabel;

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
        String row= (String) rowLabel.getValue();
        String col=(String) colLabel.getValue();
        String depth=(String) depthLabel.getValue();
        Message message= new Message("#SaveSpot"+"$"+row+"$"+col+"$"+depth);
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
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
        Message message = new Message("#DirectToAvailblePark");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
















