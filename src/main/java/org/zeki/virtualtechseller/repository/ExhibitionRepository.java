package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionRepository {

    private final ConnectionManager connectionManager;

    public ExhibitionRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public int decreaseExhibitionStock(Connection connection, int idProduct, int idExhibition, int quantity) throws SQLException {
        String query = "UPDATE products_exhibitions SET quantity = quantity - ? WHERE id_exhibition = ? AND id_product = ? AND quantity >= ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, idExhibition);
            ps.setInt(3, idProduct);
            ps.setInt(4, quantity);
            return ps.executeUpdate();
        }
    }

    public List<Exhibition> getExhibitionsData() throws DBConnectionException, SQLException {
        String query = "SELECT id_exhibition, name, description, init_date, end_date, active FROM exhibitions";
        List<Exhibition> exhibitions = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Exhibition exhibition = new Exhibition();
                exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                exhibition.setName(rs.getString("name"));
                exhibition.setDescription(rs.getString("description"));
                exhibition.setInitDate(rs.getDate("init_date").toLocalDate());
                exhibition.setEndDate(rs.getDate("end_date").toLocalDate());
                exhibition.setActive(rs.getBoolean("active"));
                exhibitions.add(exhibition);
            }
        }

        return exhibitions;
    }
}
