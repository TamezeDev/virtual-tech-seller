package org.zeki.virtualtechseller.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyProductViewController implements Initializable {

    @FXML
    private Button acceptBtn;

    @FXML
    private RadioButton availableRb;

    @FXML
    private TextField descriptTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TextField nameProductTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private RadioButton noAvailableRn;

    @FXML
    private TextField priceTxt;

    @FXML
    private Button searchBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
