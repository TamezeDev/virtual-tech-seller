package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PermissionController implements Initializable {

    @FXML
    private TableColumn<?, ?> accessColumn1;

    @FXML
    private HBox accessControlBOx;

    @FXML
    private Button addPermissionBtn;

    @FXML
    private Button authorizeBtn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TableColumn<?, ?> lastNameColumn;

    @FXML
    private Button listUsersBtn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> phoneColumn;

    @FXML
    private Button rmPermissionBtn;

    @FXML
    private TableView<?> usersTable;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
