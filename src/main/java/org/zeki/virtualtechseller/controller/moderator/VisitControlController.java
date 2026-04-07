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
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.user.Moderator;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.service.VisitService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.FormularyHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisitControlController implements Initializable {

    @FXML
    private TableColumn<UserVisit, String> eventNameColumn;

    @FXML
    private ComboBox<Exhibition> eventsCb;

    @FXML
    private Label feedbackLabel;

    @FXML
    private ComboBox<String> filterVisitsCb;

    @FXML
    private Button gobackBtn;

    @FXML
    private TableColumn<UserVisit, String> lastAccessColumn;

    @FXML
    private TextField maxVisitsTxt;

    @FXML
    private TextField minVisitsTxt;

    @FXML
    private TableColumn<UserVisit, String> quantityColumn;

    @FXML
    private HBox quantityBox;

    @FXML
    private Button showFilteredBtn;

    @FXML
    private Label totalQuantityLabel;

    @FXML
    private TableColumn<UserVisit, String> userLastNameColumn;

    @FXML
    private TableColumn<UserVisit, String> userNameColumn;

    @FXML
    private HBox visitFilterBox;

    @FXML
    private TableView<UserVisit> visitsTable;

    private ObservableList<UserVisit> visitsObs;
    private List<TextField> textFields;
    // MODERATOR
    private Moderator currentModerator;
    // SERVICES
    private VisitService visitService;
    private ExhibitionService exhibitionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentModerator = (Moderator) SessionManager.getInstance().getCurrentUser();
        visitService = AppContext.getInstance().getVisitService();
        exhibitionService = AppContext.getInstance().getExhibitionService();
        visitsObs = FXCollections.observableArrayList();
        textFields = new ArrayList<>();
    }

    private void initGUI() {
        groupTextFields();
        configTable();
        loadFiltersOnCb();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.MODERATOR_MENU_VIEW));

        showFilteredBtn.setOnAction(event -> filterByVisitCount());

        filterVisitsCb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            switch (newValue) {
                case "Visitas totales" -> loadTotalVisits();
                case "Cliente por evento" -> showEventsAvailable();
                case "Cliente por acceso" -> showVisitCountBox();
                case "Totales a eventos" -> filterGroupByEvents();
            }
        });
        eventsCb.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> filterByEvent());

        maxVisitsTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        }));

        minVisitsTxt.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        }));
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

    private void filterByVisitCount() {
        // CHECK EMPTY FIELDS
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // GET INPUT VALUES
        int minValue = Integer.parseInt(minVisitsTxt.getText());
        int maxValue = Integer.parseInt(maxVisitsTxt.getText());
        quantityBox.setVisible(false);
        visitsObs.clear();
        // LOAD VISITS
        List<UserVisit> visits = currentModerator.filterVisitors(visitService, feedbackLabel, totalQuantityLabel, minValue, maxValue);
        if (!visits.isEmpty()){
            quantityBox.setVisible(true);
        }
        visitsObs.setAll(visits);
        visitsTable.setItems(visitsObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void showVisitCountBox() {
        eventsCb.setVisible(false);
        quantityBox.setVisible(false);
        visitsObs.clear();
        visitsTable.refresh();
        minVisitsTxt.clear();
        maxVisitsTxt.clear();
        visitFilterBox.setVisible(true);
    }

    private void filterByEvent() {
        // LOAD VISITS EVENT
        quantityBox.setVisible(true);
        visitFilterBox.setVisible(false);
        List<UserVisit> visits = currentModerator.filterVisitors(visitService, feedbackLabel, totalQuantityLabel, eventsCb.getSelectionModel().getSelectedItem());
        visitsObs.setAll(visits);
        visitsTable.setItems(visitsObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void showEventsAvailable() {
        // GET EVENTS LIST AND SHOW COMBO BOX
        loadEventOnCB();
        visitsObs.clear();
        visitsTable.refresh();
        visitFilterBox.setVisible(false);
        quantityBox.setVisible(false);
        eventsCb.setVisible(true);
    }

    private void loadTotalVisits() {
        // LOAD FULL VISITS TABLE
        quantityBox.setVisible(true);
        eventsCb.setVisible(false);
        visitFilterBox.setVisible(false);
        List<UserVisit> visits = currentModerator.filterVisitors(visitService, feedbackLabel, totalQuantityLabel, null);
        visitsObs.setAll(visits);
        visitsTable.setItems(visitsObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void filterGroupByEvents() {
        // LOAD GROUPED LIST
        visitFilterBox.setVisible(false);
        quantityBox.setVisible(false);
        eventsCb.setVisible(false);
        List<UserVisit> visits = currentModerator.filterGroupEvent(visitService, feedbackLabel);
        visitsObs.setAll(visits);
        visitsTable.setItems(visitsObs);
        Feedback.showFeedback(feedbackLabel);
    }

    private void loadFiltersOnCb() {
        // CREATE FILTERS FIELDS IN COMBO BOX
        String[] filters = {"Visitas totales", "Cliente por evento", "Cliente por acceso", "Totales a eventos"};
        for (String filter : filters) {
            filterVisitsCb.getItems().add(filter);
        }
    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(maxVisitsTxt);
        textFields.add(minVisitsTxt);
    }

    private void configTable() {
        // CONFIG COLUMNS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventNameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getExhibition().getName()));
        userNameColumn.setCellValueFactory(cd -> cd.getValue().getClient() == null ? new SimpleStringProperty("") : new SimpleStringProperty(cd.getValue().getClient().getName()));
        userLastNameColumn.setCellValueFactory(cd -> cd.getValue().getClient() == null ? new SimpleStringProperty("") : new SimpleStringProperty(cd.getValue().getClient().getLastName()));
        lastAccessColumn.setCellValueFactory(cd -> cd.getValue().getClient() == null ? new SimpleStringProperty("") : new SimpleStringProperty(cd.getValue().getLastVisit().format(formatter)));
        quantityColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getVisitCounter())));
    }
}