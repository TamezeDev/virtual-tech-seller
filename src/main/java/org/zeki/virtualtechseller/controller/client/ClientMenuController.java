package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.CartService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.AlertHelper;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    // COMPONENTS
    @FXML
    private Button accessEventBtn;

    @FXML
    private Button addCreditBtn;

    @FXML
    private Button cartItemBtn;

    @FXML
    private Label creditLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button myProductsBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label numItemCartLabel;
    // USER
    private Client currentUser;
    // SERVICES
    private CartService cartService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        AppContext.getInstance().getConnectionManager().getDatabaseConfig().useClientConnection(); // CHANGE DB USER
        cartService = AppContext.getInstance().getCartService();
        setTestUser();
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        loadCartUser();
    }

    private void initGUI() {
        fullNameLabel.setText("Bienvenid@: " + currentUser.getFullName());
        creditLabel.setText(currentUser.getCredit() + " €");
        if (!currentUser.getCartItems().isEmpty()) {
            numItemCartLabel.setText("X" + currentUser.getCartItems().size() + " ITEM");
            numItemCartLabel.setVisible(true);
        }
    }

    private void actions() {
        logoutBtn.setOnAction(event -> {
            SessionManager.getInstance().logout();
            SceneHelper.changeScene(logoutBtn, ViewPath.START_VIEW);
        });

        addCreditBtn.setOnAction(event -> SceneHelper.changeScene(addCreditBtn, ViewPath.ADD_CREDIT_VIEW));
        cartItemBtn.setOnAction(event -> {
            if (currentUser.getCartItems().isEmpty()) {
                feedbackLabel.setText("No tiene productos en el carrito");
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            SceneHelper.changeScene(cartItemBtn, ViewPath.CART_ITEMS_VIEW);
        });
        myProductsBtn.setOnAction(event -> {
            if (currentUser.getSales().isEmpty()) {
                feedbackLabel.setText("Aún no ha hecho ninguna compra");
            }
            SceneHelper.changeScene(myProductsBtn, ViewPath.CLIENT_PRODUCT_VIEW);
        });
        accessEventBtn.setOnAction(event -> SceneHelper.changeScene(accessEventBtn, ViewPath.EVENT_SELECT_VIEW));
    }

    private void loadCartUser() {
        cartService.setCartItemList(currentUser);
    }

    //PROVISIONAL METHOD TO TEST CLIENT FUNCTIONS
    private void setTestUser() {
        UserService userService = AppContext.getInstance().getUserService();
        ResultService<User> resultService = userService.login("client2@virtualtechseller.com", "client123");
        SessionManager.getInstance().login(resultService.getData());
        Exhibition exhibition = new Exhibition();
        exhibition.setIdExhibition(1);
        ((Client) resultService.getData()).setCurrentExhibition(exhibition);
    }
}
