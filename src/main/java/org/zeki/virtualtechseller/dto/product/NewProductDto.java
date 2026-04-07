package org.zeki.virtualtechseller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.product.NewProduct;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class NewProductDto {

    private int idProduct;
    private String name;
    private String description;
    private String urlImage;
    private double basePrice;
    private Category category;
    private int stock;
    private LocalDate releaseDate;


    public NewProductDto createNewProductDTO(NewProduct product) {
        // CREATE NEW PRODUCT TRANSFER OBJECT
        NewProductDto productDto = new NewProductDto();

        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setBasePrice(product.getBasePrice());
        productDto.setCategory(product.getCategory());
        productDto.setStock(product.getStock());
        productDto.setReleaseDate(product.getReleaseDate());
        productDto.setUrlImage(product.getUrlImage());

        return productDto;
    }
}
