package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.repository.VisitRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.List;

public final class VisitService {

    private final VisitRepository visitRepository;

    public VisitService() {
        this.visitRepository = new VisitRepository();
    }

    public ResultService<List<UserVisit>> getAllUserVisits(Client client) {
        try {
            List<UserVisit> userVisits = visitRepository.getUserVisits(client);
            if (userVisits.isEmpty()) {
                return new ResultService<>(true, "El cliente no ha visitado ningún evento", userVisits);
            }
            return new ResultService<>(true, "Mostrando lista de visitas del cliente", userVisits);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error conectando con el servidor", null);
        } catch (SQLException e) {
            String message = "Error recibiendo visitas del cliente";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, null);
        }
    }

    public boolean updateUserVisit() {
        Client client = (Client) SessionManager.getInstance().getCurrentUser();
        try {
            return visitRepository.updateUserVisits(client.getIdUser(), client.getCurrentExhibition().getIdExhibition());
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;
        } catch (SQLException e) {
            String message = "Error actualizando las visitas del cliente";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }
}