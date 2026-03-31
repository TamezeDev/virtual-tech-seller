package org.zeki.virtualtechseller.controller.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button clearBtn;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private PasswordField repeatPassTxt;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @FXML
    private ComboBox<?> userRolCb;

    @FXML
    private Button gobackBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
