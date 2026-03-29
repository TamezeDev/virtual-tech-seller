package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
