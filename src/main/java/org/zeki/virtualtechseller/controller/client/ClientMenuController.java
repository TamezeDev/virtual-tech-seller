package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.user.LoginUserDto;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.*;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.List;
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
    private SaleService saleService;
    private VisitService visitService;

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
        visitService = AppContext.getInstance().getVisitService();

        setTestUser();
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        loadCartUser();
        loadUserVisits();

        currentUser.setCurrentExhibition(null);  // SET NULL CURRENT EVENT
        saleService.setSalesList(currentUser);
    }

    private void initGUI() {
        fullNameLabel.setText("Bienvenid@: " + currentUser.getFullName());
        creditLabel.setText(currentUser.getCredit() + " €");
        if (!currentUser.emptyCartList()) {
            numItemCartLabel.setText("X" + currentUser.checkQuantityCartItems() + " Artículos");
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
            if (currentUser.emptyCartList()) {
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

    private void loadUserVisits() {
        ResultService<List<UserVisit>> result = visitService.getAllUserVisits(currentUser);
        if (result.isSuccess()) {
            currentUser.setVisits(result.getData());
        }
    }

    //PROVISIONAL METHOD TO TEST CLIENT FUNCTIONS
    private void setTestUser() {
        UserService userService = AppContext.getInstance().getUserService();

        ResultService<User> resultService = userService.login(new LoginUserDto("client2@virtualtechseller.com", "Client-123"));
        SessionManager.getInstance().login(resultService.getData());
    }
}
