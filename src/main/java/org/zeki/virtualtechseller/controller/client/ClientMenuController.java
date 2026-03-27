package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
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
    // USER
    private Client currentUser;
    // SERVICES
    private CartService cartService;
    private SaleService saleService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        AppContext.getInstance().getConnectionManager().getDatabaseConfig().useClientConnection(); // CHANGE DB USER
        cartService = AppContext.getInstance().getCartService();
        saleService = AppContext.getInstance().getSaleService();
        setTestUser();
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
    }

    private void initGUI() {
        fullNameLabel.setText("Bienvenid@: " + currentUser.getFullName());
        creditLabel.setText(currentUser.getCredit() + " €");
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
            if (currentUser.getPurchaseList().isEmpty()) {
                feedbackLabel.setText("Aún no ha hecho ninguna compra");
            }
            SceneHelper.changeScene(myProductsBtn, ViewPath.CLIENT_PRODUCT_VIEW);
        });
        accessEventBtn.setOnAction(event -> SceneHelper.changeScene(accessEventBtn, ViewPath.EVENT_SELECT_VIEW));

    }

    //PROVISIONAL METHOD TO TEST CLIENT FUNCTIONS
    private void setTestUser() {
        UserService userService = AppContext.getInstance().getUserService();
        CartService cartService = AppContext.getInstance().getCartService();
        SaleService saleService = AppContext.getInstance().getSaleService();
        try {
            ResultService<User> resultService = userService.login("client5@virtualtechseller.com", "client123");
            SessionManager.getInstance().login(resultService.getData());
            cartService.setCartItemList(resultService.getData());
            saleService.setSalesList(resultService.getData());
        } catch (DBConnectionException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadCartUser() {
        try {
            saleService.setSalesList(currentUser);
            cartService.setCartItemList(currentUser);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            AlertHelper.showSQLAlert(); // SHOW SQL ALERT TO USER

        }
    }
}
