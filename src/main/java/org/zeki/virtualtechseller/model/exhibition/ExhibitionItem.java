package org.zeki.virtualtechseller.model.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionItem {

    private Product product;
    private int quantity;


    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public boolean quantityAvailable() {
        return this.quantity >= quantity;
    }
}
