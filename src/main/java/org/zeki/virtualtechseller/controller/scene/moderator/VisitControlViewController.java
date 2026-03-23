package org.zeki.virtualtechseller.controller.scene.moderator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class VisitControlViewController implements Initializable {

    @FXML
    private TableColumn<?, ?> eventNameColumn;

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
    private TableColumn<?, ?> lastAccessColumn;

    @FXML
    private Button listVisitsBtn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private Button showFilteredBtn;

    @FXML
    private TableColumn<?, ?> userLastNameColumn;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TableView<?> visitsTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
