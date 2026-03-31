package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.dto.RegisterUserDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.Role;
import org.zeki.virtualtechseller.model.user.User;

import java.sql.*;
import java.time.LocalDate;

public class UserRepository {

    private final ConnectionManager connectionManager;

    public UserRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public boolean emailExist(String email) throws DBConnectionException, SQLException {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?);";

        try (Connection connection = connectionManager.connect(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1); // FOUND! RETURN TRUE
            }
        }
        return false;
    }

    public String getUserPassword(String email) throws DBConnectionException, SQLException {
        String query = "SELECT password FROM users WHERE email = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1); // RETURN PASS TO COMPARE
            }
        }
        return null;
    }

    public boolean emailActive(String email) throws DBConnectionException, SQLException {
        String query = "SELECT email_activate FROM users WHERE email = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1); // EMAIL ACTIVE! RETURN TRUE
            }
        }
        return false;
    }

    public Role getUserRole(String email) throws DBConnectionException, SQLException {
        String query = "SELECT rol FROM users WHERE email = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Role.valueOf(rs.getString(1).toUpperCase());
            }
        }
        return null;
    }

    public void getUserData(User user) throws DBConnectionException, SQLException {

        String query = "SELECT id_user, name, last_name, phone, credit FROM users WHERE email = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setIdUser(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setPhone(rs.getString(4));

                if (user instanceof Client) {
                    ((Client) user).setCredit(rs.getDouble(5));
                }
            }
        }
    }

    public boolean updateUserCredit(Client client, double credit) throws DBConnectionException, SQLException {
        String query = "UPDATE users SET credit = credit + ? WHERE id_user = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, credit);
            ps.setDouble(2, client.getIdUser());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean enoughCredit(double amount, Client client) throws DBConnectionException, SQLException {
        String query = "SELECT credit >= ? FROM users WHERE id_user = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setInt(2, client.getIdUser());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // RETURN TRUE HAVE ENOUGH CREDIT
            }

            return false;
        }
    }

    public int decreaseCredit(Connection connection, double amount, int idUser) throws SQLException {
        String quey = "UPDATE users SET credit = credit - ? WHERE id_user = ? AND credit >= ?;";
        try (PreparedStatement ps = connection.prepareStatement(quey)) {
            ps.setDouble(1, amount);
            ps.setInt(2, idUser);
            ps.setDouble(3, amount);
            return ps.executeUpdate();
        }
    }

    public boolean registerNewUser(RegisterUserDto registerUserDto) throws DBConnectionException, SQLException {
        String query = "INSERT INTO users(name, last_name, phone, email, password, rol, created_date, credit, email_activate) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?);";

        try (Connection connection = connectionManager.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String today = String.valueOf(LocalDate.now());
            Role role = registerUserDto.getUserRole();
            boolean emailActivate = false;

            preparedStatement.setString(1, registerUserDto.getName());
            preparedStatement.setString(2, registerUserDto.getLastName());
            preparedStatement.setString(3, registerUserDto.getPhone());
            preparedStatement.setString(4, registerUserDto.getEmail());
            preparedStatement.setString(5, registerUserDto.getPassword());
            preparedStatement.setString(6, String.valueOf(role));
            preparedStatement.setString(7, today);

            if (role.equals(Role.ADMIN)) {
                emailActivate = true;
            }
            preparedStatement.setBoolean(8, emailActivate);

            return preparedStatement.executeUpdate() > 0;
        }
    }

}