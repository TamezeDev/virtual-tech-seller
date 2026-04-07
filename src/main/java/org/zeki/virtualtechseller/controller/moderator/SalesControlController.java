package org.zeki.virtualtechseller.controller.moderator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Sale;
import org.zeki.virtualtechseller.model.user.Moderator;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class SalesControlController implements Initializable {

    @FXML
    private HBox dateFilterBox;

    @FXML
    private DatePicker endDatePk;

    @FXML
    private TableColumn<Sale, String> eventColumn;

    @FXML
    private ComboBox<Exhibition> eventsCb;

    @FXML
    private Label feedbackLabel;

    @FXML
    private ComboBox<String> filterSalesCb;

    @FXML
    private Button gobackBtn;

    @FXML
    private DatePicker initDatePk;

    @FXML
    private TableColumn<Sale, String> priceColumn;

    @FXML
    private TableColumn<Sale, String> productColumn;

    @FXML
    private HBox quantityBox;

    @FXML
    private TableColumn<Sale, String> quantityColumn;

    @FXML
    private TableColumn<Sale, String> saleDateColumn;

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private Button showFilteredBtn;

    @FXML
    private Label totalQuantityLabel;

    @FXML
    private TableColumn<Sale, String> userColumn;

    private ObservableList<Sale> salesObs;
    // MODERATOR
    private Moderator currentModerator;
    // SERVICES
    private ExhibitionService exhibitionService;
    private SaleService saleService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentModerator = (Moderator) SessionManager.getInstance().getCurrentUser();
        exhibitionService = AppContext.getInstance().getExhibitionService();
        saleService = AppContext.getInstance().getSaleService();
        salesObs = FXCollections.observableArrayList();
    }

    private void initGUI() {
        configTable();
        loadFiltersOnCb();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.MODERATOR_MENU_VIEW));

        showFilteredBtn.setOnAction(event -> filterBetweenDates());

        filterSalesCb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            switch (newValue) {
                case "Total ventas" -> loadTotalSales();
                case "Ventas por evento" -> showEventsAvailable();
                case "Venta entre fechas" -> showDateFilterBox();
            }
        });

        eventsCb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterByEvent());
    }

    private void filterByEvent() {
        // LOAD SALES EVENT
        quantityBox.setVisible(true);
        dateFilterBox.setVisible(false);
        List<Sale> visits = currentModerator.filterSales(saleService, feedbackLabel, totalQuantityLabel, eventsCb.getSelectionModel().getSelectedItem());
        salesObs.setAll(visits);
        salesTable.setItems(salesObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void loadTotalSales() {
        // LOAD FULL SALES TABLE
        quantityBox.setVisible(true);
        eventsCb.setVisible(false);
        dateFilterBox.setVisible(false);
        List<Sale> visits = currentModerator.filterSales(saleService, feedbackLabel, totalQuantityLabel, null);
        salesObs.setAll(visits);
        salesTable.setItems(salesObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void showDateFilterBox() {
        eventsCb.setVisible(false);
        quantityBox.setVisible(false);
        salesObs.clear();
        salesTable.refresh();
        dateFilterBox.setVisible(true);
    }

    private void filterBetweenDates() {
        // CHECK DATE FIELDS
        if (initDatePk.getValue() == null || endDatePk.getValue() == null) {
            feedbackLabel.setText("Debe indicar las fechas de filtro inicio y fin");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // GET INPUT VALUES
        LocalDate initDate = initDatePk.getValue();
        LocalDate endDate = endDatePk.getValue();
        quantityBox.setVisible(false);
        salesObs.clear();
        // LOAD SALES
        List<Sale> sales = currentModerator.filterSales(saleService, feedbackLabel, totalQuantityLabel, initDate, endDate);
        if (!sales.isEmpty()){
            quantityBox.setVisible(true);
        }
        salesObs.setAll(sales);
        salesTable.setItems(salesObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void showEventsAvailable() {
        // GET EVENTS LIST AND SHOW COMBO BOX
        loadEventOnCB();
        salesObs.clear();
        salesTable.refresh();
        dateFilterBox.setVisible(false);
        quantityBox.setVisible(false);
        eventsCb.setVisible(true);
    }

    private void loadFiltersOnCb() {
        // CREATE FILTERS FIELDS IN COMBO BOX
        String[] filters = {"Total ventas", "Ventas por evento", "Venta entre fechas"};
        for (String filter : filters) {
            filterSalesCb.getItems().add(filter);
        }
    }

    private void loadEventOnCB() {
        // GET EXHIBITION LIST
        ResultService<List<Exhibition>> result = exhibitionService.getAllExhibitions();
        if (result.isSuccess()) {
            eventsCb.getItems().clear();
            List<Exhibition> exhibitions = result.getData();
            eventsCb.getItems().setAll(exhibitions);
        }
    }

    private void configTable() {
        // CONFIG COLUMNS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getExhibition().getName()));
        userColumn.setCellValueFactory(cd -> cd.getValue().getClient() == null ? new SimpleStringProperty("") : new SimpleStringProperty(cd.getValue().getClient().getFullName()));
        productColumn.setCellValueFactory(cd -> cd.getValue().getProduct() == null ? new SimpleStringProperty("") : new SimpleStringProperty(cd.getValue().getProduct().getName()));
        priceColumn.setCellValueFactory(cd -> cd.getValue() == null ? new SimpleStringProperty("") : new SimpleStringProperty(String.valueOf(cd.getValue().getTotalPrice())));
        saleDateColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getPurchaseDate().format(formatter)));
        quantityColumn.setCellValueFactory(cd -> cd.getValue() == null ? new SimpleStringProperty("") : new SimpleStringProperty(String.valueOf(cd.getValue().getQuantity())));
    }
}
