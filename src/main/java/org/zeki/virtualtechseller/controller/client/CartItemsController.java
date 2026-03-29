package org.zeki.virtualtechseller.controller.client;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.CartService;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CartItemsController implements Initializable {

    @FXML
    private Button buyBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Label creditLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane productsBox;

    // USER
    private Client currentUser;
    private ObservableList<CartItem> cartItems;
    // SERVICES
    private CartService cartService;
    private UserService userService;
    private ProductService productService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        cartService = AppContext.getInstance().getCartService();
        userService = AppContext.getInstance().getUserService();
        productService = AppContext.getInstance().getProductService();
        cartItems = FXCollections.observableArrayList(currentUser.getCartItems());
    }

    private void initGUI() {
        creditLabel.setText(currentUser.getCredit() + " €");
        setCartItems();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));

        clearBtn.setOnAction(event -> requestRemoveAllCartItems());

        buyBtn.setOnAction(event -> requestCheckOutCart());

        cartItems.addListener((ListChangeListener<CartItem>) change -> reloadItems());
    }

    private void clearCartItems() {
        currentUser.clearCart();
        cartItems.clear();
        reloadItems();
    }

    private boolean checkAvailableStock() {
        // CHECK ITEM AVAILABLE AND ENOUGH STOCK
        ResultService<CartItem> result = productService.stockCartItems();
        if (result == null) {
            return false;
        } else if (!result.isSuccess()) {
            // SHOW FEEDBACK
            String message = result.getMessage() + result.getData().getProduct().getName();
            feedbackLabel.setText(message);
            Feedback.showFeedback(feedbackLabel);
            return false;
        }
        return true;
    }

    private boolean checkCredit(double amountSale) {
        // CHECK CREDIT
        if (Boolean.FALSE.equals(userService.enoughCredit(amountSale))) {
            feedbackLabel.setText("Crédito insuficiente para realizar la operación");
            Feedback.showFeedback(feedbackLabel);
            return false;
        }
        return true;
    }

    private void reloadItems() {
        //RELOAD CART BOX CONTENT
        productsBox.getChildren().clear();
        setCartItems();
    }

    private void requestCheckOutCart() {
        String alertTitle = "Procedimiento de compra";
        String content = "Se va a proceder al pago de los productos, ¿Continuar?";
        if (AlertHelper.choiceAlert(alertTitle, content)) {
            // CHECK CREDIT AND STOCK
            double amountSale = cartItems.stream().mapToDouble(CartItem::calculateSubtotal).sum();
            if (checkCredit(amountSale) && checkAvailableStock()) {
                // DO TRANSACTION
                ResultService<Void> resultTransaction = cartService.checkoutCart(currentUser.getCartItems(), amountSale);
                feedbackLabel.setText(resultTransaction.getMessage());
                Feedback.showFeedback(feedbackLabel);
                // SUCCESS
                if (resultTransaction.isSuccess()) {
                    currentUser.decreaseCredit(amountSale);
                    creditLabel.setText(String.format("%.2f €", currentUser.getCredit()));
                    clearCartItems();
                }
            }
        }
    }

    private void requestRemoveAllCartItems() {
        String alertTitle = "Eliminar del carrito";
        String content = "¿Realmente quieres eliminar todos los productos de la cesta?";
        if (AlertHelper.choiceAlert(alertTitle, content)) {

            if (cartService.removeAllCartItemsFromDB()) {
                clearCartItems();
                feedbackLabel.setText("Productos eliminados del carrito correctamente");

            } else {
                feedbackLabel.setText("Error al eliminar los productos del carrito");
            }
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private void requestRemoveCartItem(CartItem cartItem) {
        // REQUEST CONFIRM USER TO REMOVE ITEM
        String alertTitle = "Eliminar del carrito";
        String content = "¿Realmente quieres eliminar el producto de la cesta?";

        if (AlertHelper.choiceAlert(alertTitle, content)) {

            if (cartService.removeSingleCartItemFromDb(cartItem)) {
                currentUser.removeFromCart(cartItem);
                cartItems.remove(cartItem);
                feedbackLabel.setText("Producto eliminado del carrito correctamente");

            } else {
                feedbackLabel.setText("Error al eliminar producto del carrito");
            }
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private void setCartItems() {
        productsBox.getChildren().clear();
        // CREATE CARD EACH CART ITEM
        for (CartItem cartItem : cartItems) {
            VBox card = ProductCardHelper.createCartCard(cartItem, this::requestRemoveCartItem);
            productsBox.getChildren().add(card);
        }
        // FEEDBACK MESSAGE
        if (!cartItems.isEmpty()) {
            feedbackLabel.setText("Productos del carrito cargados con éxito");
            Feedback.showFeedback(feedbackLabel);
        }
    }
}
