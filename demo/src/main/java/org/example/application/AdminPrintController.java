package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import enums.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.util.stream.Collectors;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class AdminPrintController {

    @FXML
    private TableView<UserDTO> userTable;

    private ObservableList<UserDTO> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeTestData();
        userTable.setItems(userData);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setStyle("-fx-font-size: 14px;");
    }

    private void initializeTestData() {
        UserDTO dto = new UserDTO();
        Request request = new Request(CommandType.FULL_LIST_USERS, dto);
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

        if (response.getState() == 1) {
            Object data = response.getData();
            List<UserDTO> users = new ArrayList<>();
            if (data instanceof List<?>) {
                users = ((List<?>) data).stream()
                        .filter(obj -> obj instanceof UserDTO)
                        .map(obj -> (UserDTO) obj)
                        .collect(Collectors.toList());
            }
            for (UserDTO userDTO: users) {
                userData.addAll(userDTO);
            }
        } else {
            showAlert("Ошибка", "Неизвестный ответ сервера");
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Меню администратора");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
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


