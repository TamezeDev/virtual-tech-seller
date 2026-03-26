package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.CartRespository;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartRespository cartRespository;

    public CartService() {
        this.cartRespository = new CartRespository();
    }

    public void setCartItemList(User currentUser) throws DBConnectionException, SQLException {
        List<CartItem> cartItems = cartRespository.getCartItem(currentUser);
        if (cartItems != null) {
            ((Client) currentUser).setCartItems(cartItems);
            System.out.println(((Client) currentUser).getCartItems().getFirst().getProduct().getName());
        }
    }
}

