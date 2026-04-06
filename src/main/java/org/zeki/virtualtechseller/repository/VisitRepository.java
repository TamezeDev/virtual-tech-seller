package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.user.Client;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisitRepository {

    private final ConnectionManager connectionManager;

    public VisitRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public List<UserVisit> getUserVisits(Client client) throws DBConnectionException, SQLException {
        String query = "SELECT e.id_exhibition, e.name, e.description, e.init_date, e.end_date, e.active, uv.last_visit, uv.visit_counter " +
                "FROM user_visits uv INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition " +
                "WHERE uv.id_user = ?;";
        List<UserVisit> userVisits = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, client.getIdUser());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserVisit userVisit = new UserVisit();
                Exhibition exhibition = new Exhibition();

                exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                exhibition.setName(rs.getString("name"));
                exhibition.setDescription(rs.getString("description"));
                exhibition.setInitDate(rs.getDate("init_date").toLocalDate());
                exhibition.setEndDate(rs.getDate("end_date").toLocalDate());
                exhibition.setActive(rs.getBoolean("active"));
                userVisit.setLastVisit(rs.getDate("last_visit").toLocalDate());
                userVisit.setVisitCounter(rs.getInt("visit_counter"));

                userVisit.setClient(client);
                userVisit.setExhibition(exhibition);
                userVisits.add(userVisit);

            }
            return userVisits;
        }
    }

    public boolean updateUserVisits(int idUser, int idExhibition) throws DBConnectionException, SQLException {

        String query = "INSERT INTO user_visits(id_user, id_exhibition, last_visit, visit_counter) VALUES " +
                "(?, ?, ?, 1) ON DUPLICATE KEY UPDATE visit_counter = visit_counter + 1, last_visit = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            LocalDate today = LocalDate.now();
            ps.setInt(1, idUser);
            ps.setInt(2, idExhibition);
            ps.setDate(3, Date.valueOf(today));
            ps.setDate(4, Date.valueOf(today));

            return ps.executeUpdate() > 0;
        }
    }

    public List<UserVisit> getTotalVisits(Exhibition selectedExhibition) throws DBConnectionException, SQLException {
        String fullQuery = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, uv.last_visit, uv.visit_counter " +
                "FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition;";

        String eventQuery = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, uv.last_visit, uv.visit_counter " +
                "FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition WHERE uv.id_exhibition = ?;";

        List<UserVisit> userVisits = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = selectedExhibition == null ? connection.prepareStatement(fullQuery)
                     : connection.prepareStatement(eventQuery)) {

            if (selectedExhibition != null) {
                ps.setInt(1, selectedExhibition.getIdExhibition());
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserVisit visit = new UserVisit();
                Client user = new Client();
                Exhibition exhibition = new Exhibition();

                user.setIdUser(rs.getInt("id_user"));
                user.setName(rs.getString("user_name"));
                user.setLastName(rs.getString("last_name"));
                exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                exhibition.setName(rs.getString("event_name"));
                visit.setLastVisit(rs.getDate("last_visit").toLocalDate());
                visit.setVisitCounter(rs.getInt("visit_counter"));

                visit.setExhibition(exhibition);
                visit.setClient(user);
                userVisits.add(visit);
            }
        }
        return userVisits;
    }

    public List<UserVisit> visitsGroupByEvent() throws DBConnectionException, SQLException {
        String query = "SELECT e.id_exhibition, e.name, (SELECT SUM(uv.visit_counter) " +
                "FROM user_visits uv WHERE uv.id_exhibition = e.id_exhibition) AS amount_visit " +
                "FROM exhibitions e WHERE e.id_exhibition IN ( SELECT uv.id_exhibition FROM user_visits uv ) " +
                "ORDER BY amount_visit DESC;";
        List<UserVisit> userVisits = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserVisit visit = new UserVisit();
                Exhibition exhibition = new Exhibition();

                exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                exhibition.setName(rs.getString("name"));
                visit.setVisitCounter(rs.getInt("amount_visit"));

                visit.setExhibition(exhibition);
                userVisits.add(visit);
            }
        }
        return userVisits;
    }

    public boolean getLasterClientEventVisit(UserVisit visit) throws DBConnectionException, SQLException {
        String query = "SELECT u.id_user, u.name, u.last_name, e.id_exhibition, MAX(uv.last_visit) AS last_visit_exhibition " +
                "FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON e.id_exhibition = uv.id_exhibition " +
                "GROUP BY e.id_exhibition HAVING e.id_exhibition = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, visit.getExhibition().getIdExhibition());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Client client = new Client();
                client.setIdUser(rs.getInt("id_user"));
                client.setName(rs.getString("name"));
                client.setLastName(rs.getString("last_name"));
                visit.setLastVisit(rs.getDate("last_visit_exhibition").toLocalDate());
                visit.setClient(client);
                return true;
            }
            return false;
        }
    }

    public int getAmountVisitors() throws DBConnectionException, SQLException {
        String query = "SELECT SUM(visit_counter) AS amount_visits FROM user_visits;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("amount_visits");
            }
            return -1;
        }
    }

    public int getAmountVisitors(Exhibition exhibition) throws DBConnectionException, SQLException {
        String query = "SELECT SUM(visit_counter) AS amount_visits FROM user_visits WHERE id_exhibition = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, exhibition.getIdExhibition());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("amount_visits");
            }
            return -1;
        }
    }

    public List<UserVisit> visitsByNumAccessOrdered(int minValue, int maxValue) throws DBConnectionException, SQLException {
        String query = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, uv.last_visit, uv.visit_counter " +
                "FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition WHERE uv.visit_counter BETWEEN ? AND ? ORDER BY uv.visit_counter DESC;";

        List<UserVisit> userVisits = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, minValue);
            ps.setInt(2, maxValue);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserVisit visit = new UserVisit();
                Client user = new Client();
                Exhibition exhibition = new Exhibition();

                user.setIdUser(rs.getInt("id_user"));
                user.setName(rs.getString("user_name"));
                user.setLastName(rs.getString("last_name"));
                exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                exhibition.setName(rs.getString("event_name"));
                visit.setLastVisit(rs.getDate("last_visit").toLocalDate());
                visit.setVisitCounter(rs.getInt("visit_counter"));

                visit.setExhibition(exhibition);
                visit.setClient(user);
                userVisits.add(visit);
            }
        }
        return userVisits;
    }
}
