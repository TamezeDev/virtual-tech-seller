package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.ProductRepository;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
