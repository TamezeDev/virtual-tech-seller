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
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientProductController implements Initializable {

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane productsBox;

    // USER
    private Client currentUser;
    private ObservableList<Sale> listSales;
    // SERVICES
    private SaleService saleService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        saleService = AppContext.getInstance().getSaleService();
        saleService.setSalesList(currentUser);
        listSales = FXCollections.observableArrayList(currentUser.getSales());
    }

    private void initGUI() {
        setSales();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));

        listSales.addListener((ListChangeListener<Sale>) change -> reloadItems());
    }

    private void reloadItems() {
        //RELOAD CART BOX CONTENT
        productsBox.getChildren().clear();
        setSales();
    }

    private void setSales() {
        for (Sale sale : listSales) {
            // COMPONENTS
            Label title = new Label(sale.getProduct().getName());
            Label quantity = new Label("Cantidad: " + sale.getQuantity());
            Label price = new Label(String.format("Precio: %.2f €", sale.getTotalPrice()));
            ImageView itemImage = new ImageView();
            //SET IMAGE
            String pathImage = sale.getProduct().getUrlImage();
            URL urlImage = getClass().getResource(pathImage);
            if (urlImage != null) {
                itemImage.setImage(new Image(urlImage.toExternalForm()));
            } else {
                String path = "/img/products/no_image.jpg";
                itemImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
            }
            VBox card = new VBox(title, itemImage, quantity, price);
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
            card.getStyleClass().add("card-a");
            card.getStyleClass().add("card-b");
            // TITLE SIZE
            title.setWrapText(true);
            title.setPrefWidth(180);
            title.setMaxWidth(180);
            title.setMinHeight(Label.USE_PREF_SIZE);
            // CARD
            card.setAlignment(Pos.CENTER);
            card.setSpacing(10);
            card.setMaxWidth(200);
            card.setPrefWidth(200);
            card.setPrefHeight(300);
            card.setMaxHeight(300);
            card.setPadding(new Insets(10, 10, 10, 10));
            productsBox.getChildren().add(card);
            // REMOVE ITEM ACTION
            card.setOnMouseClicked(event -> SceneHelper.goToSaleDetails(gobackBtn, ViewPath.DETAIL_PURCHASE_VIEW, sale));

        }
        if (!listSales.isEmpty()) {
            feedbackLabel.setText("Lista de artículos comprados cargados con éxito");
            Feedback.showFeedback(feedbackLabel);
        }
    }

}