package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.RequestHistoryDTO;
import enums.RequestStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.application.MainApp.*;

public class ManagerPrintHistoryController {

    @FXML
    private TableView<RequestHistoryDTO> requestHistoryTable;
    private ObservableList<RequestHistoryDTO> allHistoryData;

    @FXML
    public void initialize() {
        loadRequestHistory();
        setupStatusColumn();
    }

    private void setupStatusColumn() {
        TableColumn<RequestHistoryDTO, RequestStatus> statusColumn = (TableColumn<RequestHistoryDTO, RequestStatus>)
                requestHistoryTable.getColumns().get(5);
        statusColumn.setCellFactory(column -> new TableCell<RequestHistoryDTO, RequestStatus>() {
            @Override
            protected void updateItem(RequestStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(convertStatusToRussian(status));
                }
            }
        });
    }

    private String convertStatusToRussian(RequestStatus status) {
        return switch (status) {
            case PENDING -> "Ожидание";
            case APPROVED -> "Одобрено";
            case REJECTED -> "Отклонено";
        };
    }

    private void loadRequestHistory() {
        Long userId = getID();
        if (userId == -3L) {
            showAlert("Ошибка", "Не удалось получить ID пользователя");
            return;
        }

        Request request = new Request(CommandType.GET_REQUEST_BY_ID, userId);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                @SuppressWarnings("unchecked")
                List<RequestHistoryDTO> historyData = (List<RequestHistoryDTO>) response.getData();
                allHistoryData = FXCollections.observableArrayList(historyData);
                requestHistoryTable.setItems(allHistoryData);
            } else {
                showAlert("Ошибка", "Не удалось загрузить историю запросов");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFilter() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Все", "Ожидание", "Одобрено", "Отклонено");
        dialog.setTitle("Фильтр по статусу");
        dialog.setHeaderText("Выберите статус для фильтрации");
        dialog.setContentText("Статус:");

        dialog.showAndWait().ifPresent(selectedStatus -> {
            if ("Все".equals(selectedStatus)) {
                requestHistoryTable.setItems(allHistoryData);
            } else {
                RequestStatus status = convertRussianToStatus(selectedStatus);
                ObservableList<RequestHistoryDTO> filteredList = allHistoryData.stream()
                        .filter(dto -> dto.getStatus() == status)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                requestHistoryTable.setItems(filteredList);
            }
        });
    }

    private RequestStatus convertRussianToStatus(String russianStatus) {
        return switch (russianStatus) {
            case "Ожидание" -> RequestStatus.PENDING;
            case "Одобрено" -> RequestStatus.APPROVED;
            case "Отклонено" -> RequestStatus.REJECTED;
            default -> throw new IllegalArgumentException("Неизвестный статус: " + russianStatus);
        };
    }

    private Long getID() {
        Request request = new Request(CommandType.GET_ID, username);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                return (Long) response.getData();
            } else {
                showAlert("Ошибка", "Не удалось получить ID пользователя");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при получении ID: " + e.getMessage());
        }
        return -3L;
    }

    @FXML
    private void handleRefresh() {
        loadRequestHistory();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) requestHistoryTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager-view.fxml"));
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