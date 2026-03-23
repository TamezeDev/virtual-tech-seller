package org.zeki.virtualtechseller.controller.scene.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button clearBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private GridPane loginPane;

    @FXML
    private PasswordField passTxt;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @FXML
    private Button gobackBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
