package org.zeki.virtualtechseller.controller.client;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.CartService;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.AlertHelper;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.Objects;
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

        clearBtn.setOnAction(event -> {
            if (cartService.removeAllCartItemsFromDB()) {
                currentUser.clearCart();
                cartItems.clear();
                reloadItems();
            }
        });
        buyBtn.setOnAction(event -> {
            if (checkCredit() && checkAvailableStock()){

            }

        });
        cartItems.addListener((ListChangeListener<CartItem>) change -> reloadItems());
    }

    private boolean checkAvailableStock() {
        // CHECK ITEM AVAILABLE AND ENOUGH STOCK
        ResultService<CartItem> result = productService.stockCartItems();
        if (result == null) {
            return false;
        } else if (!result.isSuccess()) {
            String message = result.getMessage() + result.getData().getProduct().getName();
            feedbackLabel.setText(message);
            Feedback.showFeedback(feedbackLabel);
            return false;
        }
        return true;
    }

    private boolean checkCredit() {
        // CHECK CREDIT
        double amountSale = 0;
        for (CartItem cartItem : cartItems) {
            amountSale += cartItem.calculateSubtotal();
        }
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
        for (CartItem cartItem : cartItems) {
            // COMPONENTS
            Label title = new Label(cartItem.getProduct().getName());
            Label quantity = new Label("Cantidad: " + cartItem.getQuantity());
            Label price = new Label(String.format("Subtotal: %.2f €", cartItem.calculateSubtotal()));
            ImageView itemImage = new ImageView();
            Button removeItem = new Button("Eliminar");
            //SET IMAGE
            String pathImage = cartItem.getProduct().getUrlImage();
            URL urlImage = getClass().getResource(pathImage);
            if (urlImage != null) {
                itemImage.setImage(new Image(urlImage.toExternalForm()));
            } else {
                String path = "/img/products/no_image.jpg";
                itemImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
            }
            // CONFIG IMAGE
            itemImage.setFitWidth(120);
            itemImage.setFitHeight(120);
            title.setTextAlignment(TextAlignment.CENTER);
            title.setAlignment(Pos.CENTER);
            itemImage.setPreserveRatio(true);
            // STYLES
            title.getStyleClass().add("label-a");
            quantity.getStyleClass().add("label-a");
            price.getStyleClass().add("label-a");
            removeItem.getStyleClass().add("button-a");
            // TITLE SIZE
            title.setWrapText(true);
            title.setPrefWidth(180);
            title.setMaxWidth(180);
            title.setMinHeight(Label.USE_PREF_SIZE);
            // CARD
            VBox card = new VBox(title, itemImage, quantity, price, removeItem);
            card.getStyleClass().add("card-a");
            card.setAlignment(Pos.CENTER);
            card.setSpacing(10);
            card.setMaxWidth(200);
            card.setPrefWidth(200);
            card.setPrefHeight(300);
            card.setMaxHeight(300);
            card.setPadding(new Insets(10, 10, 10, 10));
            productsBox.getChildren().add(card);
            // REMOVE ITEM ACTION
            removeItem.setOnAction(event -> requestRemoveCartItem(cartItem));

        }
        feedbackLabel.setText("Productos del carritos cargados con éxito");
        Feedback.showFeedback(feedbackLabel);
    }
}
