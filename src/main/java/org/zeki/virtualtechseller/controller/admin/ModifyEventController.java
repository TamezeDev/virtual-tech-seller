package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionModifyDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.FormularyHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyEventController implements Initializable {

    @FXML
    private TextField descriptionTxt;

    @FXML
    private DatePicker endDatePk;

    @FXML
    private TextField eventNameTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private DatePicker initDatePk;

    @FXML
    private Button modifyEventBtn;

    private List<TextField> textFields;
    // ADMIN
    private Admin currentAdmin;
    // EVENT
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
        exhibitionService = AppContext.getInstance().getExhibitionService();
        currentAdmin= (Admin) SessionManager.getInstance().getCurrentUser();
        textFields = new ArrayList<>();
    }

    private void initGUI() {
        groupTextFields();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CONTROL_EVENTS_VIEW));

        modifyEventBtn.setOnAction(event -> modifyEvent());
    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(eventNameTxt);
        textFields.add(descriptionTxt);
    }

    public void initData(Exhibition exhibition) {
        // SET REFERENCE EXHIBITION
        this.selectedExhibition = exhibition;
        // SET DATA
        eventNameTxt.setText(exhibition.getName());
        descriptionTxt.setText(exhibition.getDescription());
        initDatePk.setValue(exhibition.getInitDate());
        endDatePk.setValue(exhibition.getEndDate());
        //FEEDBACK
        feedbackLabel.setText("Mostrando datos de la exhibición");
        Feedback.showFeedback(feedbackLabel);

    }

    private ExhibitionModifyDto createExhibitionDto(){
        // CREATE EXHIBITION DATA TRANSFER OBJECT
        ExhibitionModifyDto exhibitionModifyDto = new ExhibitionModifyDto();
        exhibitionModifyDto.setIdExhibition(selectedExhibition.getIdExhibition());
        exhibitionModifyDto.setName(eventNameTxt.getText());
        exhibitionModifyDto.setDescription(descriptionTxt.getText());
        exhibitionModifyDto.setInitDate(initDatePk.getValue());
        exhibitionModifyDto.setEndDate(endDatePk.getValue());

        return exhibitionModifyDto;
    }

    private void modifyEvent() {
        // CHECK EMPTY FIELDS
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK DATE VALUES
        if (FormularyHelper.checkDates(initDatePk, endDatePk, feedbackLabel)) {
            // ADMIN MODIFY A EXHIBITION
            String result = currentAdmin.modifyExhibition(exhibitionService, createExhibitionDto());
                feedbackLabel.setText(result);
                Feedback.showFeedback(feedbackLabel);

        }
    }
}
