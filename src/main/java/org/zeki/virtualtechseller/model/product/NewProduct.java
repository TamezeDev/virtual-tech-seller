package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewProduct extends Product {

    private int stock;
    private LocalDate releaseDate;

    public boolean hasStock(int quantity) {
        return stock >= quantity;
    }

    @Override
    public double calculateUnitPrice() {
        return getBasePrice() * (1 + (getTaxPercentage() / 100));
    }
}
