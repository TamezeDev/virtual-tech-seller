package org.zeki.virtualtechseller.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;


public final class ProductCardHelper {

    private ProductCardHelper() {
    }

    // MAIN METHOD
    private static <T> VBox createCard(T item, String titleLabel, String label2, String label3,
                                       String label4, String image, String buttonText, Consumer<T> buttonAction, Consumer<T> cardAction) {
        // CREATE LABELS
        Label title = new Label(titleLabel);
        Label quantity = new Label(label2);
        Label price = new Label(label3);
        Label extra = new Label(label4);
        // SET IMAGE
        ImageView itemImage = new ImageView();
        loadProductImage(itemImage, image);
        // SET STYLES AND CONFIG IMAGE / TITLE
        setLabelsStyles(title, quantity, price, extra);
        setConfigLabel(title);
        setImage(itemImage);
        // CREATE CARD
        VBox card = new VBox(title, itemImage, quantity, price, extra);
        setCard(card);
        if (item instanceof Sale || item instanceof Product) {
            card.getStyleClass().add("card-b");
        }
        // CREATE BUTTON ONLY WHEN NECESSARY
        if (buttonText != null && !buttonText.isEmpty()) {
            Button button = new Button(buttonText);
            button.getStyleClass().add("button-a");
            button.setOnAction(event -> {
                if (buttonAction != null) {
                    buttonAction.accept(item);
                }
            });
            card.getChildren().add(button);
        }
        // CREATE CLICK CARD ONLY WHEN NECESSARY
        if (cardAction != null) {
            card.setOnMouseClicked(event -> cardAction.accept(item));
        }

        return card;
    }

    public static VBox createCartCard(CartItem cartItem, Consumer<CartItem> onRemove) {
        // CREATE GLOBAL CART CARDS
        CartItem item = cartItem;
        String title = item.getProduct().getName();
        String quantity = "Cantidad: " + item.getQuantity();
        String price = String.format("Subtotal: %.2f €", item.calculateTotal());
        String event = "Evento: " + item.getExhibition().getName();
        String urlImage = item.getProduct().getUrlImage();
        String textButton = "Eliminar";

        return createCard(item, title, quantity, price, event, urlImage, textButton, onRemove, null);
    }

    public static VBox createSaleCard(Sale sale, Consumer<Sale> onClick) {
        // CREATE GLOBAL SALE CARDS
        String title = sale.getProduct().getName();
        String quantity = "Cantidad: " + sale.getQuantity();
        String price = String.format("Precio: %.2f €", sale.getTotalPrice());
        String event = "Evento: " + sale.getExhibition().getName();
        String urlImage = sale.getProduct().getUrlImage();

        return createCard(sale, title, quantity, price, event, urlImage, null, null, onClick);
    }

    public static VBox createExhibitionItemCard(ExhibitionItem item, Consumer<ExhibitionItem> onClick) {
        // CREATE GLOBAL EXHIBITIONS CARDS
        String title = item.getProduct().getName();
        String stock = "Stock: " + item.getQuantity();
        String price = String.format("Precio: %.2f €", item.getProduct().calculateUnitPrice());
        String category = "Categoría: " + item.getProduct().getCategory().getName();
        String urlImage = item.getProduct().getUrlImage();

        return createCard(item, title, stock, price, category, urlImage, null, null, onClick);
    }

    // AUXILIARY METHODS
    public static void setLabelsStyles(Label... labels) {
        for (Label label : labels) {
            label.getStyleClass().add("label-a");
        }
    }

    public static void setConfigLabel(Label label) {
        label.setWrapText(true);
        label.setPrefWidth(180);
        label.setMaxWidth(180);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
    }

    private static void setImage(ImageView itemImage) {
        itemImage.setFitWidth(120);
        itemImage.setFitHeight(120);
        itemImage.setPreserveRatio(true);
    }

    public static void setCard(VBox card) {
        card.getStyleClass().add("card-a");
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setMaxWidth(200);
        card.setPrefWidth(200);
        card.setPrefHeight(300);
        card.setMaxHeight(300);
        card.setPadding(new Insets(10));
    }

    public static void loadProductImage(ImageView imageView, String pathImage) {
        URL urlImage = ProductCardHelper.class.getResource(pathImage);

        if (urlImage != null) {
            imageView.setImage(new Image(urlImage.toExternalForm()));
        } else {
            String noImage = "/img/products/no_image.jpg";
            imageView.setImage(new Image(Objects.requireNonNull(ProductCardHelper.class.getResourceAsStream(noImage))));
        }
    }

}
