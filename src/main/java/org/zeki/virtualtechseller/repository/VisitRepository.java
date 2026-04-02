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
}
