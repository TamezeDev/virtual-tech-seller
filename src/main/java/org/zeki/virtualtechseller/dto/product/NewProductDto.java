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
    protected String name;
    protected String description;
    protected String urlImage;
    protected double basePrice;
    protected Category category;
    private int stock;
    private LocalDate releaseDate;

}
