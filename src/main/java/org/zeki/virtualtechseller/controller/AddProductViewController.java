package org.zeki.virtualtechseller.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private ImageView productImg;

    @FXML
    private TextField productNameTxt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
