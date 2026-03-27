package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.FormularyHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import javax.swing.text.View;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCreditController implements Initializable {

    @FXML
    private Button acceptBtn;

    @FXML
    private Label creditLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TextField quantityAddTxt;
    // USER
    private Client currentUser;
    // SERVICE
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        userService = AppContext.getInstance().getUserService();
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
    }

    private void initGUI() {
        creditLabel.setText(currentUser.getCredit() + " €");
    }

    private void actions() {

        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));

        acceptBtn.setOnAction(event -> {
            // CHECK QUANTITY  VALID -> 0.00 < X >= 1000.00
            double addQuantity = validCredit();
            if (addQuantity != -1) setNewCredit(addQuantity);
        });

        quantityAddTxt.textProperty().addListener((obs, oldV, newV) -> {
            // TEXT FIELD CONTROL PATTERN
            if (!newV.matches("\\d{0,4}(\\.\\d{0,2})?")) {
                quantityAddTxt.setText(oldV);
            }
        });
    }

    private double validCredit() {
        String quantity = quantityAddTxt.getText();
        try {
            double parseQuantity = Double.parseDouble(quantity);
            if (parseQuantity < 1 || parseQuantity > 1000) {
                throw new NumberFormatException();
            }
            return parseQuantity;
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Debe introducir un valor entre 1 y 1000");
            Feedback.showFeedback(feedbackLabel);
            return -1;
        }
    }

    private void setNewCredit(double addQuantity) {
        // COMMUNICATE WITH DB AND RELOAD CREDIT
        String result = userService.addCredit(addQuantity);
        feedbackLabel.setText(result);
        Feedback.showFeedback(feedbackLabel);
        String formatedCredit = String.format("%.2f €", currentUser.getCredit());
        creditLabel.setText(formatedCredit);
    }
}
