package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyUserController implements Initializable {
    @FXML
    private Button acceptBtn;

    @FXML
    private HBox accessControlBOx;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField emailTxt1;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private PasswordField repeatPassTxt;

    @FXML
    private Button searchBtn;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
