package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.CartRespository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartRespository cartRespository;

    public CartService() {
        this.cartRespository = new CartRespository();
    }

    public void setCartItemList(User currentUser) {
        try {
            List<CartItem> cartItems = cartRespository.getCartItem(currentUser);
            if (cartItems != null) {
                ((Client) currentUser).setCartItems(cartItems);
            }

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
        } catch (SQLException e) {
            String message = "Error obteniendo datos de la cesta del usuario";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
        }
    }
}

