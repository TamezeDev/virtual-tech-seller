package org.zeki.virtualtechseller.controller.moderator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.user.Moderator;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ResourceBundle;

public class ModeratorMenuController implements Initializable {

    @FXML
    private Label fullNameLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button salesControlBtn;

    @FXML
    private Button visitsControlBtn;

    // MODERATOR
    private Moderator currentModerator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        AppContext.getInstance().getConnectionManager().getDatabaseConfig().useModeratorConnection(); // CHANGE DB ADMIN
        currentModerator = (Moderator) SessionManager.getInstance().getCurrentUser();
    }

    private void initGUI() {
        fullNameLabel.setText("Bienvenid@: " + currentModerator.getFullName());
    }

    private void actions() {
        logoutBtn.setOnAction(event -> {
            SessionManager.getInstance().logout();
            SceneHelper.changeScene(logoutBtn, ViewPath.START_VIEW);
        });

        salesControlBtn.setOnAction(event -> SceneHelper.changeScene(logoutBtn, ViewPath.SALES_CONTROL_VIEW));

        visitsControlBtn.setOnAction(event -> SceneHelper.changeScene(logoutBtn, ViewPath.VISIT_CONTROL_VIEW));

    }
}
