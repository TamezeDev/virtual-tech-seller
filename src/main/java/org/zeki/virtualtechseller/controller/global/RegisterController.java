package org.zeki.virtualtechseller.controller.global;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.RegisterUserDto;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.FormularyHelper;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button clearBtn;

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
    private PasswordField repeatPassTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private Button registerBtn;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private TextField userNameTxt;

    @FXML
    private ComboBox<String> userRolCb;

    @FXML
    private TextField visiblePasswordTxt;

    @FXML
    private TextField repeatVisiblePasswordTxt;

    private List<TextField> textFields;
    //SERVICES
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        userService = AppContext.getInstance().getUserService();
        textFields = new ArrayList<>();
    }

    private void initGUI() {
        groupTextFields();
        passTxt.textProperty().bindBidirectional(visiblePasswordTxt.textProperty());
        repeatPassTxt.textProperty().bindBidirectional(repeatVisiblePasswordTxt.textProperty());
        // CHECK IF USER IS ADMIN TO SHOW MENU TO CREATE SELECTED ROLES
        if (SessionManager.getInstance().isLogged() && userService.checkRoleCurrentUser()) {
            loadRolesOnCb();
            userRolCb.setVisible(true);
        }

    }

    private void actions() {
        gobackBtn.setOnAction(event -> loadBackScene());

        clearBtn.setOnAction(event -> FormularyHelper.clearFields(textFields));

        registerBtn.setOnAction(event -> registerNewUser());

        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> exchangeVisibilityText(selected));

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

    private void loadBackScene() {
        // CHECK IF CURRENT USER IS ADMIN
        if (SessionManager.getInstance().isLogged() && userService.checkRoleCurrentUser()) {
            SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW);
        } else {
            SceneHelper.changeScene(gobackBtn, ViewPath.START_VIEW);
        }
    }

    private void loadRolesOnCb() {
        // CREATE ROLE FIELDS IN COMBO BOX
        String[] roles = {"Cliente", "Administrador", "Moderador"};
        for (String role : roles) {
            userRolCb.getItems().add(role);
        }
        userRolCb.getSelectionModel().select(0);
    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(userNameTxt);
        textFields.add(lastNameTxt);
        textFields.add(phoneTxt);
        textFields.add(emailTxt);
        textFields.add(passTxt);
        textFields.add(repeatPassTxt);
        textFields.add(visiblePasswordTxt);
        textFields.add(repeatVisiblePasswordTxt);

    }

    private RegisterUserDto createRegisterUserDto() {
        // SET DATA REGISTER USER TRANSFER OBJECT
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setName(userNameTxt.getText().trim());
        userDto.setLastName(lastNameTxt.getText().trim());
        userDto.setPhone(phoneTxt.getText().trim());
        userDto.setEmail(emailTxt.getText().trim());
        userDto.setPassword(passTxt.getText().trim());
        userDto.setUserRole(FormularyHelper.getSelectedRole(userRolCb.getSelectionModel().getSelectedItem()));

        return userDto;
    }

    private void registerNewUser() {
        // CHECK EMPTY FIELDS
        if (FormularyHelper.emptyFields(textFields, feedbackLabel)) {
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CHECK MATCH PASS
        else if (!FormularyHelper.matchPassword(passTxt.getText().trim(), repeatPassTxt.getText().trim())) {
            feedbackLabel.setText("Las contraseñas no coinciden");
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
        // CHECK FORMAT PASS
        else if (!FormularyHelper.passFormatValid(passTxt.getText())) {
            feedbackLabel.setText("La contraseña requiere 8 caracteres, mayúscula, minúscula y símbolo");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // CREATE USER DTO
        RegisterUserDto userDto = createRegisterUserDto();
        // REGISTER USER
        String resultMessage = userService.registerNewUser(userDto);
        if (resultMessage != null) {
            feedbackLabel.setText(resultMessage);
            Feedback.showFeedback(feedbackLabel);
        }

    }
}
