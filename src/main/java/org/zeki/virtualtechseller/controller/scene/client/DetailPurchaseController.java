package org.zeki.virtualtechseller.controller.scene.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
