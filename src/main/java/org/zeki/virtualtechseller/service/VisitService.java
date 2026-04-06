package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.repository.VisitRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public ResultService<List<UserVisit>> getVisitors(Exhibition exhibition) {

        try {
            List<UserVisit> visits = visitRepository.getTotalVisits(exhibition);
            if (visits.isEmpty()) {
                return new ResultService<>(false, "La lista de visitas está vacía", visits);
            }
            return new ResultService<>(true, "Mostrando listado de visitas", visits);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error conectando con el servidor", new ArrayList<>());
        } catch (SQLException e) {
            String message = "Error recibiendo listado de visitas";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, new ArrayList<>());
        }
    }

    public ResultService<List<UserVisit>> visitsByNumAccessOrdered(int minValue, int maxValue) {

        try {
            List<UserVisit> visits = visitRepository.visitsByNumAccessOrdered(minValue, maxValue);
            if (visits.isEmpty()) {
                return new ResultService<>(false, "La lista de visitas está vacía", visits);
            }
            return new ResultService<>(true, "Mostrando listado de visitas", visits);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error conectando con el servidor", new ArrayList<>());
        } catch (SQLException e) {
            String message = "Error recibiendo listado de visitas";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, new ArrayList<>());
        }
    }

    public ResultService<List<UserVisit>> visitsGroupByEvent() {
        try {
            List<UserVisit> visits = visitRepository.visitsGroupByEvent();
            if (visits.isEmpty()) {
                return new ResultService<>(false, "La lista de visitas está vacía", visits);
            }
            for (UserVisit visit : visits) {
                if (!visitRepository.getLasterClientEventVisit(visit)) {
                    return new ResultService<>(false, "Error recibiendo datos de eventos", visits);
                }
            }
            return new ResultService<>(true, "Mostrando listado de visitas", visits);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error conectando con el servidor", new ArrayList<>());
        } catch (SQLException e) {
            String message = "Error recibiendo listado de visitas";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, new ArrayList<>());
        }
    }

    public int getAmountVisitors(Exhibition exhibition) {
        try {
            if (exhibition == null) {
                return visitRepository.getAmountVisitors();
            } else {
                return visitRepository.getAmountVisitors(exhibition);
            }

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return -1;
        } catch (SQLException e) {
            String message = "Error actualizando las visitas del cliente";
            e.printStackTrace();
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return -1;
        }
    }


}