package org.zeki.virtualtechseller.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PermissionViewController implements Initializable {

    @FXML
    private HBox accessControlBOx;

    @FXML
    private Button addPermissionBtn;

    @FXML
    private Button authorizeBtn;

    @FXML
    private TableColumn<?, ?> dateHourColumn;

    @FXML
    private TableColumn<?, ?> dniColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TableColumn<?, ?> emailColumn1;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button listUsersBtn;

    @FXML
    private Button rmPermissionBtn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableView<?> usersTable;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
