package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class AdminAddController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> accessComboBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Button buttonBack;

    @FXML
    private Button createButton;

    @FXML
    private void initialize() {
        roleComboBox.getSelectionModel().selectFirst();
        accessComboBox.getSelectionModel().selectFirst();
        buttonBack.setOnAction(event -> loadMainMenuAdmin());
    }

    @FXML
    private void handleCreateUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        Role roleInSQL;
        if (role.equals("Менеджер"))
            roleInSQL = Role.MANAGER;
        else
            roleInSQL = Role.ACCOUNTANT;
        String access = accessComboBox.getValue();
        if (access.equals("Разрешен"))
            access = "1";
        else
            access = "0";
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Заполните все поля!");
            return;
        }

        UserDTO userDTO = new UserDTO(username, password, access, roleInSQL);
        Request request = new Request(CommandType.ADD_USER, userDTO);
        Response response;
        try {
            getOut().writeObject(request);
            response = (Response) getIn().readObject();
            if (response.getState() == 0) {
                showAlertInf("Результат", "Пользователь успешно добавлен");
            } else if (response.getState() == 1) {
                showAlert("Ошибка", "Пользователь с таким логином уже существует");
            } else {
                showAlert("Ошибка", "Не удалось добавить пользователя: " + username);
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при добавлении пользователя");
            e.printStackTrace();
        }

        messageLabel.setText("Пользователь создан: " + username);
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().selectFirst();
        accessComboBox.getSelectionModel().selectFirst();
    }

    private void loadMainMenuAdmin() {
        try {
            Stage currentStage = (Stage) buttonBack.getScene().getWindow();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertInf(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}