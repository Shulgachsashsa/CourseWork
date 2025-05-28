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
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.application.MainApp.getIn;
import static org.example.application.MainApp.getOut;

public class AdminEditController {
    @FXML
    private TableView<UserDTO> userTable;
    @FXML
    private ComboBox<Integer> accessComboBox;
    @FXML
    private Button updateButton;

    private ObservableList<UserDTO> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeTestData();
        userTable.setItems(userData);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setStyle("-fx-font-size: 14px;");
        accessComboBox.setItems(FXCollections.observableArrayList(0, 1));
        accessComboBox.getSelectionModel().selectFirst();
        updateButton.setDisable(true);
        userTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean userSelected = newSelection != null;
                    updateButton.setDisable(!userSelected);
                    if (userSelected) {
                        accessComboBox.getSelectionModel().select(Integer.valueOf(newSelection.getAccess().toString()));
                    }
                });
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
        } catch (IOException | ClassNotFoundException e) {
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
            userData.addAll(users);
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

    @FXML
    private void handleUpdate() {
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Ошибка", "Пожалуйста, выберите пользователя для изменения");
            return;
        }
        Integer newAccess = accessComboBox.getValue();
        if (newAccess == null) {
            showAlert("Ошибка", "Пожалуйста, выберите уровень доступа");
            return;
        }
        UserDTO updateDTO = new UserDTO();
        updateDTO.setLogin(selectedUser.getLogin());
        updateDTO.setAccess(String.valueOf(newAccess));
        Request request = new Request(CommandType.UPDATE_USER_ACCESS, updateDTO);
        Response response;
        try {
            getOut().writeObject(request);
            response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                selectedUser.setAccess(String.valueOf(newAccess));
                userTable.refresh();
                showAlertInf("Результат", "Доступ пользователя успешно изменен");
            } else {
                showAlert("Ошибка", "Не удалось изменить доступ пользователя: " + selectedUser.getLogin());
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при изменении доступа пользователя");
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