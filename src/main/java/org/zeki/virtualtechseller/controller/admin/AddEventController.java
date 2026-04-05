package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
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

public class AddEventController implements Initializable {

    @FXML
    private Button createEventBtn;

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

    List<TextField> textFields;

    // USER
    private Admin currentAdmin;
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
        textFields = new ArrayList<>();
    }

    private void initGUI() {
        groupTextFields();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        createEventBtn.setOnAction(event -> {
            createNewEvent();
        });
    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(eventNameTxt);
        textFields.add(descriptionTxt);
    }

    private Exhibition createExhibition() {
        // CREATE NEW EXHIBITION
        Exhibition exhibition = new Exhibition();
        exhibition.setName(eventNameTxt.getText());
        exhibition.setDescription(descriptionTxt.getText());
        exhibition.setInitDate(initDatePk.getValue());
        exhibition.setEndDate(endDatePk.getValue());
        return exhibition;
    }

    private void createNewEvent() {
        // CHECK EMPTY FIELDS
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK DATE VALUES
        if (FormularyHelper.checkDates(initDatePk, endDatePk, feedbackLabel)) {
            // ADMIN CREATE A EXHIBITION
            String result = currentAdmin.createExhibition(createExhibition(), exhibitionService);
            feedbackLabel.setText(result);
        }
        Feedback.showFeedback(feedbackLabel);
    }

}
