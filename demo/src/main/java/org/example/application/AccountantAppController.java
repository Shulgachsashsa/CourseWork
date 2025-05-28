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

public class AccountantAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonCheckHistoryBudget;

    @FXML
    private Button buttonCheckHistoryReq;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonWorkWithBudget;

    @FXML
    private Button buttonWorkWithRequests;

    @FXML
    void initialize() {
        assert buttonCheckHistoryBudget != null : "fx:id=\"buttonCheckHistoryBudget\" was not injected: check your FXML file 'accountant-view.fxml'.";
        assert buttonCheckHistoryReq != null : "fx:id=\"buttonCheckHistoryReq\" was not injected: check your FXML file 'accountant-view.fxml'.";
        assert buttonExit != null : "fx:id=\"buttonExit\" was not injected: check your FXML file 'accountant-view.fxml'.";
        assert buttonWorkWithBudget != null : "fx:id=\"buttonWorkWithBudget\" was not injected: check your FXML file 'accountant-view.fxml'.";
        assert buttonWorkWithRequests != null : "fx:id=\"buttonWorkWithRequests\" was not injected: check your FXML file 'accountant-view.fxml'.";
        buttonExit.setOnAction(actionEvent -> loadMainMenu());
        buttonWorkWithRequests.setOnAction(actionEvent -> loadAppWorkWithRequests());
        buttonCheckHistoryBudget.setOnAction(actionEvent -> loadAppCheckHistoryBudget());
        buttonWorkWithBudget.setOnAction(actionEvent -> loadAppWorkWithBudget());
        buttonCheckHistoryReq.setOnAction(actionEvent -> loadAppCheckHistoryRequests());
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

    private void loadAppWorkWithRequests() {
        try {
            Stage currentStage = (Stage) buttonWorkWithRequests.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-work-req-view.fxml"));
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

    private void loadAppWorkWithBudget() {
        try {
            Stage currentStage = (Stage) buttonWorkWithBudget.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-work-budget-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Работа с бюджетом");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню с запросами!");
            e.printStackTrace();
        }
    }

    private void loadAppCheckHistoryBudget() {
        try {
            Stage currentStage = (Stage) buttonCheckHistoryBudget.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-history-budget-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("История изменения бюджета");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню с запросами!");
            e.printStackTrace();
        }
    }

    private void loadAppCheckHistoryRequests() {
        try {
            Stage currentStage = (Stage) buttonCheckHistoryReq.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-history-req-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("История запросов");
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
