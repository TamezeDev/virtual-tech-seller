package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItem {

    private int idCartItem;
    private Product product;
    private int quantity;

    public double calculateSubtotal() {
        return product.calculateUnitPrice() * quantity;
    }

}
