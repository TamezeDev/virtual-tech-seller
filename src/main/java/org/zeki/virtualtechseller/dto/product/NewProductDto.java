package org.zeki.virtualtechseller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.product.Category;

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

}
