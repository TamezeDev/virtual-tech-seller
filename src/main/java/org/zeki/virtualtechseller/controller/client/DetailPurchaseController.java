package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailPurchaseController implements Initializable {

    @FXML
    private Label categoryLabel;

    @FXML
    private VBox descriptionBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView productImg;

    @FXML
    private Label purchaseDatelabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label tipoProduct;

    // SALE
    private Sale sale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {

    }

    private void initGUI() {
    }

    private void actions() {

    }

    public  void setCurrentSale(Sale sale){
        this.sale = sale;
    }
}
