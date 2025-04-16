package org.example.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

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
                    loadMainMenuAdmin();
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
            showAlert("Ошибка", "Не удалось загрузить главное меню");
            e.printStackTrace();
        }
    }

}
