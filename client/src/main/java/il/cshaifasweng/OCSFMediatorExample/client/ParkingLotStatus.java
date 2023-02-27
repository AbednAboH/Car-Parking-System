package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.Message;
import il.cshaifasweng.MoneyRelatedServices.PricingChart;
import il.cshaifasweng.OCSFMediatorExample.client.Subscribers.ParkingLotResults;
import il.cshaifasweng.ParkingLotEntities.ParkingLot;
import il.cshaifasweng.ParkingLotEntities.ParkingSpot;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.Hibernate;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParkingLotStatus {
    // Store the current scene in a member variable
    private Scene currentScene;
    private GridPane grid;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button backButton;

    @FXML
    ChoiceBox<Integer> parkingLotList;

    @FXML
    ChoiceBox<Integer> floorsList;

    @FXML
    Label totalNumberOfSpots;

    @FXML
    void showPlot(ActionEvent event) {

    }
//    private final int[][] matrix = {
//            {0, 1, 2},
//            {2, 0, 1},
//            {1, 2, 0}
//    };

    private int[][] matrix;
    private int rows;
    private int cols;
    private ArrayList<ParkingLot> parkingLots = new ArrayList<>();
    private Map<Integer,ParkingLot> parkingLotIntegerHashMap;
    private ParkingLot currentPLot;
//    @FXML
//    public void ShowFloor() {
//            Stage popUp = new Stage();
//            popUp.initModality(Modality.APPLICATION_MODAL);
//            popUp.setTitle("matrix");
//
//            GridPane grid = new GridPane();
//            grid.setAlignment(Pos.CENTER);
//            grid.setHgap(10);
//            grid.setVgap(10);
//            grid.setPadding(new Insets(25, 25, 25, 25));
//
//            Label titleLabel = new Label("Dynamic Matrix");
//            titleLabel.setStyle("-fx-font-size: 18pt");
//            titleLabel.setAlignment(Pos.CENTER);
//            grid.add(titleLabel, 0, 0, 2, 1);
//
//            VBox matrixContainer = new VBox(10);
//            matrixContainer.setAlignment(Pos.CENTER);
//            for (int i = 0; i < rows; i++) {
//                HBox row = new HBox(10);
//                row.setAlignment(Pos.CENTER);
//                for (int j = 0; j < cols; j++) {
//                    int squareSize = 50;
//                    Rectangle rect = new Rectangle(squareSize, squareSize);
//                    switch (matrix[i][j]) {
//                        case 0:
//                            rect.setFill(Color.BLUE);
//                            break;
//                        case 1:
//                            rect.setFill(Color.BLACK);
//                            break;
//                        case 2:
//                            rect.setFill(Color.RED);
//                            break;
//                    }
//                    row.getChildren().add(rect);
//                }
//                matrixContainer.getChildren().add(row);
//            }
//            grid.add(matrixContainer, 0, 1, 2, 1);
//
//            Button closePopup = new Button("Close");
//            closePopup.setOnAction(e -> popUp.close());
//            HBox buttonContainer = new HBox(10);
//            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
//            buttonContainer.getChildren().add(closePopup);
//            grid.add(buttonContainer, 1, 2);
//
//            Scene popUpScene = new Scene(grid);
//            popUp.setScene(popUpScene);
//            popUp.show();
//
//
//
//    }

    @FXML
    public void ShowFloor(ParkingLot parkingLot,int floorNumber) {
        String resources = "/il/cshaifasweng/OCSFMediatorExample/client/icons/car-";
        System.out.println(floorNumber);
        fillMatrix(parkingLot,floorNumber);
        System.out.println(grid);
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setMaxSize(300,300);
        rows = matrix.length;
        cols = matrix[0].length;
        Label titleLabel = new Label("Dynamic Matrix");
        titleLabel.setStyle("-fx-font-size: 18pt");
        titleLabel.setAlignment(Pos.CENTER);
        grid.add(titleLabel, 0, 0, 2, 1);
        totalNumberOfSpots.setText(String.valueOf(rows*cols));
        VBox matrixContainer = new VBox(10);
        matrixContainer.setAlignment(Pos.CENTER);
        for (int i = 0; i < rows; i++) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);
            for (int j = 0; j < cols; j++) {
                int squareSize = 50;
                String currResource = resources;
                switch (matrix[i][j]) {
                    case 0:
                        currResource = currResource + "green";
                        break;
                    case 1:
                        currResource = currResource + "yellow";
                        break;
                    case 2:
                        currResource = currResource + "black";
                        break;
                    case 3:
                        currResource = currResource + "red";
                        break;
                }
                Image image = new Image(getClass().getResourceAsStream(currResource+".png"), squareSize, squareSize, false, false);
                ImageView imageView = new ImageView(image);
                row.getChildren().add(imageView);
            }
            matrixContainer.getChildren().add(row);
        }
        grid.add(matrixContainer, 0, 1, 2, 1);

        Button closePopup = new Button("Close");
        closePopup.setTextFill(Color.WHITE);
        closePopup.setOnAction(e -> anchorPane.getChildren().remove(grid));
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.getChildren().add(closePopup);
        grid.add(buttonContainer, 1, 2);

        anchorPane.getChildren().add(grid);
        AnchorPane.setTopAnchor(grid, 0.0);
        AnchorPane.setLeftAnchor(grid, 0.0);
        AnchorPane.setRightAnchor(grid, 0.0);
        AnchorPane.setBottomAnchor(grid, 0.0);
    }

    private void fillMatrix(ParkingLot parkingLot,int floorNumber) {
        List<ParkingSpot> spots = parkingLot.getSpots().stream().filter(parkingSpot -> parkingSpot.getFloor() == floorNumber).collect(Collectors.toList());
        matrix = new int[parkingLot.getRowsInEachFloor()][parkingLot.getRowCapacity()]; // rows and cols
        for(ParkingSpot spot : spots){
            int row = spot.getRow();
            int col = spot.getDepth();
            if(spot.isOccupied()){
                matrix[row][col] = 1;
                continue;
            }
            if(spot.isSaved()){
                matrix[row][col] = 2;
                continue;
            }
            if(spot.isFaulty()){
                matrix[row][col] = 3;
                continue;
            }
            matrix[row][col] = 0;
        }
    }


    @FXML
    void initialize() {
        try {
            EventBus.getDefault().register(this);
            Message initMessage = new Message("#intializeParkingLot");
            SimpleClient.getClient().sendToServer(initMessage);
            Thread.sleep(600);
            Message message = new Message("#getAllParkingLots");
            SimpleClient.getClient().sendToServer(message);
            addParkingLotsId();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void SubscriptionEvents(ParkingLotResults event){
        if(event.getMessage() != null) {
            if(event.getMessage().getMessage().startsWith("#getAllParkingLots")){
                System.out.println("Hello");
                parkingLots = (ArrayList<ParkingLot>) event.getMessage().getObject();
                parkingLotIntegerHashMap = parkingLots.stream().collect(Collectors.toMap(ParkingLot::getId, Function.identity()));
                addParkingLotsId();
            }
        }
    }

    private void addParkingLotsId() {
        System.out.println("Adding");
        parkingLotList.setItems(FXCollections.observableList(parkingLots.stream().map(ParkingLot::getId).collect(Collectors.toList())));
        System.out.println(parkingLots);
        parkingLotList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            ShowFloor(parkingLotIntegerHashMap.get(newValue));
            currentPLot = parkingLotIntegerHashMap.get(newValue);
            List<Integer> floorNumbers = new ArrayList<>();
            for (int i = 0; i < currentPLot.getFloor(); i++) {
                floorNumbers.add(i);
            }
            floorsList.setItems(FXCollections.observableList(floorNumbers));
        });

        floorsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if(grid != null){
                anchorPane.getChildren().remove(grid);
            }
            ShowFloor(currentPLot,newValue);
        });
    }

    public void goBack(javafx.event.ActionEvent event) throws IOException {
        SimpleChatClient.setRoot(SimpleChatClient.getPreviousScreen());
    }
}
