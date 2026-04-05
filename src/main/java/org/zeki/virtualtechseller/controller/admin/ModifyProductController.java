package org.zeki.virtualtechseller.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.UsedProduct;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

    @FXML
    private TextField addStockTxt;

    @FXML
    private TextField basePriceTxt;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private HBox changeBox;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TableView<Product> itemsTable;

    @FXML
    private Button listProductsBtn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private Button saveChangeBtn;

    @FXML
    private HBox stockBox;

    @FXML
    private TableColumn<Product, String> stockColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    private ObservableList<Product> productsObs;
    private Product selectedProduct;
    // ADMIN
    private Admin currentAdmin;
    //SERVICES
    private ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentAdmin = (Admin) SessionManager.getInstance().getCurrentUser();
        productService = AppContext.getInstance().getProductService();
        productsObs = FXCollections.observableArrayList();
    }

    private void initGUI() {
        configTable();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        listProductsBtn.setOnAction(event -> loadProductsTable());

        saveChangeBtn.setOnAction(event -> saveChanges());

        basePriceTxt.textProperty().addListener((obs, oldV, newV) -> {
            // TEXT FIELD CONTROL PATTERN
            if (!newV.matches("\\d{0,5}(\\.\\d{0,2})?")) {
                basePriceTxt.setText(oldV);
            }
        });

        addStockTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("([1-9]|[1-9][0-9])?") ? change : null;
        }));

        itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> changeVisibilityOptions(newItem));

    }

    private void saveChanges() {
        // CHECK SELECTED ITEM
        if (!validateFields()) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // SET NEW VALUES
        if (selectedProduct instanceof NewProduct) {
            if (!basePriceTxt.getText().isBlank()) {
                selectedProduct.setBasePrice(Double.parseDouble(basePriceTxt.getText()));
            }
            if (!addStockTxt.getText().isBlank()) {
                ((NewProduct) selectedProduct).setStock(((NewProduct) selectedProduct).getStock() + Integer.parseInt(addStockTxt.getText()));
            }
        } else {
            selectedProduct.setBasePrice(Double.parseDouble(basePriceTxt.getText()));
        }
        // UPDATE DB
        if (!productService.updateProduct(selectedProduct)) {
            feedbackLabel.setText("Hubo un error al actualizar los datos");
        } else {
            feedbackLabel.setText("Datos actualizados con éxito");
        }
        addStockTxt.setText("");
        basePriceTxt.setText("");
        itemsTable.refresh();
        Feedback.showFeedback(feedbackLabel);
    }

    private boolean validateFields() {
        // CHECK SELECTED ITEM
        if (selectedProduct == null) {
            feedbackLabel.setText("Debe seleccionar un artículo primero");
            return false;
        }
        // CHECK EMPTY VALUES
        if (selectedProduct instanceof UsedProduct) {
            if (basePriceTxt.getText().isBlank()) {
                feedbackLabel.setText("Debe rellenar el precio nuevo del producto");
                return false;
            }
        } else {
            if (basePriceTxt.getText().isBlank() && addStockTxt.getText().isBlank()) {
                feedbackLabel.setText("Debe rellenar el dato de los que quiere modificar");
                return false;
            }
        }
        return true;
    }

    private void loadProductsTable() {
        // GET DATA
        ResultService<List<Product>> result = productService.getStockPriceProducts();
        if (result != null) {
            // SHOW DATA
            feedbackLabel.setText(result.getMessage());
            productsObs.setAll(result.getData());
            itemsTable.setItems(productsObs);
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private void changeVisibilityOptions(Product newItem) {
        // MAKE VISIBLE ADD STOCK VALUE ONLY ON NEW PRODUCTS
        if (newItem != null) {
            selectedProduct = newItem;
        }
        if (selectedProduct instanceof UsedProduct) {
            stockBox.setVisible(false);
        } else stockBox.setVisible(true);
        changeBox.setVisible(true);
    }

    private void configTable() {
        // CONFIG COLUMNS
        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        descriptionColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDescription()));
        categoryColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCategory().getName()));
        priceColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getBasePrice())));
        stockColumn.setCellValueFactory(cd -> {
            if (cd.getValue() instanceof UsedProduct && cd.getValue().isAvailable()) {
                return new SimpleStringProperty("1");
            } else if (cd.getValue() instanceof NewProduct) {
                return new SimpleStringProperty(String.valueOf(((NewProduct) cd.getValue()).getStock()));
            } else {
                return new SimpleStringProperty("0");
            }
        });

        typeColumn.setCellValueFactory(cd -> {
            if (cd.getValue() instanceof NewProduct) {
                return new SimpleStringProperty("Nuevo");
            } else return new SimpleStringProperty("Usado");
        });

    }


}
