package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.exception.DuplicateExhibitionNameException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Category> getAllProductCategories() throws DBConnectionException, SQLException {
        String query = "SELECT id_category, name, description FROM categories";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setIdCategory(rs.getInt("id_category"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));

                categories.add(category);
            }

        }
        return categories;
    }

    public int getLastProductID(Connection connection) throws SQLException {
        String query = "SELECT LAST_INSERT_ID();";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        }
        return -1;
    }

    public int addProduct(Connection connection, NewProductDto productDto, UsedProductDto usedProductDto) throws SQLException, DuplicateExhibitionNameException {
        String query = "INSERT INTO products(name, id_category, description, url_image, base_price, available) VALUES (?, ?, ?, ?, ?, 1);";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            if (usedProductDto == null) {
                ps.setString(1, productDto.getName());
                ps.setInt(2, productDto.getCategory().getIdCategory());
                ps.setString(3, productDto.getDescription());
                ps.setString(4, productDto.getUrlImage());
                ps.setDouble(5, productDto.getBasePrice());

            } else if (productDto == null) {
                ps.setString(1, usedProductDto.getName());
                ps.setInt(2, usedProductDto.getCategory().getIdCategory());
                ps.setString(3, usedProductDto.getDescription());
                ps.setString(4, usedProductDto.getUrlImage());
                ps.setDouble(5, usedProductDto.getBasePrice());
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new DuplicateExhibitionNameException("El nombre o la imagen del producto deben ser únicos y ya están en uso");
            }
            throw new SQLException();
        }

    }

    public int addNewProduct(Connection connection, NewProductDto productDto) throws SQLException {
        String query = "INSERT INTO new_products(id_product, stock, release_date) VALUES (?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, productDto.getIdProduct());
            ps.setInt(2, productDto.getStock());
            ps.setDate(3, Date.valueOf(productDto.getReleaseDate()));
            return ps.executeUpdate();
        }

    }

    public int addUsedProduct(Connection connection, UsedProductDto productDto) throws SQLException {
        String query = "INSERT INTO used_products(id_product, discount, remark) VALUES (?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, productDto.getIdProduct());
            ps.setDouble(2, productDto.getDiscountPercentage());
            ps.setString(3, productDto.getRemark());
            return ps.executeUpdate();
        }

    }

}

