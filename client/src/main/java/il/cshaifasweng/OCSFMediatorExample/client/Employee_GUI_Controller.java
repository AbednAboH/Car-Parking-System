package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MySQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    private TextField colLabel;

    @FXML
    private TableColumn<?, ?> depth;

    @FXML
    private TextField depthLabel;

    @FXML
    private Button intialize;

    @FXML
    private TableColumn<?, ?> row;

    @FXML
    private TextField rowLabel;

    boolean isAmountValid(String newAmount, int min, int max) {

        if (newAmount.compareTo("") == 0) {
            return false;
        }
        try {
            if (Integer.parseInt(newAmount) < min || Integer.parseInt(newAmount) > max)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


/*
    @FXML
    void SaveSpot(ActionEvent event) {
        String row= rowLabel.getText();
        String col= colLabel.getText();
        String depth= depthLabel.getText();
        // send message to server requesting size of parkilot returns the 0/1/2 row/col/col in that order
        if(isAmountValid(row, 0, ) ==0 ||isAmountValid(col, 0,  )==0  || isAmountValid(depth, 0, )==0 ){
            // print one of the values is out of range!!!

        }




    }*/



    @FXML
    void DirectToAvailblePark(ActionEvent event) {

       try {
            Message message = new Message("#DirectToAvailblePark");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

 /*   @FXML
    void Table(ActionEvent event) {

    }*/

    @FXML
    void intializeBtn(ActionEvent event) {
        try {
            MySQL.initiateParkingLot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
