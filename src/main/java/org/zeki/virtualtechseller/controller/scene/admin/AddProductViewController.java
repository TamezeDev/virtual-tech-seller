package org.zeki.virtualtechseller.controller.scene.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductViewController implements Initializable {

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField basePriceTxt;

    @FXML
    private ComboBox<?> categoryCb;

    @FXML
    private TextField descriptTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private RadioButton newProductRb;

    @FXML
    private ImageView productImg;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField stockTxt;

    @FXML
    private RadioButton usedProductRb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
