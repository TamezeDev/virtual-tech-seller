package org.zeki.virtualtechseller.repository;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.database.ConnectionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.*;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRespository {

    private final ConnectionManager connectionManager;

    public CartRespository() {
        connectionManager = AppContext.getInstance().getConnectionManager();
    }

    public List<CartItem> getCartItem(User user) throws DBConnectionException, SQLException {
        String query = "SELECT p.id_product, cart.quantity, p.name, p.description, p.base_price, " +
                "p.url_image, c.id_category, c.name, c.description, np.id_product AS new_id, np.stock, " +
                "np.release_date, up.id_product AS used_id, up.discount, up.remark, cart.quantity " +
                "FROM cart_items cart INNER JOIN products p ON p.id_product = cart.id_product " +
                "LEFT JOIN new_products np ON np.id_product = p.id_product " +
                "LEFT JOIN used_products up ON up.id_product = p.id_product " +
                "INNER JOIN categories c ON c.id_category = p.id_category " +
                "WHERE cart.id_user = ?;";
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, user.getIdUser());
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
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUrlImage(rs.getString("url_image"));
                product.setBasePrice(rs.getDouble("base_price"));
                // CREATE CATEGORY
                Category category = new Category();
                category.setIdCategory(rs.getInt("id_category"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                product.setCategory(category);
                // CREATE CART ITEM
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setProduct(product);
                cartItems.add(cartItem);
            }
        }
        return cartItems;
    }

    public boolean removeUserCartItem(CartItem cartItem, Client client) throws DBConnectionException, SQLException {
        String sql = "DELETE FROM cart_items WHERE id_user = ? AND id_product = ?;";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, client.getIdUser());
            ps.setInt(2, cartItem.getProduct().getIdProduct());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeAllUserCartItems(Client client) throws DBConnectionException, SQLException {
        String sql = "DELETE FROM cart_items WHERE id_user = ?";

        try (Connection connection = connectionManager.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, client.getIdUser());
            return ps.executeUpdate() > 0;
        }

    }
}
