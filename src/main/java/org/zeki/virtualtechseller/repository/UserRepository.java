package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private final ConnectionManager connectionManager;

    public UserRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public boolean emailExist(String email) {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?);";

        try (Connection connection = connectionManager.connect(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1); // FOUND! RETURN TRUE
            }

        } catch (DBConnectionException e) {   // SHOW CONNECTION ALERT TO USER
            AlertHelper.showDBConnectAlert();

        } catch (SQLException e) {
            AlertHelper.showSQLAlert(); // SHOW SQL ALERT TO USER
        }
        return false;
    }

    public boolean matchCredentials(String email, String pass) {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ? AND password = ?);";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1); // MATCH CREDENTIALS! RETURN TRUE
            }

        } catch (DBConnectionException e) {   // SHOW CONNECTION ALERT TO USER
            AlertHelper.showDBConnectAlert();

        } catch (SQLException e) {
            AlertHelper.showSQLAlert(); // SHOW SQL ALERT TO USER
        }
        return false;
    }
}