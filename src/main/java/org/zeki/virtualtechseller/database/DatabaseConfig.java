package org.zeki.virtualtechseller.database;

import lombok.Getter;

@Getter
public class DatabaseConfig {

    private final String URL = "jdbc:mysql://localhost:3306/virtual_tech_seller_db";
    private final String USER = "root";
    private final String PASS = "";
}
