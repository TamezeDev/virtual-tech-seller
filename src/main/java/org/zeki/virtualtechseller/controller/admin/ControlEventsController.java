package org.zeki.virtualtechseller.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionAccessDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControlEventsController implements Initializable {

    @FXML
    private Button activateEvent;

    @FXML
    private Button finishEventBtn;

    @FXML
    private TableColumn<Exhibition, String> dateInColumn;

    @FXML
    private TableColumn<Exhibition, String> dateOutColumn;

    @FXML
    private TableColumn<Exhibition, String> descriptionColumn;

    @FXML
    private TableColumn<Exhibition, String> nameColumn;

    @FXML
    private TableColumn<Exhibition, String> statusColumn;

    @FXML
    private TableView<Exhibition> eventsTable;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button listEventsBtn;

    @FXML
    private Button modifyEventBtn;

    // USER
    private Admin currentAdmin;
    private ObservableList<Exhibition> exhibitions;
    private Exhibition selectedExhibition;
    // SERVICES
    private ExhibitionService exhibitionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentAdmin = (Admin) SessionManager.getInstance().getCurrentUser();
        exhibitionService = AppContext.getInstance().getExhibitionService();
        exhibitions = FXCollections.observableArrayList();
    }

    private void initGUI() {
        configTable();
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        listEventsBtn.setOnAction(event -> listExhibitions());

        activateEvent.setOnAction(event -> changeEventStatus(true));

        finishEventBtn.setOnAction(event -> changeEventStatus(false));
        modifyEventBtn.setOnAction(event -> {
            if (selectedExhibition == null) {
                feedbackLabel.setText("Para esta operación debe seleccionar un evento");
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            SceneHelper.changeScene(modifyEventBtn, ViewPath.MODIFY_EVENTS_VIEW, (ModifyEventController controller) -> controller.initData(selectedExhibition));
        });


        eventsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldEv, newEv) -> {
            if (newEv != null) {
                selectedExhibition = newEv;
            }
        });

    }

    private void configTable() {
        // CONFIG COLUMNS
        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        descriptionColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDescription()));
        dateInColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getInitDate())));
        dateOutColumn.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getEndDate())));
        statusColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().checkActive()));

    }

    private ExhibitionAccessDto createExhibitionDTO() {
        // CREATE EXHIBITION OBJECT TRANSFER
        ExhibitionAccessDto exhibitionAccessDto = new ExhibitionAccessDto();
        exhibitionAccessDto.setIdExhibition(selectedExhibition.getIdExhibition());
        exhibitionAccessDto.setActive(selectedExhibition.isActive());
        return exhibitionAccessDto;
    }

    private void changeEventStatus(boolean access) {
        // CHECK IF SELECTED EXHIBITION
        if (selectedExhibition == null) {
            feedbackLabel.setText("Para esta operación debe seleccionar un evento");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHANGE ACCESS
        if (access) {
            currentAdmin.enableExhibition(selectedExhibition);
        } else {
            currentAdmin.disableExhibition(selectedExhibition);
        }
        // CHANGE DB EVENT STATUS
        if (!exhibitionService.changeActivateUSer(createExhibitionDTO())) {
            feedbackLabel.setText("Hubo un error cambiando el estado del evento");

        } else {
            feedbackLabel.setText("Acceso a exhibición modificado correctamente");
            listExhibitions();
        }
        Feedback.showFeedback(feedbackLabel);
    }

    private void listExhibitions() {
        // GET EXHIBITIONS
        ResultService<List<Exhibition>> result = exhibitionService.getAllExhibitions();
        exhibitions.setAll(result.getData());
        if (exhibitions == null || exhibitions.isEmpty()) {
            feedbackLabel.setText(result.getMessage());
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        eventsTable.setItems(exhibitions);
        if (selectedExhibition == null) {
            feedbackLabel.setText(result.getMessage());
            Feedback.showFeedback(feedbackLabel);
        }
    }
}
