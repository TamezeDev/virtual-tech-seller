package org.zeki.virtualtechseller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Category;

@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class UsedProductDto {

    private int idProduct;
    protected String name;
    protected String description;
    protected String urlImage;
    protected double basePrice;
    protected Category category;
    private double discountPercentage;
    private String remark;

}
