package org.zeki.virtualtechseller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.product.UsedProduct;

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

    public UsedProductDto createUsedProductDTO(UsedProduct product) {
        // CREATE NEW PRODUCT TRANSFER OBJECT
        UsedProductDto productDto = new UsedProductDto();

        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setBasePrice(product.getBasePrice());
        productDto.setCategory(product.getCategory());
        productDto.setDiscountPercentage(product.getDiscountPercentage());
        productDto.setRemark(product.getRemark());
        productDto.setUrlImage(product.getUrlImage());

        return productDto;
    }

}
