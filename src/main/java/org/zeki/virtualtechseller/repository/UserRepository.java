package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.Role;
import org.zeki.virtualtechseller.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public boolean matchCredentials(String email, String pass) throws DBConnectionException, SQLException {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ? AND password = ?);";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1); // MATCH CREDENTIALS! RETURN TRUE
            }
        }
        return false;
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
                Role role = Role.valueOf(rs.getString(1).toUpperCase());
                return role;
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
}