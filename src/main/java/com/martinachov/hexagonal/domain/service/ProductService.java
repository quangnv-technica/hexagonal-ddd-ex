package com.martinachov.hexagonal.domain.service;

import com.martinachov.hexagonal.application.ports.input.CreateProductUseCase;
import com.martinachov.hexagonal.application.ports.input.GetProductUseCase;
import com.martinachov.hexagonal.application.ports.output.ProductOutputPort;
import com.martinachov.hexagonal.domain.exception.ProductNotFoundException;
import com.martinachov.hexagonal.domain.model.Product;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class ProductService implements CreateProductUseCase, GetProductUseCase{

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductOutputPort productOutputPort;
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        logger.info("Retrieving product by ID: {}", id);
        return productOutputPort.getProductById(id)
                                .orElseThrow(() -> new ProductNotFoundException("No se encontro el producto con ID: " + id));
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        logger.info("Creating product with name: {}", product.getName());
        return productOutputPort.saveProduct(product);
    }
    
}
