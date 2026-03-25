package org.zeki.virtualtechseller.controller.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actions();
    }

    private void actions() {
        loginBtn.setOnAction(event -> SceneHelper.changeScene(loginBtn, ViewPath.LOGIN_VIEW));
        registerBtn.setOnAction(event -> SceneHelper.changeScene(loginBtn, ViewPath.REGISTER_VIEW));
    }

}
