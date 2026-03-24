package org.zeki.virtualtechseller.controller.scene.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailProductViewController implements Initializable {

    @FXML
    private Button addCartItemBtn;

    @FXML
    private Label availableLabel;

    @FXML
    private Button buyBtn;

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
    private HBox quantityBox;

    @FXML
    private TextField quantityTxt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
