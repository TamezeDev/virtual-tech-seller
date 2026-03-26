package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsedProduct extends Product {

    private double discountPercentage;
    private String remark;

    @Override
    public double calculateUnitPrice() {
        return getBasePrice() * (1 - (getDiscountPercentage() / 100));
    }
}
