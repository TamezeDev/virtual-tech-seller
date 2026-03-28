package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository {

    private final ConnectionManager connectionManager;

    public ProductRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public boolean availableStock(Product product, int quantity) throws DBConnectionException, SQLException {
        String query = "SELECT np.stock, p.available " +
                "FROM products p LEFT JOIN new_products np ON p.id_product = np.id_product " +
                "WHERE id_product = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, product.getIdProduct());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }

            boolean available = rs.getBoolean("available");

            if (!available) {
                return false;
            }

            if (product instanceof NewProduct) {
                return quantity <= rs.getInt("stock");
            }

            return true;
        }

    }
}

