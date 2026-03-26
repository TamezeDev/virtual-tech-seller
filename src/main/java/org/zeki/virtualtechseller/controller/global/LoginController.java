package org.zeki.virtualtechseller.controller.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.CartService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.*;

import java.net.URL;
import java.sql.SQLException;
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
    private TextField userEmailTxt;

    @FXML
    private Button gobackBtn;

    private List<TextField> textFields;
    private UserService userService;
    private CartService cartService;
    private SaleService saleService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        textFields = new ArrayList<>();
        userService = AppContext.getInstance().getUserService();
        cartService = AppContext.getInstance().getCartService();
        saleService = AppContext.getInstance().getSaleService();
    }

    private void initGUI() {
        visiblePasswordTxt.textProperty().bindBidirectional(passTxt.textProperty());
        groupTextFields();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.START_VIEW));
        loginBtn.setOnAction(event -> checkLogin());
        clearBtn.setOnAction(event -> FormularyHelper.clearFields(textFields));
        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> {
            // INTERCHANGE BETWEEN HIDE OR VISIBLE TXT
            visiblePasswordTxt.setVisible(selected);
            visiblePasswordTxt.setManaged(selected);
            passTxt.setVisible(!selected);
            passTxt.setManaged(!selected);
        });
    }

    private void groupTextFields() {
        textFields.add(userEmailTxt);
        textFields.add(passTxt);
        textFields.add(visiblePasswordTxt);
    }

    private void checkLogin() {
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        try {
            ResultService<User> resultUser = userService.login(userEmailTxt.getText(), passTxt.getText());

            if (!resultUser.isSuccess()) {
                feedbackLabel.setText(resultUser.getMessage());
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            SessionManager.getInstance().login(resultUser.getData());
            if (resultUser.getData() instanceof Client) {
                cartService.setCartItemList(resultUser.getData());
                saleService.setSalesList(resultUser.getData());
            }
            SceneHelper.changeScene(loginBtn, ViewPath.CLIENT_MENU_VIEW);
        } catch (DBConnectionException e) {   // SHOW CONNECTION ALERT TO USER
            AlertHelper.showDBConnectAlert();

        } catch (SQLException e) {
            AlertHelper.showSQLAlert(); // SHOW SQL ALERT TO USER
            e.printStackTrace();
        }
    }
}
