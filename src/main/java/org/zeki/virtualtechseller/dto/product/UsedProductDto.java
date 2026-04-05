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
    private String name;
    private String description;
    private String urlImage;
    private double basePrice;
    private Category category;
    private double discountPercentage;
    private String remark;

}
