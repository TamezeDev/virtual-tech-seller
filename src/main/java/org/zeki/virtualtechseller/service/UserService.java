package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Role;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.model.user.UserFactory;
import org.zeki.virtualtechseller.repository.UserRepository;

import java.sql.SQLException;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public ResultService<User> login(String email, String pass) throws DBConnectionException, SQLException {
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
    }
}
