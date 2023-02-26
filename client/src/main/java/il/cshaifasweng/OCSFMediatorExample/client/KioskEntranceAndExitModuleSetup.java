package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.runLater;

public class KioskEntranceAndExitModuleSetup {
    @FXML
    private TextField employeeID;

    @FXML
    private TextField employeePass;


    @FXML
    private ComboBox<Integer> pLotCombo;
    private List<ParkingLot> plots;
    @Subscribe
    public void onConfirmingCredintials(UpdateMessageEvent message){
        if(message.getMessage().getObject().equals("true")){
            runLater(()->{
                Notifications notificationBuilder = Notifications.create()
                        .title("Success")
                        .text("You have entered the system")
                        .hideAfter(javafx.util.Duration.seconds(5))
                        .position(Pos.CENTER);
                notificationBuilder.showInformation();


                try {
                    for (ParkingLot plot : plots) {
                        if (plot.getId() == pLotCombo.getSelectionModel().getSelectedItem()) {
                            SimpleChatClient.setCurrentKioskID(plot);
                            break;
                        }
                    }
                    System.out.println(SimpleChatClient.getCurrentKioskID().getId());
                    SimpleChatClient.setRoot("EntranceAndExit");
                    EventBus.getDefault().unregister(this);
                    SimpleChatClient.addScreen("KioskEntranceAndExitPageSetup");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }
        else{
            runLater(()->{Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text((String) message.getMessage().getObject())
                    .hideAfter(javafx.util.Duration.seconds(5))
                    .position(Pos.CENTER);
                notificationBuilder.showError();});
        }
    }
    @FXML
    void visitor(ActionEvent event) {
        if (checkInputs()) {
            Message message = new Message();
            message.setMessage("#checkCredentials&" + employeeID.getText() + "&" +
                    employeePass.getText()+"&"+pLotCombo.getSelectionModel().getSelectedItem());
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                Notifications notificationBuilder = Notifications.create()
                        .title("Error")
                        .text("Couldn't connect to server")
                        .hideAfter(javafx.util.Duration.seconds(5))
                        .position(Pos.CENTER);
                notificationBuilder.showError();
            }
        }
        else{
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text(checkInputsWithString())
                    .hideAfter(javafx.util.Duration.seconds(5))
                    .position(Pos.CENTER);
            notificationBuilder.showError();
        }
    }
    public boolean checkInputs(){
        return checkSameFields(employeePass, employeeID, pLotCombo);
    }

    static boolean checkSameFields(TextField employeePass, TextField employeeID, ComboBox<Integer> pLotCombo) {
        boolean input= InputValidator.isValidPassRegular(employeePass.getText()) && InputValidator.isValidNumber(employeeID.getText());
        if(pLotCombo.getSelectionModel()!=null&& pLotCombo.getSelectionModel().getSelectedItem()!=null){
            input= input;
        }
        else{
            input= false;
        }

        return input;
    }

    public String checkInputsWithString(){
        String out="";
        out+=InputValidator.isValidPassRegular(employeePass.getText())?"":"Invalid Password\n";
        out+=pLotCombo.getSelectionModel()!=null&&pLotCombo.getSelectionModel().getSelectedItem()!=null?"":"Invalid Parking Lot\n";
        out+=InputValidator.isValidNumber(employeeID.getText())?"":"Invalid ID\n";

        return out;
    }
    @FXML
    public void initialize() {
        pLotCombo.setDisable(true);
        employeePass.setDisable(true);
        employeeID.setDisable(true);
        EventBus.getDefault().register(this);
        Message message = new Message();
        message.setMessage("#getAllParkingLots");
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            Notifications notificationBuilder = Notifications.create()
                    .title("Error")
                    .text("Couldn't connect to server")
                    .hideAfter(javafx.util.Duration.seconds(5))
                    .position(Pos.CENTER);
            notificationBuilder.showError();
        }
    }
    @Subscribe
    public void onParkingLotsReceived(ParkingLotResults message) {
        plots = (List<ParkingLot>) message.getMessage().getObject();
        List<Integer> plotids=new ArrayList<>();
        runLater(() -> {
            plots.forEach(parkingLot -> {
                plotids.add(parkingLot.getId());
            });
            pLotCombo.getItems().addAll(plotids);
            pLotCombo.setPromptText("Choose parking lot");
            pLotCombo.setDisable(false);
            employeePass.setDisable(false);
            employeeID.setDisable(false);
        });

    }
}
