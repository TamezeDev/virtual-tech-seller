package org.zeki.virtualtechseller.database;

import lombok.Getter;
import org.zeki.virtualtechseller.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class ConnectionManager {

    private final DatabaseConfig databaseConfig;

    public ConnectionManager() {
        this.databaseConfig = new DatabaseConfig();
    }

    public Connection connect() throws DBConnectionException {
        try {
            return DriverManager.getConnection(databaseConfig.getURL(), databaseConfig.getCurrentUser(), databaseConfig.getCurrentPass());
        } catch (SQLException e) {
            throw new DBConnectionException("No se ha podido establecer la conexión con el servidor", e);
        }
    }
}