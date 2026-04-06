package org.zeki.virtualtechseller.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public final class UsedProduct extends Product {

    private double discountPercentage;
    private String remark;

    @Override
    public double calculateUnitPrice() {
        return getBasePrice() * (1 - (getDiscountPercentage() / 100));
    }
}
