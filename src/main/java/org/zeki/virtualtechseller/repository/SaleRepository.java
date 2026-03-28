package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.*;
import org.zeki.virtualtechseller.model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    private final ConnectionManager connectionManager;

    public SaleRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public List<Sale> getSalesByUser(User user) throws SQLException, DBConnectionException {
        String query = "SELECT s.quantity, s.total_price, s.purchase_date, p.name, p.description, p.id_product, " +
                "p.url_image, c.name AS cat_name, c.id_category, np.id_product AS new_id, up.id_product AS used_id, e.name " +
                "FROM sales s INNER JOIN products p ON p.id_product = s.id_product " +
                "LEFT JOIN new_products np ON np.id_product = p.id_product " +
                "LEFT JOIN used_products up ON up.id_product = p.id_product " +
                "INNER JOIN categories c ON c.id_category = p.id_category " +
                "INNER JOIN exhibitions e ON e.id_exhibition = s.id_exhibition " +
                "WHERE s.id_user = ?;";

        List<Sale> sales = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, user.getIdUser());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sale sale = new Sale();
                sale.setQuantity(rs.getInt("quantity"));
                sale.setTotalPrice(rs.getDouble("total_price"));
                sale.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
                // CREATE PRODUCT (CHECK TYPE)
                Product product = null;

                if (rs.getObject("new_id") != null) {
                    product = new NewProduct();
                } else if (rs.getObject("used_id") != null) {
                    product = new UsedProduct();
                }

                product.setIdProduct(rs.getInt("id_product"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUrlImage(rs.getString("url_image"));
                // CREATE CATEGORY
                Category category = new Category();
                category.setIdCategory(rs.getInt("id_category"));
                category.setName(rs.getString("name"));
                product.setCategory(category);
                sale.setProduct(product);
                // CREATE EXHIBITION
                Exhibition exhibition = new Exhibition();
                exhibition.setName(rs.getString("cat_name"));
                sale.setExhibition(exhibition);
                sales.add(sale);
            }
        }
        return sales;

    }

    public boolean saveSale(Connection connection, Sale sale) throws SQLException {
        String query = "INSERT INTO sales(id_user, id_product, id_exhibition, quantity, total_price, purchase_date) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, sale.getClient().getIdUser());
            ps.setInt(2, sale.getProduct().getIdProduct());
            ps.setInt(3, sale.getExhibition().getIdExhibition());
            ps.setInt(4, sale.getQuantity());
            ps.setDouble(5, sale.getTotalPrice());
            ps.setDate(6, Date.valueOf(sale.getPurchaseDate()));

            return ps.executeUpdate() > 0;
        }
    }
}
