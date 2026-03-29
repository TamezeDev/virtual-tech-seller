package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.ExhibitionService;
import org.zeki.virtualtechseller.service.ResultService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.ProductCardHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EventSelectController implements Initializable {

    @FXML
    private FlowPane availableEventsBox;

    @FXML
    private Label creditLabel;

    @FXML
    private FlowPane endEventsBox;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane nextEventsBox;

    // USER
    private Client client;
    // SERVICES
    private ExhibitionService exhibitionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        client = (Client) SessionManager.getInstance().getCurrentUser();
        exhibitionService = AppContext.getInstance().getExhibitionService();

    }

    private void initGUI() {
        creditLabel.setText(client.getCredit() + " €");
        setEvents();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));
    }

    private void setEvents() {
        // GET EVENTS LIST
        ResultService<List<Exhibition>> result = exhibitionService.getAllExhibitions();
        String message = result.getMessage();
        if (result.getData() == null) {
            return;
        }
        createEventCart(result.getData());
        feedbackLabel.setText(message);
        Feedback.showFeedback(feedbackLabel);

    }

    private void createEventCart(List<Exhibition> exhibitions) {

        for (Exhibition exhibition : exhibitions) {
            // LABELS
            Label name = new Label(exhibition.getName());
            Label description = new Label(exhibition.getDescription());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            Label initDate = new Label("Inicio: " + exhibition.getInitDate().format(formatter));
            Label endDate = new Label("Fin: " + exhibition.getEndDate().format(formatter));
            // SET STYLES
            ProductCardHelper.setLabelsStyles(name, description, initDate, endDate);
            ProductCardHelper.setConfigLabel(name);
            ProductCardHelper.setConfigLabel(description);
            // CREATE CARD
            VBox card = new VBox(name, description, initDate, endDate);
            ProductCardHelper.setCard(card);
            card.setPrefHeight(200);
            // SELECTION BOX
            selectExhibitionBox(exhibition, card);
        }
    }

    private void selectExhibitionBox(Exhibition exhibition, VBox card) {
        LocalDate today = LocalDate.now();
        // NEXT EVENTS
        if (today.isBefore(exhibition.getInitDate())) {
            nextEventsBox.getChildren().add(card);
            return;
        }
        // CURRENT EVENTS
        if ((today.isEqual(exhibition.getInitDate()) || today.isAfter(exhibition.getInitDate()))
                && (today.isEqual(exhibition.getEndDate()) || today.isBefore(exhibition.getEndDate()))
                && exhibition.isActive()) {
            // SET HOVER STYLE
            card.getStyleClass().add("card-b");
            // ON CARD ACTION
            card.setOnMouseClicked(event -> {
                client.setCurrentExhibition(exhibition);
                SceneHelper.changeScene(card, ViewPath.CATALOG_PRODUCT_VIEW);
            });
            availableEventsBox.getChildren().add(card);
            return;
        }
        // END EVENTS
        card.setDisable(true);
        endEventsBox.getChildren().add(card);
    }

}
