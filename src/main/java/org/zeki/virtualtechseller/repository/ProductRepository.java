package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;

public class ProductRepository {

    private final ConnectionManager connectionManager;

    public ProductRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }
}
