package org.zeki.virtualtechseller.controller.scene.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogProductController implements Initializable {


    @FXML
    private Button cartItemsBtn;

    @FXML
    private Label creditLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane productsBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
