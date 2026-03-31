package org.zeki.virtualtechseller.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.dto.UserAccessDto;
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

        authorizeBtn.setOnAction(event -> changeUserAccess(true));

        blockBtn.setOnAction(event -> changeUserAccess(false));

        filterCb.setOnAction(event -> filterAccessUser());

        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldUSer, newUser) -> {
            if (newUser != null) {
                selectedUser = newUser;
            }
        });
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
        List<User> usersFiltered = new ArrayList<>();
        if (users.isEmpty()) {
            feedbackLabel.setText("Error al cargar los usuarios");
            Feedback.showFeedback(feedbackLabel);
            return;
        }
        if (filterCb.getSelectionModel().isSelected(0)) return;
        if (filterCb.getSelectionModel().isSelected(1)) {
            // GET USERS ACTIVATED
            usersFiltered = users.stream().filter(user -> user.getEmailActivate().equals(true)).toList();
        }
        if (filterCb.getSelectionModel().isSelected(2)) {
            // GET USERS ACTIVATED
            usersFiltered = users.stream().filter(user -> user.getEmailActivate().equals(false)).toList();
        }
        users.setAll(usersFiltered);
        feedbackLabel.setText("Aplicado filtro seleccionado");
        Feedback.showFeedback(feedbackLabel);
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

    private void changeUserAccess(boolean access) {
        // CREATE USER DTO AND MODIFY ACTIVATE USER DB
        UserAccessDto userAccessDto = new UserAccessDto(selectedUser.getEmail(), access);
        if (!userService.changeActivateUSer(userAccessDto)) {
            feedbackLabel.setText("Error modificando acceso al usuario");
        } else {
            feedbackLabel.setText("Se ha modificado el acceso correctamente");
            listUsers();
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
