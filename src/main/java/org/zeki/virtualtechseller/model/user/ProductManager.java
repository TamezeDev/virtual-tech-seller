package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.service.ProductService;

public interface ProductManager {
    String createProduct(ProductService service, NewProductDto newProductDto, UsedProductDto usedProductDto);

    void modifyProduct(Product currentProduct, Product newDataProduct);

    void assignProductToExhibition(Product product, Exhibition event, int quantity);

    void retireProductFromExhibition(Product product, Exhibition event);
}
