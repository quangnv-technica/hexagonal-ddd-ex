package com.martinachov.hexagonal;

import com.martinachov.hexagonal.application.ports.input.CreateProductUseCase;
import com.martinachov.hexagonal.application.ports.input.GetProductUseCase;
import com.martinachov.hexagonal.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemoryLeakTests {

    @Autowired
    private CreateProductUseCase createProductUseCase;

    @Autowired
    private GetProductUseCase getProductUseCase;

    @Test
    @Transactional
    public void testTransactionalBehavior() {
        // Test that @Transactional annotations are working properly
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .build();

        Product savedProduct = createProductUseCase.createProduct(product);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());

        Product retrievedProduct = getProductUseCase.getProductById(savedProduct.getId());
        assertNotNull(retrievedProduct);
        assertEquals(savedProduct.getName(), retrievedProduct.getName());
        assertEquals(savedProduct.getDescription(), retrievedProduct.getDescription());
    }

    @Test
    public void testMemoryUsageWithMultipleOperations() {
        // Record initial memory usage
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Force garbage collection
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Perform multiple operations
        for (int i = 0; i < 100; i++) {
            Product product = Product.builder()
                    .name("Test Product " + i)
                    .description("Test Description " + i)
                    .build();
            
            Product savedProduct = createProductUseCase.createProduct(product);
            getProductUseCase.getProductById(savedProduct.getId());
        }
        
        // Force garbage collection and check memory
        runtime.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable (less than 50MB for 100 operations)
        assertTrue(memoryIncrease < 50 * 1024 * 1024, 
                   "Memory increase is too high: " + memoryIncrease + " bytes");
    }
}