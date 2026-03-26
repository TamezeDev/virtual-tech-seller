package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.repository.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }
}
