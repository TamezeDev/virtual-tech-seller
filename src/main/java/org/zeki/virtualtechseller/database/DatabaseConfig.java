package org.zeki.virtualtechseller.database;

import lombok.Getter;

@Getter
public class DatabaseConfig {

    private final String URL = "jdbc:mysql://localhost:3306/virtual_tech_seller_db";
    private String currentUser = "default_user";
    private String currentPass = "default123";

    public void useDefaultConnection() {
        currentUser = "default_user";
        currentPass = "default123";
    }

    public void useClientConnection() {
        currentUser = "client_user";
        currentPass = "client123";
    }

    public void useAdminConnection() {
        currentUser = "admin_user";
        currentPass = "admin123";
    }

    public void useModeratorConnection() {
        currentUser = "moderator_user";
        currentPass = "moderator123";
    }

}
