package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.database.ConnectionManager;

public class UserRepository {

    private ConnectionManager connectionManager;

    public UserRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
