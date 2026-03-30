package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class VisitRepository {

    private final ConnectionManager connectionManager;

    public VisitRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
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
