package com.martinachov.hexagonal.infrastructure.adapters.output.persistence;

import java.util.Optional;

import com.martinachov.hexagonal.application.ports.output.ProductOutputPort;
import com.martinachov.hexagonal.domain.model.Product;
import com.martinachov.hexagonal.infrastructure.adapters.output.persistence.entity.ProductEntity;
import com.martinachov.hexagonal.infrastructure.adapters.output.persistence.mapper.ProductMapper;
import com.martinachov.hexagonal.infrastructure.adapters.output.persistence.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductOutputPort {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Override
    @Transactional
    public Product saveProduct(Product product) {
        ProductEntity productEntity = productMapper.toEntity(product);
        // Save and return the saved entity to ensure proper ID assignment
        ProductEntity savedEntity = productRepository.save(productEntity);
        return productMapper.toProduct(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProduct);
    }
    
}
