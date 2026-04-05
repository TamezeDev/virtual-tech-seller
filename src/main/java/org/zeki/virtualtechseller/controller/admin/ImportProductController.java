package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.zeki.virtualtechseller.model.product.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportProductController implements Initializable {

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private Button exportXmlBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button importXmlBtn;

    @FXML
    private TableView<Product> itemsTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private Button saveOnDbBtn;

    @FXML
    private TableColumn<Product, String> stockColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
