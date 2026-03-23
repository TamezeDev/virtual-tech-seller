package org.zeki.virtualtechseller.controller.scene.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    @FXML
    private Button accessEventBtn;

    @FXML
    private Button addCreditBtn;

    @FXML
    private Button cartItemBtn;

    @FXML
    private FlowPane containerPane;

    @FXML
    private Label creditLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button myProductsBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
