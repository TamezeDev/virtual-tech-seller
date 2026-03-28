package org.zeki.virtualtechseller.controller.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.service.SaleService;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientProductController implements Initializable {

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private FlowPane productsBox;

    // USER
    private Client currentUser;
    // SERVICES
    private SaleService saleService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentUser = (Client) SessionManager.getInstance().getCurrentUser();
        saleService = AppContext.getInstance().getSaleService();
        saleService.setSalesList(currentUser);
    }

    private void initGUI() {

    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.CLIENT_MENU_VIEW));
    }


}
