package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;

public class ExhibitionRepository {

    private final ConnectionManager connectionManager;

    public ExhibitionRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }
}
