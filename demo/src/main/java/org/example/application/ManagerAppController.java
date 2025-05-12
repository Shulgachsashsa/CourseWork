package org.example.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonWorkWithClothes;

    @FXML
    private Button buttonWorkWithRequests;

    @FXML
    void initialize() {
        assert buttonExit != null : "fx:id=\"buttonExit\" was not injected: check your FXML file 'manager-view.fxml'.";
        assert buttonWorkWithClothes != null : "fx:id=\"buttonWorkWithClothes\" was not injected: check your FXML file 'manager-view.fxml'.";
        assert buttonWorkWithRequests != null : "fx:id=\"buttonWorkWithRequests\" was not injected: check your FXML file 'manager-view.fxml'.";
        buttonExit.setOnAction(event -> loadMainMenu());
        buttonWorkWithRequests.setOnAction(event -> loadMenuWorkWithRequests());
    }

    private void loadMainMenu() {
        try {
            Stage currentStage = (Stage) buttonExit.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Главное меню");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное меню!");
            e.printStackTrace();
        }
    }

    private void loadMenuWorkWithRequests() {
        try {
            Stage currentStage = (Stage) buttonWorkWithRequests.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager-view-create-req.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Работа с запросами");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню с запросами!");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
