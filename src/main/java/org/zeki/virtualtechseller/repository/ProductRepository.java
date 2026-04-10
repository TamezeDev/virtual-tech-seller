package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionProductsDto;
import org.zeki.virtualtechseller.dto.product.CategoryDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.exception.DuplicateNameException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.UsedProduct;

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

    public boolean addNewCategory(CategoryDto categoryDto) throws DBConnectionException, SQLException, DuplicateNameException {
        String query = "INSERT INTO categories(name, description) VALUES (?, ?);";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, categoryDto.getName());
            ps.setString(2, categoryDto.getDescription());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
                throw new DuplicateNameException("El nombre de la categoría ya está registrada");
            }
            throw new SQLException();
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
        String query = "UPDATE products SET available = 0 WHERE id_product = ? AND available = 1;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduct);
            return ps.executeUpdate();
        }
    }

    public boolean updateBasePriceProduct(Product product) throws SQLException, DBConnectionException {
        String query = "UPDATE products SET base_price = ? WHERE id_product = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDouble(1, product.getBasePrice());
            ps.setInt(2, product.getIdProduct());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean setAvailableProduct(int idProduct) throws SQLException, DBConnectionException {
        String query = "UPDATE products SET available = 1 WHERE id_product = ?;";
        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idProduct);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStockProduct(Product product) throws SQLException, DBConnectionException {
        String query = "UPDATE new_products SET stock = ? WHERE id_product = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, ((NewProduct) product).getStock());
            ps.setInt(2, product.getIdProduct());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeExhibitionItem(ExhibitionProductsDto productsDto) throws DBConnectionException, SQLException {
        String query = "DELETE FROM products_exhibitions WHERE id_product = ? AND id_exhibition = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, productsDto.getProduct().getIdProduct());
            ps.setInt(2, productsDto.getExhibition().getIdExhibition());

            return ps.executeUpdate() > 0;
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

    public int addProduct(Connection connection, NewProductDto productDto, UsedProductDto usedProductDto) throws SQLException, DuplicateNameException {
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
                throw new DuplicateNameException("El nombre o la imagen del producto deben ser únicos y ya están en uso");
            }
            throw new SQLException();
        }

    }

    public boolean getCategoryID(List<Product> products) throws DBConnectionException, SQLException {
        String query = "SELECT id_category FROM categories WHERE name = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            for (Product product : products) {
                ps.setString(1, product.getCategory().getName());

                ResultSet rs = ps.executeQuery();
                if (rs.next()) product.getCategory().setIdCategory(rs.getInt("id_category"));
            }
            return true;
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

    public List<ExhibitionProductsDto> getFullProductAssociateOrNot() throws DBConnectionException, SQLException {
        String query = "SELECT p.id_product, p.name AS product_name, p.description, p.available, np.stock, c.name AS category_name, e.id_exhibition, e.name AS exhibition_name, pe.quantity" +
                " FROM products p INNER JOIN categories c ON c.id_category = p.id_category " +
                "LEFT JOIN new_products np ON np.id_product = p.id_product " +
                "LEFT JOIN products_exhibitions pe ON pe.id_product = p.id_product " +
                "LEFT JOIN exhibitions e ON e.id_exhibition = pe.id_exhibition;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            List<ExhibitionProductsDto> exhibitionProductsDto = new ArrayList<>();

            while (rs.next()) {
                Product product;
                Category category = new Category();
                ExhibitionProductsDto exhibitionProducts = new ExhibitionProductsDto();
                // CHECK NEW OR USED TO GET STOCK
                if (rs.getObject("stock") != null) {
                    product = new NewProduct();
                    ((NewProduct) product).setStock(rs.getInt("stock"));
                } else product = new UsedProduct();
                // SET BASIC VALUES
                product.setIdProduct(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setAvailable(rs.getBoolean("available"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);
                exhibitionProducts.setProduct(product);
                // CHECK IF PRESENT IN EXHIBITION
                if (rs.getObject("id_exhibition") != null) {
                    Exhibition exhibition = new Exhibition();
                    ExhibitionItem exhibitionItem = new ExhibitionItem();
                    exhibitionItem.setQuantity(rs.getInt("quantity"));
                    exhibition.setIdExhibition(rs.getInt("id_exhibition"));
                    exhibition.setName(rs.getString("exhibition_name"));

                    exhibition.getItems().add(exhibitionItem);
                    exhibitionProducts.setExhibition(exhibition);
                    exhibitionProducts.setExhibitionItem(exhibitionItem);
                }
                // ADD PRODUCT DTO
                exhibitionProductsDto.add(exhibitionProducts);
            }
            return exhibitionProductsDto;
        }
    }

    public List<Product> getDataProducts() throws DBConnectionException, SQLException {
        String query = "SELECT p.id_product, p.name AS prod_name, p.description AS prod_description, p.base_price, p.available, np.stock, np.release_date, " +
                "p.url_image, c.id_category, c.name AS cat_name, c.description AS cat_description, up.discount, up.remark " +
                "FROM products p INNER JOIN categories c ON p.id_category = c.id_category " +
                "LEFT JOIN new_products np ON np.id_product = p.id_product " +
                "LEFT JOIN used_products up ON up.id_product = p.id_product;";
        List<Product> products = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Product product;
                if (rs.getObject("stock") != null) {
                    product = new NewProduct();
                    ((NewProduct) product).setStock(rs.getInt("stock"));
                    ((NewProduct) product).setReleaseDate(rs.getDate("release_date").toLocalDate());
                } else {
                    product = new UsedProduct();
                    ((UsedProduct) product).setDiscountPercentage(rs.getDouble("discount"));
                    ((UsedProduct) product).setRemark(rs.getString("remark"));
                }

                Category category = new Category();
                category.setIdCategory(rs.getInt("id_category"));
                category.setName(rs.getString("cat_name"));
                category.setDescription(rs.getString("cat_description"));
                product.setIdProduct(rs.getInt("id_product"));
                product.setName(rs.getString("prod_name"));
                product.setDescription(rs.getString("prod_description"));
                product.setBasePrice(rs.getDouble("base_price"));
                product.setAvailable(rs.getBoolean("available"));
                product.setUrlImage(rs.getString("url_image"));

                product.setCategory(category);
                products.add(product);
            }
        }
        return products;
    }

}

