package org.zeki.virtualtechseller.controller.client;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.ProductCardHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CatalogProductController implements Initializable {

    @FXML
    private Button cartItemBtn;

    @FXML
    private Label creditLabel;

    @FXML
    private Label numItemCartLabel;

    @FXML
    private Label catalogName;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane productsBox;
    // USER
    private Client client;
    private ObservableList<ExhibitionItem> eventProducts;
    // SERVICES
    private ExhibitionService exhibitionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        client = (Client) SessionManager.getInstance().getCurrentUser();
        exhibitionService = AppContext.getInstance().getExhibitionService();
        eventProducts = FXCollections.observableArrayList();
    }

    private void initGUI() {
        creditLabel.setText(client.getCredit() + " €");
        catalogName.setText("Catálogo de " + client.getCurrentExhibition().getName());
        // SET CART ITEMS NUMBER
        if (!client.emptyCartList()) {
            numItemCartLabel.setText("X" + client.checkQuantityCartItems() + " Artículos");
            numItemCartLabel.setVisible(true);
        }
        getEventProducts();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.EVENT_SELECT_VIEW));

        cartItemBtn.setOnAction(event -> {
            if (client.emptyCartList()) {
                feedbackLabel.setText("No tiene productos en el carrito");
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            SceneHelper.changeScene(cartItemBtn, ViewPath.CART_ITEMS_VIEW);
        });

        eventProducts.addListener((ListChangeListener<ExhibitionItem>) change -> reloadItems());
    }

    private void reloadItems() {
        //RELOAD CART BOX CONTENT
        productsBox.getChildren().clear();
        setEventProducts();
    }

    private void getEventProducts() {
        // GET EVENT PRODUCTS
        ResultService<List<ExhibitionItem>> result = exhibitionService.getProductSelectedEvent();
        String message = result.getMessage();
        // RECEIVED DATA!
        if (result.getData() != null) {
            client.getCurrentExhibition().setItems(result.getData());
            eventProducts.addAll(result.getData());
            setEventProducts();
        }
        feedbackLabel.setText(message);
        Feedback.showFeedback(feedbackLabel);
    }

    private void setEventProducts() {
        productsBox.getChildren().clear();
        for (ExhibitionItem item : eventProducts) {
            VBox card = ProductCardHelper.createExhibitionItemCard(item, exhibitionItem -> SceneHelper.changeScene(gobackBtn, ViewPath.DETAIL_PRODUCT_VIEW, (DetailProductController controller) -> controller.setCurrentProduct(item)));
            if (item.checkOutStock()) {
                productsBox.getChildren().add(ProductCardHelper.setNoAvailableImage(card));
            } else {
                productsBox.getChildren().add(card);
            }
        }
    }
}
