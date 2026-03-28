package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Product product;
    private Exhibition exhibition;
    private int quantity;

    public double calculateSubtotal() {
        return product.calculateUnitPrice() * quantity;
    }

}
