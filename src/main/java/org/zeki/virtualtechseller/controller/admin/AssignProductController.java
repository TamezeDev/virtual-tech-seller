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
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionProductsDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.UsedProduct;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AssignProductController implements Initializable {

    @FXML
    private Button addProductBtn;

    @FXML
    private TableView<ExhibitionProductsDto> itemsTable;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> categoryColumn;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> descriptionColumn;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> eventColumn;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> nameColumn;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> quantityColumn;

    @FXML
    private TableColumn<ExhibitionProductsDto, String> stockColumn;

    @FXML
    private ComboBox<Exhibition> eventCb;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button retireAllBtn;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button saveChangeBtn;

    @FXML
    private Button listProductsBtn;

    @FXML
    private TextField quantityEventTxt;

    @FXML
    private Button rmProductBtn;

    @FXML
    private HBox eventBox;

    private ObservableList<ExhibitionProductsDto> productsDto;
    private ExhibitionProductsDto selectedProductDto;
    private boolean removeEvent; // IF TRUE USE METHOD TO REMOVE ASSIGNED EVENT
    // USER
    private Admin currentAdmin;
    //SERVICES
    private ExhibitionService exhibitionService;
    private ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentAdmin = (Admin) SessionManager.getInstance().getCurrentUser();
        exhibitionService = AppContext.getInstance().getExhibitionService();
        productService = AppContext.getInstance().getProductService();
        productsDto = FXCollections.observableArrayList();
    }

    private void initGUI() {
        configTable();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        listProductsBtn.setOnAction(event -> loadProductsTable());

        addProductBtn.setOnAction(event -> enableAddMode());

        rmProductBtn.setOnAction(event -> enableRemoveMode());

        retireAllBtn.setOnAction(event -> removeExhibitionProduct());

        saveChangeBtn.setOnAction(event -> {
            if (removeEvent) {
                decreaseProductEvent();
            } else {
                addProductEvent();
            }
        });

        quantityEventTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("([1-9]|[1-9][0-9])?") ? change : null;
        }));

        itemsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                selectedProductDto = newItem;
                eventBox.setVisible(false);
            }
        });
    }

    private void configTable() {
        // CONFIG COLUMNS
        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getProduct().getName()));
        descriptionColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getProduct().getDescription()));
        categoryColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getProduct().getCategory().getName()));
        stockColumn.setCellValueFactory(cd -> {
            Product product = cd.getValue().getProduct();
            if (product instanceof UsedProduct && product.isAvailable()) {
                return new SimpleStringProperty("1");
            } else if (product instanceof NewProduct) {
                return new SimpleStringProperty(String.valueOf(((NewProduct) product).getStock()));
            } else {
                return new SimpleStringProperty("0");
            }
        });

        eventColumn.setCellValueFactory(cd -> {
            Exhibition exhibition = cd.getValue().getExhibition();
            return new SimpleStringProperty(exhibition != null ? exhibition.getName() : "No asignado");
        });

        quantityColumn.setCellValueFactory(cd -> {
            ExhibitionItem item = cd.getValue().getExhibitionItem();
            return new SimpleStringProperty((item != null ? String.valueOf(item.getQuantity()) : "-"));
        });
    }

    private void enableAddMode() {
        // CHECK SELECTED PRODUCT
        selectedProductDto = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedProductDto == null) {
            feedbackLabel.setText("Debe seleccionar un producto para añadirlo a un evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // GET EXHIBITION LIST
        ResultService<List<Exhibition>> result = exhibitionService.getAllExhibitions();
        if (result.isSuccess()) {
            eventCb.getItems().clear();
            List<Exhibition> exhibitions = result.getData();
            eventCb.getItems().setAll(exhibitions);
        }
        removeEvent = false;
        eventCb.setVisible(true);
        eventBox.setVisible(true);
        retireAllBtn.setVisible(false);
        saveChangeBtn.setText("Añadir a evento");
    }

    private void enableRemoveMode() {
        // CHECK SELECTED PRODUCT
        selectedProductDto = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedProductDto == null) {
            feedbackLabel.setText("Debe seleccionar un producto para quitarlo de un evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        removeEvent = true;
        eventBox.setVisible(true);
        eventCb.setVisible(false);
        retireAllBtn.setVisible(true);
        saveChangeBtn.setText("Retirar cantidad");
    }

    private void loadProductsTable() {
        ResultService<List<ExhibitionProductsDto>> result = productService.getFullProductAssociateOrNot();
        if (result != null) {
            productsDto.setAll(result.getData());
            itemsTable.setItems(productsDto);
            feedbackLabel.setText(result.getMessage());
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private boolean invalidInputValues() {
        // CHECK QUANTITY EMPTY
        if (quantityEventTxt.getText().isBlank()) {
            feedbackLabel.setText("Debe poner el numero de productos");
            return true;
        }
        // CHECK EVENT SELECTED
        if (!removeEvent && eventCb.getSelectionModel().getSelectedItem() == null) {
            feedbackLabel.setText("Debe seleccionar una categoría");
            return true;
        }
        return false;
    }

    private void removeExhibitionProduct() {
        // REMOVE PRODUCT FROM EXHIBITION
        if (selectedProductDto.getExhibitionItem() == null) {
            feedbackLabel.setText("El producto no tiene asociado ningún evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        if (productService.removeEventItem(selectedProductDto)) {
            currentAdmin.retireProductFromExhibition(selectedProductDto);
            feedbackLabel.setText("Producto eliminado del evento");
            Feedback.showFeedback(feedbackLabel);
            itemsTable.refresh();
        }
    }

    private int getAssignedQuantity(Product product) {
        return productsDto.stream()
                .filter(dto -> dto.getProduct().getIdProduct() == product.getIdProduct() && dto.getExhibition() != null && dto.getExhibitionItem() != null)
                .mapToInt(dto -> dto.getExhibitionItem().getQuantity())
                .sum();
    }

    private int getLogicalStock(Product product) {
        if (product instanceof UsedProduct) {
            return product.isAvailable() ? 1 : 0;
        }
        return ((NewProduct) product).getStock();
    }

    private void addProductEvent() {
        // CHECK INPUTS
        if (invalidInputValues()) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        Product product = selectedProductDto.getProduct();
        // CHECK QUANTITY
        int selectedQuantity = Integer.parseInt(quantityEventTxt.getText());

        int totalStock = getLogicalStock(product);
        int totalAssigned = getAssignedQuantity(product);

        if (totalAssigned + selectedQuantity > totalStock) {
            feedbackLabel.setText("ERROR - Las unidades totales asignadas superan el stock del producto");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // UPDATE DB
        if (!exhibitionService.increaseEventItem(selectedProductDto, eventCb.getSelectionModel().getSelectedItem(), selectedQuantity)) {
            feedbackLabel.setText("No se puede actualizar producto del evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }// UPDATE LIST
        currentAdmin.assignProductToExhibition(productsDto, selectedProductDto, eventCb.getSelectionModel().getSelectedItem(), selectedQuantity);
        feedbackLabel.setText("Producto asignado con éxito");
        Feedback.showFeedback(feedbackLabel);
        itemsTable.refresh();
    }

    private void decreaseProductEvent() {
        // CHECK INPUTS
        if (invalidInputValues()) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK QUANTITIES
        int selectedQuantity = Integer.parseInt(quantityEventTxt.getText());
        if (selectedProductDto.getExhibitionItem() == null) {
            feedbackLabel.setText("No puede quitar la cantidad si el producto no está asignado a un evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        if (!selectedProductDto.getExhibitionItem().quantityAvailable(selectedQuantity)) {
            feedbackLabel.setText("No puede quitar una cantidad mayor a la disponible");
            Feedback.showFeedback(feedbackLabel);
            return;
        }// NEW QUANTITY IS ZERO -> REMOVE ITEM
        if (selectedProductDto.getExhibitionItem().newQuantityIsZero(selectedQuantity)) {
            removeExhibitionProduct();
        } else {
            // UPDATE DB
            if (!exhibitionService.decreaseEventItem(selectedProductDto, selectedQuantity)) {
                feedbackLabel.setText("No se pudo actualizar el producto del evento");
                Feedback.showFeedback(feedbackLabel);
                return;
            }// UPDATE LIST
            currentAdmin.decreaseProductFromExhibition(selectedProductDto, selectedQuantity);
            feedbackLabel.setText("Producto actualizado con éxito");
            Feedback.showFeedback(feedbackLabel);
            itemsTable.refresh();
        }
    }
}
