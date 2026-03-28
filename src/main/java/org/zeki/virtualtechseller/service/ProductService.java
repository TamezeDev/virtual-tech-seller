package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.repository.ProductRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public ResultService<CartItem> stockCartItems() {
        List<CartItem> cartItems = ((Client) SessionManager.getInstance().getCurrentUser()).getCartItems();
        try {
            for (CartItem cartItem : cartItems) {
                if (!productRepository.availableExhibitionStock(cartItem.getProduct(), cartItem.getExhibition(), cartItem.getQuantity())) {
                    return new ResultService<>(false, "Producto no disponible : ", cartItem);
                }

            }
            return new ResultService<>(true, "Hay stock y disponibilidad", null);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;

        } catch (SQLException e) {
            String message = "Error obteniendo disponibilidad de los productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }
}
