package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Product product;
    private int quantity;

    public double calculateSubtotal() {
        return product.calculateUnitPrice() * quantity;
    }

}
