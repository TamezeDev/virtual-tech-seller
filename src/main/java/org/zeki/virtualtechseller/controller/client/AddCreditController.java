package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCreditController implements Initializable {

    @FXML
    private Button acceptBtn;

    @FXML
    private HBox accessControlBOx;

    @FXML
    private Label creditLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TextField quantityAddTxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
