package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;

public class VisitRepository {

    private final ConnectionManager connectionManager;

    public VisitRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }
}
