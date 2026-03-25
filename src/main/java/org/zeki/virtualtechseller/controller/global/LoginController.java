package org.zeki.virtualtechseller.controller.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.util.FormularyHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button clearBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField visiblePasswordTxt;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @FXML
    private Button gobackBtn;

    private List<TextField> textFields;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        textFields = new ArrayList<>();
    }

    private void initGUI() {
        visiblePasswordTxt.textProperty().bindBidirectional(passTxt.textProperty());
    }


    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.START_VIEW));
        loginBtn.setOnAction(event -> {
        });
        clearBtn.setOnAction(event -> clearFields());
        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> {
            // INTERCHANGE BETWEEN HIDE OR VISIBLE TXT
            visiblePasswordTxt.setVisible(selected);
            visiblePasswordTxt.setManaged(selected);
            passTxt.setVisible(!selected);
            passTxt.setManaged(!selected);

        });
    }

    private void groupTextFields() {
        textFields.add(userNameTxt);
        textFields.add(passTxt);
        textFields.add(visiblePasswordTxt);
    }

    private void clearFields() {
        groupTextFields();
        FormularyHelper.clearFields(textFields);
    }

    private void checkLogin(){
        
    }
}
