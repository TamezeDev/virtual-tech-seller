package org.zeki.virtualtechseller.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.virtualtechseller.model.product.CartItem;

import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

public class ProductCardHelper {

    private ProductCardHelper() {
    }

    public static VBox createCartCard(CartItem cartItem, Consumer<CartItem> onRemove, Consumer<CartItem> onClick) {
        // COMPONENTS
        Label title = new Label(cartItem.getProduct().getName());
        Label quantity = new Label("Cantidad: " + cartItem.getQuantity());
        Label price = new Label(String.format("Subtotal: %.2f €", cartItem.calculateSubtotal()));
        Label exhibition = new Label("Evento: " + cartItem.getExhibition().getName());
        //SET IMAGE
        ImageView itemImage = new ImageView();
        loadProductImage(itemImage, cartItem.getProduct().getUrlImage());

        Button removeItem = new Button("Eliminar");
        // STYLES
        title.getStyleClass().add("label-a");
        quantity.getStyleClass().add("label-a");
        price.getStyleClass().add("label-a");
        exhibition.getStyleClass().add("label-a");
        removeItem.getStyleClass().add("button-a");
        // TITLE SIZE
        title.setWrapText(true);
        title.setPrefWidth(180);
        title.setMaxWidth(180);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        // CONFIG IMAGE
        itemImage.setFitWidth(120);
        itemImage.setFitHeight(120);
        itemImage.setPreserveRatio(true);
        // CARD
        VBox card = new VBox(title, itemImage, quantity, price, exhibition, removeItem);
        card.getStyleClass().add("card-a");
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setMaxWidth(200);
        card.setPrefWidth(200);
        card.setPrefHeight(300);
        card.setMaxHeight(300);
        card.setPadding(new Insets(10));

        removeItem.setOnAction(event -> onRemove.accept(cartItem));
        card.setOnMouseClicked(event -> onClick.accept(cartItem));

        return card;
    }

    private static void loadProductImage(ImageView imageView, String pathImage) {
        URL urlImage = ProductCardHelper.class.getResource(pathImage);

        if (urlImage != null) {
            imageView.setImage(new Image(urlImage.toExternalForm()));
        } else {
            String fallback = "/img/products/no_image.jpg";
            imageView.setImage(new Image(Objects.requireNonNull(
                    ProductCardHelper.class.getResourceAsStream(fallback)
            )));
        }
    }

}
