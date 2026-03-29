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
import org.zeki.virtualtechseller.util.ProductCardHelper;
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
        seSaleItems();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));

        listSales.addListener((ListChangeListener<Sale>) change -> reloadItems());
    }

    private void reloadItems() {
        //RELOAD CART BOX CONTENT
        productsBox.getChildren().clear();
        seSaleItems();
    }

    private void seSaleItems() {
        productsBox.getChildren().clear();

        for (Sale sale : listSales) {
            VBox card = ProductCardHelper.createSaleCard(sale, saleSelected -> SceneHelper.goToSaleDetails(gobackBtn, ViewPath.DETAIL_PURCHASE_VIEW, sale));
            productsBox.getChildren().add(card);
        }

        if (!listSales.isEmpty()) {
            feedbackLabel.setText("Productos comprados cargados con éxito");
            Feedback.showFeedback(feedbackLabel);
        }
    }
    
}