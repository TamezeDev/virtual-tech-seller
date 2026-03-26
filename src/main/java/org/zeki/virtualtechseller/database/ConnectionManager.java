package org.zeki.virtualtechseller.database;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final DatabaseConfig databaseConfig;

    public ConnectionManager() {
        this.databaseConfig = new DatabaseConfig();
    }

    public Connection connect() throws DBConnectionException {
        try {
            return DriverManager.getConnection(databaseConfig.getURL(), databaseConfig.getUSER(), databaseConfig.getPASS());
        } catch (SQLException e) {
            throw new DBConnectionException("No se ha podido establecer la conexión con el servidor", e);
        }
    }
}