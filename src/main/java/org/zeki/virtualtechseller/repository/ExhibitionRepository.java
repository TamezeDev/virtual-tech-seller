package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.database.ConnectionManager;

public class ExhibitionRepository {

    private ConnectionManager connectionManager;

    public ExhibitionRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
