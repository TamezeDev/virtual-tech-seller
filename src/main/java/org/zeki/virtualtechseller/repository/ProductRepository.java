package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.database.ConnectionManager;

public class ProductRepository {

    private ConnectionManager connectionManager;

    public ProductRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
