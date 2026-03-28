package org.zeki.virtualtechseller.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public final class Client extends User implements Purchasable {

    private double credit;
    private List<CartItem> cartItems;
    private List<Sale> sales;
    private List<UserVisit> visits;
    private Exhibition currentExhibition;

    public Client() {
        cartItems = new ArrayList<>();
        sales = new ArrayList<>();
        visits = new ArrayList<>();
    }

    @Override
    public void addToCart(Product product, int quantity) {

    }

    @Override
    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    @Override
    public void clearCart() {
        cartItems.clear();

    }

    @Override
    public void buyProduct(Product product, int quantity, Exhibition event, Sale sale) {

    }

    @Override
    public void buyCart(Exhibition exhibition, List<Sale> sales) {

    }

    @Override
    public List<Sale> getPurchaseList() {
        return List.of();
    }

    @Override
    public void buyProduct(Product product, int quantity) {

    }

    @Override
    public void rechargeCredit(double quantity) {
        setCredit(getCredit() + quantity);
    }

    @Override
    public Role getRoleName() {
        return Role.CLIENT;
    }
}
