package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionModifyDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.exception.DuplicateExhibitionNameException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.*;

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

    public List<ExhibitionItem> getExhibitionItemsFromDB(int idExhibition) throws DBConnectionException, SQLException {
        String query = "SELECT pe.quantity, p.id_product, p.name AS prod_name, p.description AS prod_description, p.url_image, p.base_price, " +
                "p.available, c.id_category, c.name AS cat_name, c.description AS cat_description, np.id_product AS new_id, np.stock, " +
                "np.release_date, up.id_product AS used_id, up.discount, up.remark " +
                "FROM products_exhibitions pe INNER JOIN products p ON p.id_product = pe.id_product " +
                "LEFT JOIN new_products np ON np.id_product = p.id_product " +
                "LEFT JOIN used_products up ON up.id_product = p.id_product " +
                "INNER JOIN categories c ON c.id_category = p.id_category " +
                "WHERE pe.id_exhibition = ?";

        List<ExhibitionItem> exhibitionItems = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idExhibition);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // CREATE PRODUCT (CHECK TYPE)
                Product product = null;
                if (rs.getObject("new_id") != null) {
                    product = new NewProduct();
                    ((NewProduct) product).setStock(rs.getInt("stock"));
                    ((NewProduct) product).setReleaseDate(rs.getDate("release_Date").toLocalDate());
                } else if (rs.getObject("used_id") != null) {
                    product = new UsedProduct();
                    ((UsedProduct) product).setDiscountPercentage(rs.getDouble("discount"));
                    ((UsedProduct) product).setRemark(rs.getString("remark"));
                }

                product.setIdProduct(rs.getInt("id_product"));
                product.setName(rs.getString("prod_name"));
                product.setDescription(rs.getString("prod_description"));
                product.setUrlImage(rs.getString("url_image"));
                product.setBasePrice(rs.getDouble("base_price"));
                // CREATE CATEGORY
                Category category = new Category();
                category.setIdCategory(rs.getInt("id_category"));
                category.setName(rs.getString("cat_name"));
                category.setDescription(rs.getString("cat_description"));
                product.setCategory(category);
                //CREATE EXHIBITION ITEM
                ExhibitionItem exhibitionItem = new ExhibitionItem();
                exhibitionItem.setQuantity(rs.getInt("quantity"));
                exhibitionItem.setProduct(product);
                exhibitionItems.add(exhibitionItem);
            }
        }
        return exhibitionItems;
    }

    public boolean addNewExhibition(Exhibition exhibition) throws DBConnectionException, DuplicateExhibitionNameException, SQLException {
        String query = "INSERT INTO exhibitions(name, description, init_date, end_date) VALUES (?, ?, ?, ?);";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, exhibition.getName());
            ps.setString(2, exhibition.getDescription());
            ps.setString(3, String.valueOf(exhibition.getInitDate()));
            ps.setString(4, String.valueOf(exhibition.getEndDate()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new DuplicateExhibitionNameException("El nombre de la exhibición ya está en uso");
            }
            throw new SQLException();
        }
    }

    public boolean changeActivateExhibition(boolean access, int idExhibition) throws DBConnectionException, SQLException {
        String query = "UPDATE exhibitions SET active = ? WHERE id_exhibition = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setBoolean(1, access);
            ps.setInt(2, idExhibition);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean modifyEventData(ExhibitionModifyDto exhibitionModifyDto) throws DBConnectionException, SQLException, DuplicateExhibitionNameException {

        String query = "UPDATE exhibitions SET name = ?, description = ?, init_date = ?, end_date = ? WHERE id_exhibition = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, exhibitionModifyDto.getName());
            ps.setString(2, exhibitionModifyDto.getDescription());
            ps.setString(3, String.valueOf(exhibitionModifyDto.getInitDate()));
            ps.setString(4, String.valueOf(exhibitionModifyDto.getEndDate()));
            ps.setInt(5, exhibitionModifyDto.getIdExhibition());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new DuplicateExhibitionNameException("El nombre de la exhibición ya está en uso");
            }
            throw new SQLException();
        }
    }

}