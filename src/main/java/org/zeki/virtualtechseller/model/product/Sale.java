package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.user.Client;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    private int idSale;
    private Client client;
    private Product product;
    private Exhibition exhibition;
    private int quantity;
    private double totalPrice;
    private LocalDate purchaseDate;

    public void calculateTotalPrice(){
        totalPrice = product.calculateUnitPrice() * quantity;
    }

}
