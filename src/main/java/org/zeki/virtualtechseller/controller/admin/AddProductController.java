package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.product.CategoryDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ProductService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AddProductController implements Initializable {

    @FXML
    private Button addCategoryBtn;

    @FXML
    private TextField categoryNameTxt;

    @FXML
    private TextField categoryDescriptionTxt;

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField basePriceTxt;

    @FXML
    private ComboBox<Category> categoryCb;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private RadioButton newProductRb;

    @FXML
    private ImageView productImg;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField stockTxt;

    @FXML
    private RadioButton usedProductRb;

    @FXML
    private DatePicker initDatePk;

    @FXML
    private TextField discountTxt;

    @FXML
    private TextField remarkTxt;

    private ToggleGroup typeGroup;
    private List<TextField> textFields;
    private File selectedFile;
    // ADMIN
    private Admin currentAdmin;
    // SERVICE
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
        textFields = new ArrayList<>();
        typeGroup = new ToggleGroup();

    }

    private void initGUI() {
        newProductRb.setToggleGroup(typeGroup);
        usedProductRb.setToggleGroup(typeGroup);
        newProductRb.setSelected(true);
        loadCategoriesCombo();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        addCategoryBtn.setOnAction(event -> addNewCategory());

        addProductBtn.setOnAction(event -> addProduct());

        productImg.setOnMousePressed(event -> selectedFile = ImageHelper.setImage(event, productImg));

        typeGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> enableFields(newV));

        stockTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("([1-9]|[1-9][0-9])?") ? change : null;
        }));

        discountTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("^$|^([0-9]|[1-9][0-9])(\\.[0-9]?)?$") ? change : null;
        }));

        basePriceTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("^$|^(?!(0|0\\.0{0,2})$)\\d*(\\.\\d{0,2})?$") ? change : null;
        }));
    }

    private void addNewCategory() {
        // CHECK EMPTY FIELDS
        if (categoryNameTxt.getText().isBlank() || categoryDescriptionTxt.getText().isBlank()) {
            feedbackLabel.setText("Debe completar los campos nombre y descripción de la categoría");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // ADD PRODUCT
        ResultService<Void> result = productService.addCategory(new CategoryDto(categoryNameTxt.getText(), categoryDescriptionTxt.getText()));
        if (result != null) {
            feedbackLabel.setText(result.getMessage());
            // IF OK RELOAD CATEGORIES BOX
            if (result.isSuccess()) {
                categoryCb.getItems().clear();
                loadCategoriesCombo();
            }
            Feedback.showFeedback(feedbackLabel);
        }

    }

    private NewProductDto createNewProductDTO() {
        // CREATE NEW PRODUCT TRANSFER OBJECT
        NewProductDto productDto = new NewProductDto();

        productDto.setName(productNameTxt.getText());
        productDto.setDescription(descriptionTxt.getText());
        productDto.setBasePrice(Double.parseDouble(basePriceTxt.getText()));
        productDto.setCategory(categoryCb.getValue());
        productDto.setStock(Integer.parseInt(stockTxt.getText()));
        productDto.setReleaseDate(initDatePk.getValue());

        String cleanImgName = selectedFile.getName().replace(" ", "_");
        productDto.setUrlImage("/img/products/" + cleanImgName);

        return productDto;
    }

    private UsedProductDto createUsedProductDTO() {
        // CREATE NEW PRODUCT TRANSFER OBJECT
        UsedProductDto productDto = new UsedProductDto();

        productDto.setName(productNameTxt.getText());
        productDto.setDescription(descriptionTxt.getText());
        productDto.setBasePrice(Double.parseDouble(basePriceTxt.getText()));
        productDto.setCategory(categoryCb.getValue());
        productDto.setDiscountPercentage(Double.parseDouble(discountTxt.getText()));
        productDto.setRemark(remarkTxt.getText());

        String cleanImgName = selectedFile.getName().replace(" ", "_");
        productDto.setUrlImage("/img/products/" + cleanImgName);

        return productDto;
    }

    private void addProduct() {
        textFields.clear();
        // CHECK VALUES COMPLETES
        if (!checkCompleteAllFields()) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CREATE NEW PRODUCT
        if (newProductRb.isSelected()) {
            feedbackLabel.setText(currentAdmin.createProduct(productService, createNewProductDTO(), null));
        }
        //CREATE USED PRODUCT
        else if (usedProductRb.isSelected()) {
            feedbackLabel.setText(currentAdmin.createProduct(productService, null, createUsedProductDTO()));
        }
        Feedback.showFeedback(feedbackLabel);
    }

    private boolean checkCompleteAllFields() {
        // CHECK CATEGORY
        if (categoryCb.getSelectionModel().getSelectedItem() == null) {
            feedbackLabel.setText("Tiene que seleccionar una categoría");
            return false;
        }
        // CHECK IMG SELECTED
        if (selectedFile == null) {
            feedbackLabel.setText("Tiene que seleccionar una imagen de producto");
            return false;
        }
        // CHECK COPY IMG TO RESOURCES
        if (!ImageHelper.getUrlCopyPhoto(selectedFile)) {
            feedbackLabel.setText("Error copiando ruta de la imagen");
            return false;
        }
        // CHECK DATE FIELDS & EMPTY FIELDS
        return checkSelectedProduct() && !FormularyHelper.emptyFields(textFields, feedbackLabel);
    }

    private boolean checkSelectedProduct() {
        boolean passed = false;
        // PACK TEXT FIELDS
        if (newProductRb.isSelected()) {
            passed = true;
            groupTextFields();
            textFields.add(stockTxt);
            // CHECK DATE FIELD
            if (initDatePk.getValue() == null) {
                feedbackLabel.setText("Tiene que introducir la fecha de lanzamiento");
                passed = false;
            } else if (initDatePk.getValue().isAfter(LocalDate.now())) {
                feedbackLabel.setText("No se puede vender un producto que aún no ha salido");
                passed = false;
            }
        } else if (usedProductRb.isSelected()) {
            textFields.add(discountTxt);
            textFields.add(remarkTxt);
            passed = true;
        }
        return passed;
    }

    private void groupTextFields() {
        // GROUP RELATED TEXT FIELDS
        textFields.add(productNameTxt);
        textFields.add(descriptionTxt);
        textFields.add(basePriceTxt);
    }

    private void enableFields(Toggle newV) {
        // CHECK WHICH RB IS SELECTED TO HABILITE FIELDS
        if (newV == newProductRb) {
            stockTxt.setDisable(false);
            initDatePk.setDisable(false);
            discountTxt.setDisable(true);
            remarkTxt.setDisable(true);
            remarkTxt.setText(null);
            discountTxt.setText(null);
        } else if (newV == usedProductRb) {
            stockTxt.setDisable(true);
            initDatePk.setDisable(true);
            discountTxt.setDisable(false);
            remarkTxt.setDisable(false);
            stockTxt.setText(null);
            initDatePk.setValue(null);
        }
    }

    private void loadCategoriesCombo() {
        // LOAD ALL AVAILABLE CATEGORIES
        List<Category> categories = productService.getAllCategories();
        if (categories.isEmpty()) {
            feedbackLabel.setText("Error categorías desde el servidor");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        categories.forEach(category -> categoryCb.getItems().add(category));
    }


}
