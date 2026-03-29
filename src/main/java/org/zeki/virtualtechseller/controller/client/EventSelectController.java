package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ResourceBundle;

public class EventSelectController implements Initializable {

    @FXML
    private FlowPane availableEventsBox;

    @FXML
    private Label creditLabel;

    @FXML
    private FlowPane endEventsBox;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane nextEventsBox;

    // USER
    private Client client = (Client) SessionManager.getInstance().getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        
    }

    private void initGUI() {

    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));
    }
}
