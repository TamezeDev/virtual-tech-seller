package org.zeki.virtualtechseller.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.UsedProduct;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;
import org.zeki.virtualtechseller.util.XmlFilesHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductsXMLController implements Initializable {

    @FXML
    private Button getProductsBtn;

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
    private ObservableList<Product> productsObs;
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

        importXmlBtn.setOnAction(this::importProducts);

        exportXmlBtn.setOnAction(event -> XmlFilesHelper.exportProducts(event,feedbackLabel, productsObs));

        getProductsBtn.setOnAction(event -> loadAllProducts());

        saveOnDbBtn.setOnAction(event -> saveProductsOnDB());
    }

    private void loadAllProducts() {
        // GET DATA
        ResultService<List<Product>> result = productService.getStockPriceProducts();
        if (result != null) {
            // SHOW DATA
            feedbackLabel.setText(result.getMessage());
            productsObs.setAll(result.getData());
            itemsTable.setItems(productsObs);
            Feedback.showFeedback(feedbackLabel);
            saveOnDbBtn.setDisable(true);
            exportXmlBtn.setDisable(false);
        }
    }

    private void saveProductsOnDB() {
        List<Product> products = new ArrayList<>(productsObs);
        // GET CATEGORY ID IF ONLY HAS NAME
        if (!productService.setIdProductsByName(products)) {
            feedbackLabel.setText("Error guardado los productos");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // INSERT BY DTO
        for (Product product : products) {
            if (product instanceof NewProduct) {
                NewProductDto productDto = new NewProductDto();
                productDto = productDto.createNewProductDTO((NewProduct) product);
                feedbackLabel.setText(currentAdmin.createProduct(productService, productDto, null));
            } else {
                UsedProductDto usedProductDto = new UsedProductDto();
                usedProductDto = usedProductDto.createUsedProductDTO((UsedProduct) product);
                feedbackLabel.setText(currentAdmin.createProduct(productService, null, usedProductDto));
            }
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private void importProducts(ActionEvent event) {
        // READ XML
        List<Product> productsXML = Objects.requireNonNull(XmlFilesHelper.importProducts(event, feedbackLabel)).getProducts();
        if (productsXML != null && !productsXML.isEmpty()) {
            // LOAD LIST
            productsObs.setAll(productsXML);
            itemsTable.setItems(productsObs);
            saveOnDbBtn.setDisable(false);
            exportXmlBtn.setDisable(false);
        }
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
