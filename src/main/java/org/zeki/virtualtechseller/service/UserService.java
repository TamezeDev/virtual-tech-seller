package org.zeki.virtualtechseller.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.RegisterUserDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.Role;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.model.user.UserFactory;
import org.zeki.virtualtechseller.repository.UserRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public String registerNewUser(RegisterUserDto registerUserDto) {

        try {
            // CHECK IF EMAIL IS ALREADY REGISTERED
            if (userRepository.emailExist(registerUserDto.getEmail())) {
                return "Error: El email ya está registrado";
            }
            // ENCODE PASS
            String plainPass = registerUserDto.getPassword();
            String hashPass = BCrypt.withDefaults().hashToString(12, plainPass.toCharArray());
            registerUserDto.setPassword(hashPass);
            // REGISTER USER
            if (!userRepository.registerNewUser(registerUserDto)) {
                return "Error al registrar el usuario en el servidor";
            }
            return "Usuario registrado con éxito";

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        } catch (SQLException e) {
            String message = "Error registrando usuario en el servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }

    }

    public boolean checkRoleCurrentUser() {
        User user = SessionManager.getInstance().getCurrentUser();
        try {
            Role role = userRepository.getUserRole(user.getEmail());
            return role.equals(Role.ADMIN);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;
        } catch (SQLException e) {
            String message = "Error el rol del usuario desde servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }

    public ResultService<User> login(String email, String pass) {

        try {
            // CHECK EMAIL
            if (!userRepository.emailExist(email)) {
                return new ResultService<>(false, "Email no registrado", null);
            }
            // CHECK CREDENTIALS
            else if (!userRepository.matchCredentials(email, pass)) {
                return new ResultService<>(false, "Credenciales incorrectas", null);
            } else if (!userRepository.emailActive(email)) {
                return new ResultService<>(false, "No autorizado, contacte con un admin", null);
            }
            // GET USER ROLE
            Role role = userRepository.getUserRole(email);
            if (role == null) {
                return new ResultService<>(false, "Error obteniendo datos", null);
            }
            // GET USER DATA
            UserFactory userFactory = new UserFactory();
            User currentUser = userFactory.createUser(role);
            currentUser.setEmail(email);
            userRepository.getUserData(currentUser);

            return new ResultService<>(true, "Login correcto", currentUser);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        } catch (SQLException e) {
            String message = "Error obteniendo datos del usuario del servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }

    public String addCredit(double credit) {
        Client client = (Client) SessionManager.getInstance().getCurrentUser();

        try {
            // UPDATE CREDIT
            if (!userRepository.updateUserCredit(client, credit)) {
                return "Hubo un error al añadir el crédito";
            }
            // SET NEW CREDIT
            client.rechargeCredit(credit);
            return "Operación realizada con éxito";

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        } catch (SQLException e) {
            String message = "Error actualizando usuario en el servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }

    public Boolean enoughCredit(double amount) {
        Client client = ((Client) SessionManager.getInstance().getCurrentUser());
        try {
            return userRepository.enoughCredit(amount, client);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        } catch (SQLException e) {
            String message = "Error en consulta de crédito del cliente";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }


}
