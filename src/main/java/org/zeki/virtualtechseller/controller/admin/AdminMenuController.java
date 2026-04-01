package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.LoginUserDto;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    private Button activateEventBtn;

    @FXML
    private Button addEventBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button adduserBtn;

    @FXML
    private Button assignProductBtn;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button modUserBtn;

    @FXML
    private Button importXMLBtn;

    // ADMIN
    private Admin currentAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        AppContext.getInstance().getConnectionManager().getDatabaseConfig().useAdminConnection(); // CHANGE DB ADMIN
        setTestUser();
        currentAdmin = (Admin) SessionManager.getInstance().getCurrentUser();

    }

    private void initGUI() {
        fullNameLabel.setText("Bienvenid@: " + currentAdmin.getFullName());
    }

    private void actions() {
        logoutBtn.setOnAction(event -> {
            SessionManager.getInstance().logout();
            SceneHelper.changeScene(logoutBtn, ViewPath.START_VIEW);
        });

        adduserBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.REGISTER_VIEW));

        importXMLBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.PERMISSION_VIEW));

        modUserBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.PERMISSION_VIEW));

        addEventBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.ADD_EVENT_VIEW));

        activateEventBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.CONTROL_EVENTS_VIEW));

        addProductBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.ADD_PRODUCT_VIEW));

        assignProductBtn.setOnAction(event -> SceneHelper.changeScene(adduserBtn, ViewPath.ASSIGN_PRODUCT_VIEW));

    }

    //PROVISIONAL METHOD TO TEST ADMIN FUNCTIONS
    private void setTestUser() {
        UserService userService = AppContext.getInstance().getUserService();
        ResultService<User> resultService = userService.login(new LoginUserDto("admin1@virtualtechseller.com", "Admin-123"));
        SessionManager.getInstance().login(resultService.getData());

    }
}
