package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.*;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    private final ConnectionManager connectionManager;

    public SaleRepository() {
        this.connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public List<Sale> getSalesByUser(User user) throws SQLException, DBConnectionException {
        String query = "SELECT s.quantity, s.total_price, s.purchase_date, p.name, p.description, p.id_product, " +
                "p.url_image, c.name AS cat_name, c.id_category, np.id_product AS new_id, up.id_product AS used_id, e.name AS event_name " +
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
                category.setName(rs.getString("cat_name"));
                product.setCategory(category);
                sale.setProduct(product);
                // CREATE EXHIBITION
                Exhibition exhibition = new Exhibition();
                exhibition.setName(rs.getString("event_name"));
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

    public List<Sale> getTotalSales(Exhibition selectedExhibition) throws DBConnectionException, SQLException {
        String fullQuery = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, " +
                "e.name AS event_name, s.quantity, s.total_price, s.purchase_date, p.id_product, p.name AS prod_name " +
                "FROM sales s INNER JOIN users u ON s.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON s.id_exhibition = e.id_exhibition " +
                "INNER JOIN products p ON p.id_product = s.id_product ORDER BY user_name ASC;";

        String eventQuery = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, " +
                "e.name AS event_name, s.quantity, s.total_price, s.purchase_date, p.id_product, p.name AS prod_name " +
                "FROM sales s INNER JOIN users u ON s.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON s.id_exhibition = e.id_exhibition " +
                "INNER JOIN products p ON p.id_product = s.id_product WHERE e.id_exhibition = ? ORDER BY user_name ASC;";

        List<Sale> sales = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = selectedExhibition == null ? connection.prepareStatement(fullQuery)
                     : connection.prepareStatement(eventQuery)) {

            if (selectedExhibition != null) {
                ps.setInt(1, selectedExhibition.getIdExhibition());
            }

            ResultSet rs = ps.executeQuery();
            createAnalystSale(rs, sales);
        }
        return sales;
    }

    public int getAmountEarnings() throws DBConnectionException, SQLException {
        String query = "SELECT SUM(total_price) AS earning_sales FROM sales;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("earning_sales");
            }
            return -1;
        }
    }

    public int getAmountEarnings(Exhibition exhibition) throws DBConnectionException, SQLException {
        String query = "SELECT SUM(total_price) AS earning_sales FROM sales WHERE id_exhibition = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, exhibition.getIdExhibition());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("earning_sales");
            }
            return -1;
        }
    }

    public List<Sale> salesBetweenDates(LocalDate initDate, LocalDate endDate) throws DBConnectionException, SQLException {
        String query = "SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, " +
                "e.name AS event_name, s.quantity, s.total_price, s.purchase_date, p.id_product, p.name AS prod_name " +
                "FROM sales s INNER JOIN users u ON s.id_user = u.id_user " +
                "INNER JOIN exhibitions e ON s.id_exhibition = e.id_exhibition " +
                "INNER JOIN products p ON p.id_product = s.id_product WHERE s.purchase_date BETWEEN ? AND ? ORDER BY user_name ASC;";

        List<Sale> sales = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDate(1, Date.valueOf(initDate));
            ps.setDate(2, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            createAnalystSale(rs, sales);
        }
        return sales;
    }

    private static void createAnalystSale(ResultSet rs, List<Sale> sales) throws SQLException {
        while (rs.next()) {
            Sale sale = new Sale();
            Client user = new Client();
            Exhibition exhibition = new Exhibition();
            Product product = new NewProduct();

            user.setIdUser(rs.getInt("id_user"));
            user.setName(rs.getString("user_name"));
            user.setLastName(rs.getString("last_name"));
            exhibition.setIdExhibition(rs.getInt("id_exhibition"));
            exhibition.setName(rs.getString("event_name"));
            sale.setQuantity(rs.getInt("quantity"));
            sale.setTotalPrice(rs.getDouble("total_price"));
            sale.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
            product.setIdProduct(rs.getInt("id_product"));
            product.setName(rs.getString("prod_name"));

            sale.setExhibition(exhibition);
            sale.setClient(user);
            sale.setProduct(product);
            sales.add(sale);
        }
    }


}
