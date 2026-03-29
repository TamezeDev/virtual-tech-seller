package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
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

    public boolean availableExhibitionStock(Product product, Exhibition exhibition, int quantity) throws DBConnectionException, SQLException {
        String query = "SELECT np.stock, p.available, ph.quantity  " +
                "FROM products p LEFT JOIN new_products np ON p.id_product = np.id_product " +
                "INNER JOIN products_exhibitions ph ON ph.id_product = p.id_product AND ph.id_exhibition = ? " +
                "WHERE p.id_product = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, exhibition.getIdExhibition());
            ps.setInt(2, product.getIdProduct());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }

            boolean available = rs.getBoolean("available");
            int stockEvent = rs.getInt("quantity");

            if (!available || (stockEvent < quantity)) {
                return false;
            }

            if (product instanceof NewProduct) {
                return quantity <= rs.getInt("stock");
            }

            return true;
        }

    }

    public int decreaseNewProductStock(Connection connection, int quantity, int idProduct) throws SQLException {
        String query = "UPDATE new_products " +
                "SET stock = stock - ? " +
                "WHERE id_product = ? AND stock >= ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, idProduct);
            ps.setInt(3, quantity);
            return ps.executeUpdate();
        }
    }

    public boolean checkSoldOutNewProduct(Connection connection, int idProduct) throws SQLException {
        String query = "SELECT stock = 0 FROM new_products WHERE id_product = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduct);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }
            return rs.getBoolean(1);
        }
    }

    public int setNoAvailableProduct(Connection connection, int idProduct) throws SQLException {
        String query = "UPDATE products SET available = 0 WHERE id_product = ? AND available = 1";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduct);
            return ps.executeUpdate();
        }
    }


}

