package org.zeki.virtualtechseller.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.user.AccessUserDto;
import org.zeki.virtualtechseller.dto.user.LoginUserDto;
import org.zeki.virtualtechseller.dto.user.ModifyUserDto;
import org.zeki.virtualtechseller.dto.user.RegisterUserDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.UserRole;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.model.user.UserFactory;
import org.zeki.virtualtechseller.repository.UserRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public String registerNewUser(RegisterUserDto registerUserDto) {

        try {
            // CHECK IF EMAIL IS ALREADY REGISTERED
            if (userRepository.emailExist(registerUserDto.getEmail())) return "Error: El email ya está registrado";
            // ENCODE PASS
            String plainPass = registerUserDto.getPassword();
            String hashPass = BCrypt.withDefaults().hashToString(12, plainPass.toCharArray());
            registerUserDto.setPassword(hashPass);
            // REGISTER USER
            if (!userRepository.registerNewUser(registerUserDto)) return "Error al registrar el usuario en el servidor";
            return "Usuario registrado con éxito";

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        } catch (SQLException e) {
            String message = "Error registrando usuario en el servidor";
            e.printStackTrace();
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }

    }

    public boolean checkRoleCurrentUser() {
        User user = SessionManager.getInstance().getCurrentUser();
        try {
            UserRole USerRole = userRepository.getUserRole(user.getEmail());
            return USerRole.equals(USerRole.ADMIN);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;
        } catch (SQLException e) {
            String message = "Error el rol del usuario desde servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }

    public ResultService<User> login(LoginUserDto userDto) {

        String userEmail = userDto.getEmail();
        String plainPass = userDto.getPassword();
        // ENCODE PASS
        try {
            // CHECK EMAIL
            if (!userRepository.emailExist(userEmail)) return new ResultService<>(false, "Email no registrado", null);
            // CHECK CREDENTIALS
            String dbPass = userRepository.getUserPassword(userEmail);
            if (dbPass == null) return new ResultService<>(false, "Error obteniendo datos", null);
            BCrypt.Result result = BCrypt.verifyer().verify(plainPass.toCharArray(), dbPass);
            if (!result.verified) return new ResultService<>(false, "Credenciales incorrectas", null);
                // CHECK EMAIL ACTIVE
            else if (!userRepository.emailActive(userEmail))
                return new ResultService<>(false, "No autorizado, contacte con un admin", null);
            // GET USER ROLE
            UserRole USerRole = userRepository.getUserRole(userEmail);
            if (USerRole == null) return new ResultService<>(false, "Error obteniendo datos", null);
            // GET USER DATA
            UserFactory userFactory = new UserFactory();
            User currentUser = userFactory.createUser(USerRole);
            currentUser.setEmail(userEmail);
            userRepository.getUserData(currentUser);

            return new ResultService<>(true, "Login correcto", currentUser);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            e.printStackTrace();
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            userRepository.getAllUsers(users);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            String message = "Error recibiendo usuarios desde servidor";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
        }
        return users;
    }

    public boolean changeActivateUSer(AccessUserDto userAccessDto) {
        try {
            return userRepository.changeAccessToUser(userAccessDto.isAccess(), userAccessDto.getEmail());
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;
        } catch (SQLException e) {
            String message = "Error actualizando el estado de activo del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }

    public ResultService<Boolean> modifyUser(ModifyUserDto userDto) {
        // CHECK EMAIL AVAILABLE
        try {
            // CHECK IF EMAIL IS ALREADY REGISTERED
            if (userRepository.emailExist(userDto.getEmail(), userDto.getIdUser()))
                return new ResultService<>(false, "Error: El email ya está registrado");
            // ENCODE PASS IF PRESENT
            String plainPass = userDto.getPassword();
            if (plainPass != null && !plainPass.isBlank()) {
                String hashPass = BCrypt.withDefaults().hashToString(12, plainPass.toCharArray());
                userDto.setPassword(hashPass);
            }
            // MODIFY USER DATA
            if (!userRepository.modifyUserData(userDto))
                return new ResultService<>(false, "Error actualizando los datos del usuario");
            // MODIFY OK
            return new ResultService<>(true, "Datos de usuario actualizados correctamente");
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error conectando con el servidor");
        } catch (SQLException e) {
            String message = "Error actualizando los datos del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message);
        }
    }
}
