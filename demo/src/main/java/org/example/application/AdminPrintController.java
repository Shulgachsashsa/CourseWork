package org.example.application;

import dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.management.relation.Role;
import java.util.List;

import java.awt.*;

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
        userData.addAll(new UserDTO("aa", "aa", "fdfd", "11", ));
        userData.addAll(new UserDTO("aa", "aa"));
        userData.addAll(new UserDTO("aa", "aa"));
        userData.addAll(new UserDTO("aa", "aa"));

    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) userTable.getScene().getWindow();
        stage.close();
    }
}
