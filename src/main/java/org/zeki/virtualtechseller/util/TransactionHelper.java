package org.zeki.virtualtechseller.util;

import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public final class TransactionHelper {
    private TransactionHelper() {
    }

    public static Connection begin(ConnectionManager connectionManager)
            throws SQLException, DBConnectionException {
        Connection connection = connectionManager.connect();
        connection.setAutoCommit(false);
        return connection;
    }

    public static void rollback(Connection connection) {
        if (connection == null) return;
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Error al hacer rollback: " + e.getMessage());
        }
    }

    public static void close(Connection connection) {
        if (connection == null) return;

        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar autoCommit: " + e.getMessage());
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

}
