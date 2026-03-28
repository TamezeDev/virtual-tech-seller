package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.util.List;

public interface Purchasable {
    void buyProduct(Product product, int quantity);

    void rechargeCredit(double quantity);

    void decreaseCredit(double quantity);

    void addToCart(Product product, int quantity);

    void removeFromCart(CartItem cartItem);

    void clearCart();

    void buyProduct(Product product, int quantity, Exhibition event, Sale sale);

    List<Sale> buyCart(List<CartItem> cartItems);

    List<Sale> getPurchaseList();

}
