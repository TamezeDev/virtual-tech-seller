package org.zeki.virtualtechseller.controller.scene.moderator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SalesControlViewController implements Initializable {

    @FXML
    private TableColumn<?, ?> eventColumn;

    @FXML
    private Button exportXmlBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private ComboBox<?> filterVisitsCb;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button importXmlBtn;

    @FXML
    private Button listSalesBtn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TableColumn<?, ?> productColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TableColumn<?, ?> saleDateColumn;

    @FXML
    private TableView<?> salesTable;

    @FXML
    private Button showFilteredBtn;

    @FXML
    private TableColumn<?, ?> userColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
