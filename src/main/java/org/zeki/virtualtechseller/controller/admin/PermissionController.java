package org.zeki.virtualtechseller.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.AccessUserDto;
import org.zeki.virtualtechseller.model.user.Admin;
import org.zeki.virtualtechseller.model.user.Moderator;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.service.UserService;
import org.zeki.virtualtechseller.util.Feedback;
import org.zeki.virtualtechseller.util.SceneHelper;
import org.zeki.virtualtechseller.util.ViewPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PermissionController implements Initializable {

    @FXML
    private Button authorizeBtn;

    @FXML
    private Button blockBtn;
    @FXML
    private Label feedbackLabel;

    @FXML
    private Button gobackBtn;

    @FXML
    private Button listUsersBtn;

    @FXML
    private Button modifyBtn;

    @FXML
    private ComboBox<String> filterCb;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> accessColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> categoryColumn;

    // USER
    private Admin currentAdmin;
    private ObservableList<User> users;
    private User selectedUser;
    //SERVICES
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        currentAdmin =(Admin) SessionManager.getInstance().getCurrentUser();
        userService = AppContext.getInstance().getUserService();
        users = FXCollections.observableArrayList();
    }

    private void initGUI() {
        configCombo();
        configTable();
    }

    private void actions() {
        gobackBtn.setOnAction(event -> SceneHelper.changeScene(gobackBtn, ViewPath.ADMIN_MENU_VIEW));

        listUsersBtn.setOnAction(event -> listUsers());

        authorizeBtn.setOnAction(event -> changeAccess(true));

        blockBtn.setOnAction(event -> changeAccess(false));

        modifyBtn.setOnAction(event -> changeToModifyScene());

        filterCb.setOnAction(event -> filterAccessUser());

        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldUSer, newUser) -> {
            if (newUser != null) {
                selectedUser = newUser;
            }
        });
    }

    private void changeToModifyScene() {
        // GO TO MODIFY SCENE IF USER IS SELECTED
        if (selectedUser == null) {
            feedbackLabel.setText("Para esta operación debe seleccionar un usuario");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        SceneHelper.changeScene(modifyBtn, ViewPath.MODIFY_USER_VIEW, (ModifyUserController controller) -> controller.initData(selectedUser));
    }

    private void configTable() {
        // CONFIG COLUMNS
        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        lastNameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getLastName()));
        emailColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getEmail()));
        categoryColumn.setCellValueFactory(cd -> new SimpleStringProperty(checkRoles(cd.getValue())));
        accessColumn.setCellValueFactory(cd -> new SimpleStringProperty(checkAccess(cd.getValue())));

    }

    private void configCombo() {
        // CREATE FIELDS OPTIONS IN COMBO BOX
        String[] fields = {"Filtrar", "Autorizados", "Bloqueados"};
        for (String role : fields) {
            filterCb.getItems().add(role);
        }
        filterCb.getSelectionModel().select(0);
    }

    private void filterAccessUser() {
        // UPDATE LIST
        users.setAll(userService.getAllUsers());
        if (users.isEmpty()) {
            feedbackLabel.setText("Error al cargar los usuarios");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // APPLY FILTERS
        if (checkSelectedFilter()) {
            feedbackLabel.setText("Aplicado filtro seleccionado");
            Feedback.showFeedback(feedbackLabel);
        }
    }

    private boolean checkSelectedFilter() {
        // FILTERED LIST
        List<User> usersFiltered = new ArrayList<>();
        if (filterCb.getSelectionModel().isSelected(0)) return false;
        else if (filterCb.getSelectionModel().isSelected(1)) {
            // GET USERS ACTIVATED
            usersFiltered = users.stream().filter(user -> user.getEmailActivate().equals(true)).toList();
        } else if (filterCb.getSelectionModel().isSelected(2)) {
            // GET USERS ACTIVATED
            usersFiltered = users.stream().filter(user -> user.getEmailActivate().equals(false)).toList();
        }
        users.setAll(usersFiltered);
        return true;
    }

    private String checkRoles(User user) {
        // CONVERT ROLE TO SPANISH STRING
        if (user instanceof Moderator) {
            return "Moderador";
        } else if (user instanceof Admin) {
            return "Administrador";
        } else return "Cliente";
    }

    private String checkAccess(User user) {
        return Boolean.TRUE.equals(user.getEmailActivate()) ? "Permitido" : "Bloqueado";
    }

    private void changeAccess(boolean access) {
        String result = currentAdmin.changeUserAccess(access, selectedUser, userService);
        feedbackLabel.setText(result);
        if (result.equals("OK")){
            feedbackLabel.setText("Operación cancelada por el administrador");
            listUsers();
            checkSelectedFilter();
        }
        Feedback.showFeedback(feedbackLabel);
    }

    private void listUsers() {
        // GET USERS
        users.setAll(userService.getAllUsers());
        if (users.isEmpty()) {
            feedbackLabel.setText("Error al cargar los usuarios");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        // SET IN TABLE
        usersTable.setItems(users);
        if (selectedUser == null) {
            feedbackLabel.setText("Mostrando listado total de usuarios");
            Feedback.showFeedback(feedbackLabel);
        }
    }
}
