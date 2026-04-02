package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
