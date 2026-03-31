package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.repository.VisitRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;

public final class VisitService {

    private final VisitRepository visitRepository;

    public VisitService() {
        this.visitRepository = new VisitRepository();
    }

    public void updateUserVisit() {
        Client client = (Client) SessionManager.getInstance().getCurrentUser();
        try {
            visitRepository.updateUserVisits(client.getIdUser(), client.getCurrentExhibition().getIdExhibition());
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            String message = "Error actualizando las visitas del cliente";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
        }
    }
}
