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
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.ProductCardHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        listSales = FXCollections.observableArrayList(currentUser.getSales());
    }

    private void initGUI() {
        setSaleItems();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));

        listSales.addListener((ListChangeListener<Sale>) change -> reloadItems());
    }

    private void reloadItems() {
        //RELOAD CART BOX CONTENT
        productsBox.getChildren().clear();
        setSaleItems();
    }

    private void setSaleItems() {
        productsBox.getChildren().clear();
        // CREATE CARD FOR EACH ITEM
        for (Sale sale : listSales) {
            VBox card = ProductCardHelper.createSaleCard(sale, saleSelected -> SceneHelper.changeScene(gobackBtn, ViewPath.DETAIL_PURCHASE_VIEW, (DetailPurchaseController controller) -> controller.initData(sale)));
            productsBox.getChildren().add(card);
        }
        // FEEDBACK MESSAGE
        if (!listSales.isEmpty()) {
            feedbackLabel.setText("Productos comprados cargados con éxito");
            Feedback.showFeedback(feedbackLabel);
        }
    }

}