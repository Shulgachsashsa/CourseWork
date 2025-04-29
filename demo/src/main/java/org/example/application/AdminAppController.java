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

public class AdminAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonPrintAll;

    @FXML
    void initialize() {
        assert buttonAdd != null : "fx:id=\"buttonAdd\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonDelete != null : "fx:id=\"buttonDelete\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonEdit != null : "fx:id=\"buttonEdit\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonExit != null : "fx:id=\"buttonExit\" was not injected: check your FXML file 'admin-view.fxml'.";
        assert buttonPrintAll != null : "fx:id=\"buttonPrintAll\" was not injected: check your FXML file 'admin-view.fxml'.";

        buttonPrintAll.setOnAction(event -> loadAdminPrint());
        buttonExit.setOnAction(event -> loadMainMenuAdmin());
        buttonAdd.setOnAction(event -> loadAdminAdd());
        buttonDelete.setOnAction(event -> loadAdminDelete());
        buttonEdit.setOnAction(event -> loadAdminEdit());
    }

    private void loadMainMenuAdmin() {
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
            showAlert("Ошибка", "Не удалось загрузить меню администратора");
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

    private void loadAdminPrint() {
        try {
            Stage currentStage = (Stage) buttonPrintAll.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-print-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Представление учетных записей");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
            e.printStackTrace();
        }
    }

    private void loadAdminAdd() {
        try {
            Stage currentStage = (Stage) buttonAdd.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-add-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Добавление учетных записей");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
            e.printStackTrace();
        }
    }

    private void loadAdminDelete() {
        try {
            Stage currentStage = (Stage) buttonDelete.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-delete-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Удаление учетных записей");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
            e.printStackTrace();
        }
    }

    private void loadAdminEdit() {
        try {
            Stage currentStage = (Stage) buttonEdit.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-edit-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Изменение учетных записей");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
            e.printStackTrace();
        }
    }
}
