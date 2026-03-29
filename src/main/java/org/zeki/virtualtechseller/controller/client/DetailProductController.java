package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.UsedProduct;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.CartService;
import org.zeki.virtualtechseller.util.*;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DetailProductController implements Initializable {

    @FXML
    private Button addCartItemBtn;

    @FXML
    private Label availableLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private VBox descriptionBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label typeProduct;

    @FXML
    private HBox quantityBox;

    @FXML
    private TextField quantityTxt;

    @FXML
    private ImageView productImg;

    // EVENT PRODUCT
    private ExhibitionItem item;
    // CLIENT
    private Client client;
    // SERVICES
    private CartService cartService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        actions();
    }

    private void instances() {
        client = (Client) SessionManager.getInstance().getCurrentUser();
        cartService = AppContext.getInstance().getCartService();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CATALOG_PRODUCT_VIEW));

        addCartItemBtn.setOnAction(event -> requestCheckOutCart());

        quantityTxt.textProperty().addListener((obs, oldV, newV) -> {
            // TEXT FIELD CONTROL PATTERN
            if (!newV.matches("\\d*")) {
                quantityTxt.setText(oldV);
                return;
            }
            if (!newV.isEmpty()) {
                int quantity = Integer.parseInt(quantityTxt.getText());
                int available = Integer.parseInt(availableLabel.getText().replaceAll("[^\\d]", ""));
                if (quantity > available || quantity == 0) {
                    quantityTxt.setText(oldV);
                }
            }
        });
    }

    private void requestCheckOutCart() {
        String alertTitle = "Añadir al carrito";
        String content = "Se va a añadir el producto al carrito, ¿Continuar?";
        if (AlertHelper.choiceAlert(alertTitle, content)) {
            int quantity;
            if (item.getProduct() instanceof NewProduct) {
                quantity = Integer.parseInt(quantityTxt.getText());
            } else {
                quantity = 1;
            }
            // ADD PRODUCT TO CART
            if (cartService.addToCartItem(item, quantity)) {
                cartService.setCartItemList(client);
                feedbackLabel.setText("Producto añadido al carrito");
            } else {
                feedbackLabel.setText("Ocurrió un error al añadir el producto al carrito");
            }
            Feedback.showFeedback(feedbackLabel);
        }
    }

    public void setCurrentProduct(ExhibitionItem item) {
        // SET REFERENCE ITEM
        this.item = item;
        // SET LABELS
        nameLabel.setText("Nombre: " + item.getProduct().getName());
        descriptionLabel.setText("Descripción: " + item.getProduct().getDescription());
        categoryLabel.setText("Categoría: " + item.getProduct().getCategory().getName());
        priceLabel.setText("Importe: " + String.format("%.2f €", item.getProduct().calculateUnitPrice()));
        availableLabel.setText("Disponible: " + item.getQuantity());
        // CHECK TYPE PRODUCT
        if (item.getProduct() instanceof NewProduct) {
            typeProduct.setText("Tipo de producto: Nuevo");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            Label releaseDate = new Label("Fecha de lanzamiento: " + ((NewProduct) item.getProduct()).getReleaseDate().format(formatter));
            releaseDate.getStyleClass().add("label-m");
            descriptionBox.getChildren().add(4, releaseDate);

        } else if (item.getProduct() instanceof UsedProduct) {
            typeProduct.setText("Tipo de producto: Usado");
            Label remark = new Label("Reseña: " + ((UsedProduct) item.getProduct()).getRemark());
            Label discount = new Label("Descuento aplicado: " + ((UsedProduct) item.getProduct()).getDiscountPercentage() + " %");
            remark.getStyleClass().add("label-m");
            discount.getStyleClass().add("label-m");
            descriptionBox.getChildren().remove(quantityBox);
            descriptionBox.getChildren().add(4, discount);
            descriptionBox.getChildren().add(5, remark);

        }
        // SET IMAGE
        ProductCardHelper.loadProductImage(productImg, item.getProduct().getUrlImage());
        //FEEDBACK
        feedbackLabel.setText("Detalles del producto seleccionado");
        Feedback.showFeedback(feedbackLabel);
    }
}
