package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.Scene;
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

import java.awt.event.ActionEvent;

public class ParkingLotStatus {
    @FXML
    void showPlot(ActionEvent event) {

    }
    private final int[][] matrix = {
            {0, 1, 2},
            {2, 0, 1},
            {1, 2, 0}
    };

    private final int rows = matrix.length;
    private final int cols = matrix[0].length;
    @FXML
    public void ShowFloor() {
            Stage popUp = new Stage();
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("matrix");

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Label titleLabel = new Label("Dynamic Matrix");
            titleLabel.setStyle("-fx-font-size: 18pt");
            titleLabel.setAlignment(Pos.CENTER);
            grid.add(titleLabel, 0, 0, 2, 1);

            VBox matrixContainer = new VBox(10);
            matrixContainer.setAlignment(Pos.CENTER);
            for (int i = 0; i < rows; i++) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER);
                for (int j = 0; j < cols; j++) {
                    int squareSize = 50;
                    Rectangle rect = new Rectangle(squareSize, squareSize);
                    switch (matrix[i][j]) {
                        case 0:
                            rect.setFill(Color.BLUE);
                            break;
                        case 1:
                            rect.setFill(Color.BLACK);
                            break;
                        case 2:
                            rect.setFill(Color.RED);
                            break;
                    }
                    row.getChildren().add(rect);
                }
                matrixContainer.getChildren().add(row);
            }
            grid.add(matrixContainer, 0, 1, 2, 1);

            Button closePopup = new Button("Close");
            closePopup.setOnAction(e -> popUp.close());
            HBox buttonContainer = new HBox(10);
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
            buttonContainer.getChildren().add(closePopup);
            grid.add(buttonContainer, 1, 2);

            Scene popUpScene = new Scene(grid);
            popUp.setScene(popUpScene);
            popUp.show();



    }

    @FXML
    void initialize() {

        ShowFloor();
    }

    public void showPlot(javafx.event.ActionEvent actionEvent) {
        
    }
}
