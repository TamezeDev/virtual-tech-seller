package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.product.UsedProduct;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.ProductCardHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DetailPurchaseController implements Initializable {

    @FXML
    private Label categoryLabel;

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
    private Label typeProduct;

    // SALE
    private Sale sale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actions();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_PRODUCT_VIEW));
    }

    public void initData(Sale sale) {
        // SET REFERENCE ITEM
        this.sale = sale;
        // SET LABELS
        nameLabel.setText("Nombre: " + sale.getProduct().getName());
        descriptionLabel.setText("Descripción: " + sale.getProduct().getDescription());
        categoryLabel.setText("Categoría: " + sale.getProduct().getCategory().getName());
        priceLabel.setText("Importe: " + String.format("%.2f €", sale.getTotalPrice()));
        quantityLabel.setText("Unidades: " + sale.getQuantity());
        eventLabel.setText("Evento: " + sale.getExhibition().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        purchaseDatelabel.setText("Fecha de compra: " + sale.getPurchaseDate().format(formatter));

        if (sale.getProduct() instanceof NewProduct) {
            typeProduct.setText("Tipo de producto: Nuevo");
        } else if (sale.getProduct() instanceof UsedProduct) {
            typeProduct.setText("Tipo de producto: Usado");
        }
        // SET IMAGE
        ProductCardHelper.loadProductImage(productImg, sale.getProduct().getUrlImage());
        //FEEDBACK
        feedbackLabel.setText("Detalles del producto seleccionado");
        Feedback.showFeedback(feedbackLabel);

    }
}

