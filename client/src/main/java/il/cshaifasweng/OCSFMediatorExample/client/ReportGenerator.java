package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.OrderHistoryResponse;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.UpdateMessageEvent;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.customerCatalogEntities.OnlineOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.bytebuddy.asm.Advice;
import org.controlsfx.control.Notifications;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static javafx.application.Platform.runLater;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> reportType;

    @FXML
    private Button generateButton;

    @FXML
    private Button backBtn;

    private HashMap<String, Integer> typesMap = new HashMap<>();
    private String currentType;


    class ReportRequest {
        private LocalDate startDate;
        private LocalDate endDate;
        private String reportType;

        public ReportRequest(LocalDate startDate, LocalDate endDate, String reportType) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.reportType = reportType;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }

    @FXML
    public void initialize() {
        try {
            EventBus.getDefault().register(this);
            typesMap.put("Parking Lots", 1);
            typesMap.put("Complaints", 2);
            typesMap.put("Faulty", 3);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getDateRanges(ActionEvent event) {

    }


    @FXML
    void back(ActionEvent event) {
        try {
            SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
            EventBus.getDefault().unregister(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateButtonAction(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            Notifications notifications = Notifications.create()
                    .title("Invalid Data!")
                    .text("Please choose a valid dates.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notifications.showError();
            return;
        }
        if (currentType == null) {
            Notifications notifications = Notifications.create()
                    .title("Invalid Data!")
                    .text("Please choose a report type.")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notifications.showError();
            return;
        }
        requestToGenerateAReport(startDate, endDate);
    }

    private void requestToGenerateAReport(LocalDate startDate, LocalDate endDate) {
        try {
            ReportRequest reportRequest = new ReportRequest(startDate,endDate,currentType);
            Message message = new Message("#generateReport", reportRequest);
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void GetResponseFromServer(UpdateMessageEvent event) {
        runLater(()->{
            Notifications notifications = Notifications.create()
                    .title("Result")
                    .text((String)event.getMessage().getObject())
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.CENTER);
            notifications.show();
        });
    }

    public void addReportType() {
        reportType.setItems(FXCollections.observableList(typesMap.keySet().stream().map(key -> key.toString()).collect(Collectors.toList())));
        reportType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentType = newValue;
        });
    }

    public void getDates() {

    }


}


