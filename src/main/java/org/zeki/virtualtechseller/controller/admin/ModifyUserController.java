package org.zeki.virtualtechseller.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.ModifyUserDto;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.model.user.UserRole;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyUserController implements Initializable {

    @FXML
    private Button acceptBtn;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField visiblePasswordTxt;

    @FXML
    private TextField repeatVisiblePasswordTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private PasswordField repeatPassTxt;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @FXML
    private ComboBox<String> userRolCb;

    private List<TextField> textFields;

    // USER
    private Admin currentAdmin;
    private User modifyUser;
    // SERVICE
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentAdmin = (Admin) SessionManager.getInstance().getCurrentUser();
        textFields = new ArrayList<>();
        userService = AppContext.getInstance().getUserService();
    }

    private void initGUI() {
        groupTextFields();
        passTxt.textProperty().bindBidirectional(visiblePasswordTxt.textProperty());
        repeatPassTxt.textProperty().bindBidirectional(repeatVisiblePasswordTxt.textProperty());
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.PERMISSION_VIEW));

        acceptBtn.setOnAction(event -> modifyUserdata());

        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> exchangeVisibilityText(selected));
    }

    private void loadRolesOnCb() {
        // CREATE ROLE FIELDS IN COMBO BOX
        String[] roles = {"Cliente", "Administrador", "Moderador"};
        for (String role : roles) {
            userRolCb.getItems().add(role);
        }
    }

    private void selectRole(User user) {
        // SELECT DEFAULT ROLE
        UserRole userRole = user.getRoleName();
        switch (userRole) {
            case UserRole.CLIENT -> userRolCb.getSelectionModel().select(0);
            case UserRole.ADMIN -> userRolCb.getSelectionModel().select(1);
            case UserRole.MODERATOR -> userRolCb.getSelectionModel().select(2);
        }
    }

    private void exchangeVisibilityText(boolean selected) {
        // INTERCHANGE BETWEEN HIDE OR VISIBLE TXT
        visiblePasswordTxt.setVisible(selected);
        visiblePasswordTxt.setManaged(selected);
        repeatVisiblePasswordTxt.setVisible(selected);
        repeatVisiblePasswordTxt.setManaged(selected);
        passTxt.setVisible(!selected);
        passTxt.setManaged(!selected);
        repeatPassTxt.setVisible(!selected);
        repeatPassTxt.setManaged(!selected);
    }

    public void initData(User user) {
        // SET REFERENCE USER
        this.modifyUser = user;
        // SET LABELS
        userNameTxt.setText(user.getName());
        lastNameTxt.setText(user.getLastName());
        phoneTxt.setText(user.getPhone());
        emailTxt.setText(user.getEmail());
        lastNameTxt.setText(user.getLastName());
        //ROLES
        loadRolesOnCb();
        selectRole(user);
        //FEEDBACK
        feedbackLabel.setText("Datos del usuario cargados");
        Feedback.showFeedback(feedbackLabel);

    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(userNameTxt);
        textFields.add(lastNameTxt);
        textFields.add(phoneTxt);
        textFields.add(emailTxt);
    }

    private ModifyUserDto createModifyDto() {
        // CREATE NEW USER DTO
        ModifyUserDto userDto = new ModifyUserDto();
        userDto.setIdUser(modifyUser.getIdUser());
        userDto.setName(userNameTxt.getText());
        userDto.setLastName(lastNameTxt.getText());
        userDto.setPhone(phoneTxt.getText());
        userDto.setEmail(emailTxt.getText());
        userDto.setPassword(passTxt.getText());

        String selectedRole = userRolCb.getSelectionModel().getSelectedItem();

        switch (selectedRole) {
            case "Cliente" -> userDto.setUserRole(UserRole.CLIENT);
            case "Administrador" -> userDto.setUserRole(UserRole.ADMIN);
            case "Moderador" -> userDto.setUserRole(UserRole.MODERATOR);
        }
        return userDto;
    }

    private void modifyUserdata() {
        // CHECK EMPTY FIELDS
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK FORMAT PHONE
        else if (!FormularyHelper.phoneFormatValid(phoneTxt.getText())) {
            feedbackLabel.setText("Formato de teléfono no válido. Ejemplo: 600123123");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK FORMAT EMAIL
        else if (!FormularyHelper.emailFormatValid(emailTxt.getText())) {
            feedbackLabel.setText("El formato del email no es válido");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        StringBuilder content = new StringBuilder("¿Modificar los datos del usuario? ");
        // CHECK IF THEY WANT TO MODIFY PASS
        if (!passTxt.getText().isEmpty() || !repeatPassTxt.getText().isEmpty()) {
            // CHECK MATCH PASS
            if (!FormularyHelper.matchPassword(passTxt.getText().trim(), repeatPassTxt.getText().trim())) {
                feedbackLabel.setText("Las contraseñas no coinciden");
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            // CHECK FORMAT PASS
            else if (!FormularyHelper.passFormatValid(passTxt.getText())) {
                feedbackLabel.setText("La contraseña requiere 8 caracteres, mayúscula, minúscula y símbolo");
                Feedback.showFeedback(feedbackLabel);
                return;
            }
            // ADD COMMENT CONSERVATE PASS
        } else {
            content.append(" Dejando la contraseña vacía se conservará la anterior contraseña, ¿Continuar?");
        }
        String result = currentAdmin.updateUser(content.toString(), userService, createModifyDto());
        if (result != null){
            feedbackLabel.setText(result);
            Feedback.showFeedback(feedbackLabel);
        }
    }


}
