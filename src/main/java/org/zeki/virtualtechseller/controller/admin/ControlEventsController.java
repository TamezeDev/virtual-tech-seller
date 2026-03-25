package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlEventsController implements Initializable {

    @FXML
    private HBox accessControlBOx;

    @FXML
    private Button activateEvent;

    @FXML
    private Button finishEventBtn;

    @FXML
    private TableColumn<?, ?> dateInColumn;

    @FXML
    private TableColumn<?, ?> dateOutColumn;

    @FXML
    private TableColumn<?, ?> descriptColumn;

    @FXML
    private TextField eventNameTxt;

    @FXML
    private TableView<?> eventsTable;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button listEventsBtn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
