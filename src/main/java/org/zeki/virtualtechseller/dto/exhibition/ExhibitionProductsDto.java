package org.zeki.virtualtechseller.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.product.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionProductsDto {

    private Product product;
    private ExhibitionItem exhibitionItem;
    private Exhibition exhibition;
}
