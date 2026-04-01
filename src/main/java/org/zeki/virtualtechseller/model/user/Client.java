package org.zeki.virtualtechseller.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.UserVisit;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void addToCart(Product product, Exhibition exhibition, int quantity) {
        CartItem cartItem = new CartItem(product, exhibition, quantity);
        Optional<CartItem> item = cartItems.stream().filter(cartProduct -> cartProduct.getProduct().getIdProduct() == product.getIdProduct() && cartProduct.getExhibition().getIdExhibition() == cartItem.getExhibition().getIdExhibition()).findFirst();
        if (item.isPresent()) {

            item.get().setQuantity(item.get().getQuantity() + quantity);
        } else {
            cartItems.add(cartItem);
        }
    }

    public void modifyUserVisit(Exhibition exhibition, Client client){
       Optional<UserVisit> visit = visits.stream().filter(userVisit -> userVisit.getExhibition().getIdExhibition() == exhibition.getIdExhibition()).findFirst();
       if (visit.isPresent()){
           visit.get().increaseVisit();
           visit.get().updateLastVisit();
       }else {
           UserVisit userVisit = new UserVisit();
           userVisit.setExhibition(exhibition);
           userVisit.updateLastVisit();
           userVisit.setClient(client);
           userVisit.setVisitCounter(1);
           visits.add(userVisit);
       }
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
    public boolean emptyCartList() {
        return cartItems.isEmpty();
    }

    @Override
    public int checkQuantityCartItems() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public List<Sale> buyCart(List<CartItem> cartItems) {
        List<Sale> salesCart = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Sale sale = new Sale();
            sale.setProduct(cartItem.getProduct());
            sale.setClient(this);
            sale.setExhibition(cartItem.getExhibition());
            sale.setQuantity(cartItem.getQuantity());
            sale.setTotalPrice(cartItem.calculateTotal());
            sale.setPurchaseDate(LocalDate.now());
            salesCart.add(sale);
        }
        return salesCart;
    }

    @Override
    public void rechargeCredit(double quantity) {
        setCredit(getCredit() + quantity);
    }

    @Override
    public void decreaseCredit(double quantity) {
        setCredit(getCredit() - quantity);
    }

    @Override
    public UserRole getRoleName() {
        return UserRole.CLIENT;
    }
}
