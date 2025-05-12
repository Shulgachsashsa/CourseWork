package org.example.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.example.application.MainApp.*;

public class MainAppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button signInButton;

    @FXML
    void initialize() {
        signInButton.setOnAction(event -> {
            String login = login_field.getText().trim();
            String password = password_field.getText();
            if (login.isEmpty() || password.isEmpty()) {
                showAlert("Ошибка","Поля не могут быть пустыми!");
                return;
            }
            UserDTO dto = new UserDTO(login, password);
            Request request = new Request(CommandType.AUTHORIZATION, dto);
            Response response;

            try {
                getOut().writeObject(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                response = (Response) getIn().readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            switch (response.getState()) {
                case 0: {
                    showAlert("Ошибка", "Неверный логин или пароль");
                    break;
                }
                case 1: {
                    username = login;
                    switch ((Role)response.getData()) {
                        case Role.ADMIN -> loadMainMenuAdmin();
                        case Role.ACCOUNTANT -> loadMainMenuAccountant();
                        case Role.MANAGER -> loadMainMenuManager();
                    }
                    break;
                }
                default: {
                    showAlert("Ошибка", "Неизвестный ответ сервера");
                    break;
                }
            }

        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadMainMenuAdmin() {
        try {
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Меню администратора");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню администратора");
            e.printStackTrace();
        }
    }

    private void loadMainMenuManager() {
        try {
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Меню менеджера");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню менеджера");
            e.printStackTrace();
        }
    }

    private void loadMainMenuAccountant() {
        try {
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Меню бухгалтера");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню администратора");
            e.printStackTrace();
        }
    }

}
