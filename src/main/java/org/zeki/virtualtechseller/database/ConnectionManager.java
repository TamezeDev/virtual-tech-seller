package org.zeki.virtualtechseller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final String URL = "jdbc:mysql://localhost:3306/virtual_tech_seller_db";
    private final String USER = "root";
    private final String PASS = "";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}