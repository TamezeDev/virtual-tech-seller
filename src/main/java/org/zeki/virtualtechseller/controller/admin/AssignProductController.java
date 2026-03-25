package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignProductController implements Initializable {

    @FXML
    private TableView<?> ItemsTable;

    @FXML
    private Button addProductBtn;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private TableColumn<?, ?> descriptColumn;

    @FXML
    private TextField eventNameTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TextField idProductTxt;

    @FXML
    private Button listProductsBtn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField quantityTxt;

    @FXML
    private Button rmProductBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
