package org.zeki.virtualtechseller.model.user;

import javafx.collections.ObservableList;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionProductsDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.service.ProductService;

public interface ProductManager {

    String createProduct(ProductService service, NewProductDto newProductDto, UsedProductDto usedProductDto);

    void assignProductToExhibition(ObservableList<ExhibitionProductsDto> productsDto,ExhibitionProductsDto selectedProductDto, Exhibition selectedExhibicion, int quantity);

    void retireProductFromExhibition(ExhibitionProductsDto selectedProductDto);

    void decreaseProductFromExhibition(ExhibitionProductsDto selectedProductDto, int quantity);
}
