package org.zeki.virtualtechseller.model.user;

import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;

public interface ProductManager {
    void createProduct(Product product);

    void modifyProduct(Product currentProduct, Product newDataProduct);

    void assignProductToExhibition(Product product, Exhibition event, int quantity);

    void retireProductFromExhibition(Product product, Exhibition event);
}
