package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.FinancialHistoryDTO;
import dto.RequestDTO;
import dto.RequestHistoryDTO;
import enums.RequestStatus;
import enums.RequestType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.application.MainApp.*;

public class AccountantWorkWithRequestsController {

    @FXML private TableView<RequestDTO> requestsTable;
    @FXML private TableColumn<RequestDTO, Long> idColumn;
    @FXML private TableColumn<RequestDTO, Long> userColumn;
    @FXML private TableColumn<RequestDTO, RequestType> typeColumn;
    @FXML private TableColumn<RequestDTO, Double> amountColumn;
    @FXML private TableColumn<RequestDTO, RequestStatus> statusColumn;
    @FXML private TableColumn<RequestDTO, String> detailsColumn;
    @FXML private TableColumn<RequestDTO, String> reasonColumn;

    @FXML private Button saveButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;

    private ObservableList<RequestDTO> requestsData = FXCollections.observableArrayList();
    private RequestDTO currentModifiedRequest = null;
    private static String rejectionReason;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupButtons();
        loadRequestsData();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        typeColumn.setCellFactory(column -> new TableCell<RequestDTO, RequestType>() {
            @Override
            protected void updateItem(RequestType type, boolean empty) {
                super.updateItem(type, empty);
                setText(empty || type == null ? "" : type == RequestType.MONEY ? "Деньги" : "Одежда");
            }
        });

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(column -> new TableCell<RequestDTO, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? "" : String.format("%.2f", amount));
            }
        });
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("clothesDetails"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        statusColumn.setCellFactory(column -> new TableCell<RequestDTO, RequestStatus>() {
            private final ComboBox<RequestStatus> statusCombo = new ComboBox<>();
            private boolean ignoreEvent = false;

            {
                statusCombo.getItems().setAll(RequestStatus.APPROVED, RequestStatus.REJECTED);
                statusCombo.setConverter(new StringConverter<RequestStatus>() {
                    @Override
                    public String toString(RequestStatus status) {
                        return status == null ? "" : convertStatusToRussian(status);
                    }

                    @Override
                    public RequestStatus fromString(String string) {
                        return convertRussianToStatus(string);
                    }
                });

                statusCombo.addEventHandler(ActionEvent.ACTION, event -> {
                    if (ignoreEvent) {
                        ignoreEvent = false;
                        return;
                    }

                    RequestDTO request = getTableView().getItems().get(getIndex());
                    RequestStatus originalStatus = request.getRequestStatus();
                    RequestStatus newStatus = statusCombo.getValue();

                    if (currentModifiedRequest != null && !currentModifiedRequest.equals(request)) {
                        ignoreEvent = true;
                        Platform.runLater(() -> {
                            statusCombo.setValue(originalStatus);
                            requestsTable.refresh();
                            showAlert("Предупреждение", "Вы можете редактировать только одну запись за раз. Сохраните текущие изменения перед редактированием другой записи.");
                        });
                        return;
                    }

                    if (newStatus != originalStatus) {
                        if (newStatus == RequestStatus.REJECTED) {
                            showReasonDialog(request, () -> {
                                request.setRequestStatus(newStatus);
                                currentModifiedRequest = request;
                                updateSaveButtonState();
                                requestsTable.refresh();
                            });
                        } else {
                            request.setRequestStatus(newStatus);
                            currentModifiedRequest = request;
                            updateSaveButtonState();
                            requestsTable.refresh();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(RequestStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ignoreEvent = true;
                    statusCombo.setValue(status);
                    setGraphic(statusCombo);
                    setText(null);
                    ignoreEvent = false;
                }
            }
        });
    }

    private void setupButtons() {
        String buttonStyle = "-fx-base: #4a8bc9; -fx-text-fill: white; -fx-font-weight: bold;";
        saveButton.setStyle(buttonStyle);
        refreshButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
        saveButton.setDisable(true);
        saveButton.setOnAction(event -> saveChanges());
        refreshButton.setOnAction(event -> loadRequestsData());
        backButton.setOnAction(event -> handleBack());
    }

    private void updateSaveButtonState() {
        saveButton.setDisable(currentModifiedRequest == null);
    }

    private void saveChanges() {
        if (currentModifiedRequest == null) return;
        if (currentModifiedRequest.getRequestType() == RequestType.MONEY &&
                currentModifiedRequest.getRequestStatus() == RequestStatus.APPROVED &&
                currentModifiedRequest.getAmount() > getBudget()) {
                showAlert("Ошибка", "На балансе предприятия недостаточно средств!");
                return;
        }
        try {
            Request saveRequest = new Request(CommandType.SET_STATE_REQUEST, currentModifiedRequest);
            getOut().writeObject(saveRequest);
            Response response = (Response) getIn().readObject();
            if (currentModifiedRequest.getRequestStatus() == RequestStatus.APPROVED &&
                    currentModifiedRequest.getRequestType() == RequestType.CLOTHES) {
                approvedClothes();
            } else if (currentModifiedRequest.getRequestStatus() == RequestStatus.APPROVED &&
                    currentModifiedRequest.getRequestType() == RequestType.MONEY) {
                approvedMoney();
            } else if (currentModifiedRequest.getRequestStatus() == RequestStatus.REJECTED &&
                    currentModifiedRequest.getRequestType() == RequestType.CLOTHES) {
                rejectedClothes();
            }
            if (response.getState() == 1) {
                RequestHistoryDTO historyDTO = createHistoryDTO();
                saveRequest = new Request(CommandType.SET_NEW_REQ_HISTORY, historyDTO);
                getOut().writeObject(saveRequest);
                response = (Response) getIn().readObject();
                if (response.getState() == 1) {
                    showAlert("Успех", "Изменения успешно сохранены");
                    currentModifiedRequest = null;
                    rejectionReason = null;
                    updateSaveButtonState();
                    loadRequestsData();
                } else {
                    showAlert("Ошибка", "Не удалось сохранить историю изменений");
                }
            } else {
                showAlert("Ошибка", "Не удалось сохранить изменения статуса");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при сохранении: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void approvedClothes() {
        Double totalPrice = getTotalPriceClothes(currentModifiedRequest.getId());
        addBudget(totalPrice);
        FinancialHistoryDTO financialHistoryDTO = new FinancialHistoryDTO(
            totalPrice,
            getBudget(),
            getID(),
            "",
            currentModifiedRequest.getId()
        );
        saveFinancialHistory(financialHistoryDTO);
    }

    private void approvedMoney() {
        addBudget(-1 * currentModifiedRequest.getAmount());
        FinancialHistoryDTO financialHistoryDTO = new FinancialHistoryDTO(
                -1 * currentModifiedRequest.getAmount(),
                getBudget(),
                getID(),
                rejectionReason,
                currentModifiedRequest.getId()
        );
        saveFinancialHistory(financialHistoryDTO);
    }

    private void rejectedClothes() {
        String clothes = currentModifiedRequest.getClothesDetails();
        backClothes(clothes);
    }

    private void backClothes(String clothes) {
        Request request = new Request(CommandType.BACK_CLOTHES, clothes);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() != 1) {
                showAlert("Ошибка", "Не удалось загрузить данные цены");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private void saveFinancialHistory(FinancialHistoryDTO financialHistoryDTO) {
        Request request = new Request(CommandType.SAVE_FINANCIAL_HISTORY, financialHistoryDTO);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() != 1) {
                showAlert("Ошибка", "Не удалось загрузить данные цены");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private void addBudget(Double totalPrice) {
        Request request = new Request(CommandType.ADD_BUDGET, totalPrice);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() != 1) {
                showAlert("Ошибка", "Не удалось загрузить данные цены");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private static Double getTotalPriceClothes(Long id) {
        Request request = new Request(CommandType.GET_TOTAL_PRICE_CLOTHES, id);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                Double totalPrice = (Double) response.getData();
                return totalPrice;
            } else {
                showAlert("Ошибка", "Не удалось загрузить данные цены");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return -1.0;
    }


    private RequestHistoryDTO createHistoryDTO() throws IOException, ClassNotFoundException {
        RequestHistoryDTO dto = new RequestHistoryDTO();
        Long id = currentModifiedRequest.getId();
        dto.setAccountantUser(getID());
        dto.setStatus(currentModifiedRequest.getRequestStatus());
        dto.setProcessedAt(LocalDateTime.now());
        dto.setRequestID(id);
        if (currentModifiedRequest.getRequestStatus() == RequestStatus.REJECTED) {
            dto.setAccountantComment(rejectionReason);
            dto.setReason(rejectionReason);
        }
        dto.setUser(currentModifiedRequest.getUserId());
        dto.setType(currentModifiedRequest.getRequestType());
        dto.setAmount(currentModifiedRequest.getAmount());
        dto.setDetails(currentModifiedRequest.getClothesDetails());
        return dto;
    }

    private void showReasonDialog(RequestDTO request, Runnable onSuccess) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Причина отказа");
        dialog.setHeaderText("Укажите причину отказа для запроса #" + request.getId());
        dialog.setContentText("Причина:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            rejectionReason = result.get().trim();
            request.setReason(rejectionReason);
            onSuccess.run();
            System.out.println("Причина отказа сохранена: " + rejectionReason);
        } else {
            request.setRequestStatus(RequestStatus.PENDING);
            requestsTable.refresh();
        }
    }

    private void loadRequestsData() {
        Request request = new Request(CommandType.GET_REQUESTS_WITH_STATE_PENDING, null);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                @SuppressWarnings("unchecked")
                List<RequestDTO> requestList = (List<RequestDTO>) response.getData();
                requestsData.setAll(requestList);
                requestsTable.setItems(requestsData);
                currentModifiedRequest = null;
                updateSaveButtonState();
            } else {
                showAlert("Ошибка", "Список запросов пуст!");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Stage stage = (Stage) requestsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Меню бухгалтера");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось вернуться в меню");
            e.printStackTrace();
        }
    }

    private String convertStatusToRussian(RequestStatus status) {
        return switch (status) {
            case PENDING -> "Ожидание";
            case APPROVED -> "Одобрено";
            case REJECTED -> "Отклонено";
        };
    }

    private RequestStatus convertRussianToStatus(String russianStatus) {
        return switch (russianStatus) {
            case "Ожидание" -> RequestStatus.PENDING;
            case "Одобрено" -> RequestStatus.APPROVED;
            case "Отклонено" -> RequestStatus.REJECTED;
            default -> throw new IllegalArgumentException("Неизвестный статус: " + russianStatus);
        };
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static Long getID() {
        Request request = new Request(CommandType.GET_ID, username);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                Long ID = (Long) response.getData();
                return ID;
            } else {
                showAlert("Ошибка", "Не удалось загрузить данные из запроса");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return -3L;
    }

    private static Double getBudget() {
        Request request = new Request(CommandType.GET_BUDGET, null);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                Double budget = (Double) response.getData();
                return budget;
            } else {
                showAlert("Ошибка", "Не удалось загрузить данные бюджета");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return -1.0;
    }
}