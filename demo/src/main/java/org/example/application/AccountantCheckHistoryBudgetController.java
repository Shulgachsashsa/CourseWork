package org.example.application;

import commands.CommandType;
import connect.Request;
import connect.Response;
import dto.FinancialHistoryDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.example.application.MainApp.*;

public class AccountantCheckHistoryBudgetController {

    @FXML
    private TableView<FinancialHistoryDTO> financialHistoryTable;
    private ObservableList<FinancialHistoryDTO> allFinancialData;

    @FXML
    public void initialize() {
        loadFinancialHistory();
    }

    private void loadFinancialHistory() {
        Long userId = getID();
        if (userId == -3L) {
            showAlert("Ошибка", "Не удалось получить ID пользователя");
            return;
        }

        Request request = new Request(CommandType.GET_FINANCIAL_HISTORY, userId);
        try {
            getOut().writeObject(request);
            Response response = (Response) getIn().readObject();
            if (response.getState() == 1) {
                @SuppressWarnings("unchecked")
                List<FinancialHistoryDTO> financialData = (List<FinancialHistoryDTO>) response.getData();
                allFinancialData = FXCollections.observableArrayList(financialData);
                financialHistoryTable.setItems(allFinancialData);
            } else {
                showAlert("Ошибка", "Не удалось загрузить финансовую историю");
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как Excel");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(financialHistoryTable.getScene().getWindow());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Финансовая история");

                // Создаем заголовки
                Row headerRow = sheet.createRow(0);
                String[] headers = {"ID", "Изменение суммы", "Новый баланс", "Обработано", "ID запроса", "Дата операции"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // Заполняем данные
                int rowNum = 1;
                for (FinancialHistoryDTO item : financialHistoryTable.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getId());
                    row.createCell(1).setCellValue(item.getAmountChange());
                    row.createCell(2).setCellValue(item.getNewBalance());
                    row.createCell(3).setCellValue(item.getProcessedBy());
                    row.createCell(4).setCellValue(item.getRequestId());
                    row.createCell(5).setCellValue(item.getOperationDate().toString());
                }

                // Авторазмер колонок
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Сохраняем файл
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    showAlert("Успех", "Данные успешно экспортированы в Excel");
                }
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при экспорте в Excel: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
        loadFinancialHistory();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) financialHistoryTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountant-view.fxml"));
            Parent root = loader.load();
            stage.setTitle("Меню бухгалтера");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить представление учетных записей!");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}