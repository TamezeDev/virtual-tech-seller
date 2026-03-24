package org.zeki.virtualtechseller.controller.scene.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
