package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.ClothesDTO;
import dto.RequestDTO;
import enums.RequestStatus;
import enums.RequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.application.MainApp.*;

public class ManagerCreateRequestController {

    @FXML private AnchorPane rootPane;
    @FXML private RadioButton moneyRadio;
    @FXML private RadioButton clothesRadio;
    @FXML private ToggleGroup requestTypeGroup;

    @FXML private VBox moneySection;
    @FXML private VBox clothesSection;

    @FXML private TextField amountField;
    @FXML private ComboBox<ClothesDTO> clothesTypeCombo;
    @FXML private TextField quantityField;
    @FXML private Label availableLabel;
    @FXML private Label priceLabel;
    @FXML private Button addClothesButton;
    @FXML private Button cancelButton;
    @FXML private Button submitButton;

    @FXML private TableView<ClothesItem> clothesTable;
    @FXML private TextArea reasonField;

    private ObservableList<ClothesItem> clothesItems = FXCollections.observableArrayList();
    private List<ClothesDTO> clothesList = new ArrayList<>();

    @FXML
    private void initialize() {
        loadClothesData();
        clothesTypeCombo.setCellFactory(param -> new ListCell<ClothesDTO>() {
            @Override
            protected void updateItem(ClothesDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getClothesType());
                }
            }
        });

        clothesTypeCombo.setButtonCell(new ListCell<ClothesDTO>() {
            @Override
            protected void updateItem(ClothesDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getClothesType());
                }
            }
        });

        requestTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == moneyRadio) {
                showMoneySection();
                rootPane.setPrefWidth(700);
                rootPane.setPrefHeight(400);
            } else if (newVal == clothesRadio) {
                showClothesSection();
                rootPane.setPrefWidth(750);
                rootPane.setPrefHeight(600);
            }
        });

        clothesTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateAvailableQuantity(newVal);
            }
        });

        addClothesButton.setOnAction(e -> addClothesItem());
        clothesTable.setItems(clothesItems);
        showMoneySection();
    }

    private void loadClothesData() {
        Request request = new Request(CommandType.GET_CLOTHES);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                clothesList = (List<ClothesDTO>) response.getData();
                clothesTypeCombo.getItems().setAll(clothesList);
            } else {
                showAlert("Ошибка", "Не удалось загрузить данные об одежде");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private void updateAvailableQuantity(ClothesDTO clothesDTO) {
        availableLabel.setText("Доступно: " + clothesDTO.getCounter());
        priceLabel.setText("Цена: " + clothesDTO.getPrice() + " руб.");
    }

    private void addClothesItem() {
        try {
            ClothesDTO selectedClothes = clothesTypeCombo.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Ошибка", "Количество должно быть больше нуля");
                return;
            }
            if (quantity > selectedClothes.getCounter()) {
                showAlert("Ошибка", "Запрашиваемое количество превышает доступное");
                return;
            }
            clothesItems.add(new ClothesItem(
                    selectedClothes.getClothesType(),
                    quantity,
                    selectedClothes.getPrice()
            ));
            selectedClothes.setCounter(selectedClothes.getCounter() - quantity);
            updateAvailableQuantity(selectedClothes);
            quantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректное количество");
        }
    }

    @FXML
    private void handleSubmit() {
        if (moneyRadio.isSelected()) {
            createMoneyRequest();
        } else {
            createClothesRequest();
        }
    }

    @FXML
    private void handleCancel() {
        try {
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager-view.fxml"));
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

    private void createMoneyRequest() {
        try {
            RequestDTO requestDTO = new RequestDTO(
                    getID(),
                    RequestType.MONEY,
                    Double.parseDouble(amountField.getText()),
                    reasonField.getText(),
                    RequestStatus.PENDING);
            reqInBd(requestDTO, CommandType.REQUEST_BUDGET);
            amountField.clear();
            reasonField.clear();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную сумму");
        }
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

    private void showMoneySection() {
        moneySection.setVisible(true);
        moneySection.setManaged(true);
        clothesSection.setVisible(false);
        clothesSection.setManaged(false);
    }

    private void showClothesSection() {
        moneySection.setVisible(false);
        moneySection.setManaged(false);
        clothesSection.setVisible(true);
        clothesSection.setManaged(true);
        if (clothesTypeCombo.getSelectionModel().isEmpty() && !clothesTypeCombo.getItems().isEmpty()) {
            clothesTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private void createClothesRequest() {
        if (clothesItems.isEmpty()) {
            showAlert("Ошибка", "Добавьте хотя бы один элемент одежды");
            return;
        }
        RequestDTO requestDTO = new RequestDTO(
                getID(),
                RequestType.CLOTHES,
                reasonField.getText(),
                convertToCustomString(clothesItems),
                RequestStatus.PENDING);
        reqInBd(requestDTO, CommandType.REQUEST_CLOTHES);
        String clothes = requestDTO.getClothesDetails();
        Request request1 = new Request(CommandType.MINES_CLOTHES, clothes);
        try {
            getOut().writeObject(request1);
            Response response1 = (Response) getIn().readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clothesItems.clear();
        reasonField.clear();
        quantityField.clear();
    }

    private static void reqInBd(RequestDTO requestDTO, CommandType type) {
        Request request = new Request(type, requestDTO);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                showAlert("Успех", "Запрос на выделение средств создан");
            } else {
                showAlert("Ошибка", "Не удалось отправить запрос");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    public static class ClothesItem {
        private final String type;
        private final int quantity;
        private final double price;

        public ClothesItem(String type, int quantity, double price) {
            this.type = type;
            this.quantity = quantity;
            this.price = price;
        }

        public String getType() { return type; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotalPrice() { return quantity * price; }

        @Override
        public String toString() {
            return type;
        }
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String convertToCustomString(ObservableList<ClothesItem> clothesItems) {
        return clothesItems.stream()
                .map(item -> String.format(
                        "type=%s, quantity=%d, price=%.2f, totalprice=%.2f",
                        item.getType(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice() * item.getQuantity()
                ))
                .collect(Collectors.joining(";"));
    }
}
