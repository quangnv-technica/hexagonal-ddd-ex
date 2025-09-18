# Hexagonal Architecture Documentation

**Project:** Hexagonal DDD Example  
**Technology:** Spring Boot with Java 17  
**Last Updated:** 2025-09-08  
**Architecture Pattern:** Hexagonal Architecture (Ports and Adapters) with Domain-Driven Design

---

## 1. Introduction

This document explains the **Hexagonal Architecture** implementation in this Spring Boot application. Hexagonal Architecture, also known as **Ports and Adapters**, is an architectural pattern that separates the core business logic from external concerns through well-defined interfaces.

### Why Hexagonal Architecture?

- **Business Logic Protection**: Core domain logic is isolated from external frameworks and technologies
- **Technology Independence**: Easy to change databases, web frameworks, or external services
- **Enhanced Testability**: Business logic can be tested without external dependencies
- **Clear Boundaries**: Explicit separation between what the application does vs. how it does it

## 2. Core Principles

### 2.1 Dependency Inversion
Dependencies point **inward** toward the domain core:
```
Infrastructure Layer → Application Layer → Domain Layer
```

### 2.2 Ports and Adapters
- **Ports**: Interfaces that define what the application needs or provides
- **Adapters**: Concrete implementations of ports that handle technical details

### 2.3 Inside-Out Design
The architecture is designed from the domain outward, not from the technical infrastructure inward.

## 3. Architecture Layers

### 3.1 Domain Layer (Center of the Hexagon)

The **heart** of the application containing pure business logic.

#### Components:
- **Domain Models**: `Product.java`
  ```java
  @Builder
  public class Product {
      private Long id;
      private String name;
      private String description;
  }
  ```

- **Domain Services**: `ProductService.java`
  - Implements business use cases
  - Orchestrates domain objects
  - Contains business rules

- **Domain Exceptions**: `ProductNotFoundException.java`
  - Business-specific exceptions

#### Characteristics:
- ✅ No external dependencies
- ✅ Framework-agnostic
- ✅ Pure business logic
- ❌ No infrastructure concerns

### 3.2 Application Layer (Ports)

Defines the **interface** between the domain and the outside world.

#### Input Ports (Primary Ports)
What the application **can do**:
- `CreateProductUseCase.java`
- `GetProductUseCase.java`

```java
public interface CreateProductUseCase {
    Product createProduct(Product product);
}
```

#### Output Ports (Secondary Ports)  
What the application **needs**:
- `ProductOutputPort.java`

```java
public interface ProductOutputPort {
    Product saveProduct(Product product);
    Optional<Product> getProductById(Long id);
}
```

### 3.3 Infrastructure Layer (Adapters)

Handles **technical implementation details**.

#### Primary Adapters (Input)
Handle incoming requests:
- **REST Adapter**: `ProductRestAdapter.java`
  - Exposes HTTP endpoints
  - Converts REST requests to domain calls
  - Handles HTTP-specific concerns

```java
@RestController
@RequestMapping("/v1")
public class ProductRestAdapter {
    
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        Product product = mapper.map(request, Product.class);
        product = createProductUseCase.createProduct(product);
        return ResponseEntity.ok(mapper.map(product, ProductResponse.class));
    }
}
```

#### Secondary Adapters (Output)
Handle outgoing operations:
- **Persistence Adapter**: `ProductPersistenceAdapter.java`
  - Implements `ProductOutputPort`
  - Handles database operations
  - Converts between domain and persistence models

```java
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductOutputPort {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Override
    public Product saveProduct(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        productRepository.save(entity);
        return productMapper.toProduct(entity);
    }
}
```

## 4. Data Flow Architecture

### 4.1 Request Flow (Create Product)

```
1. HTTP Request
   ↓
2. ProductRestAdapter (Primary Adapter)
   ↓ (converts ProductRequest → Product)
3. CreateProductUseCase (Input Port)
   ↓
4. ProductService (Domain Service)
   ↓
5. ProductOutputPort (Output Port Interface)
   ↓
6. ProductPersistenceAdapter (Secondary Adapter)
   ↓ (converts Product → ProductEntity)
7. ProductRepository (JPA Repository)
   ↓
8. Database (H2/PostgreSQL)
```

### 4.2 Response Flow
The response follows the same path in reverse, with appropriate data transformations at each layer.

## 5. Key Implementation Details

### 5.1 Dependency Injection Configuration

The `BeanConfiguration.java` wires dependencies correctly:

```java
@Configuration
public class BeanConfiguration {
    
    @Bean
    public ProductService productService(ProductPersistenceAdapter adapter) {
        return new ProductService(adapter);  // Domain service gets output port implementation
    }
    
    @Bean
    public ProductPersistenceAdapter productPersistenceAdapter(
            ProductRepository repository, 
            ProductMapper mapper) {
        return new ProductPersistenceAdapter(repository, mapper);
    }
}
```

### 5.2 Data Transformation

Data is transformed at each layer boundary:

- **REST Layer**: `ProductRequest` ↔ `Product` ↔ `ProductResponse`
- **Persistence Layer**: `Product` ↔ `ProductEntity`

### 5.3 Error Handling

Domain exceptions (`ProductNotFoundException`) are handled at the adapter level, converted to appropriate HTTP responses.

## 6. Benefits Demonstrated

### 6.1 Technology Independence
- Can switch from H2 to PostgreSQL by changing configuration
- Can add GraphQL adapter alongside REST adapter
- Can replace JPA with MongoDB by implementing new adapter

### 6.2 Testability
- Domain logic testable without database
- Adapters testable in isolation
- Use case testing with mock adapters

### 6.3 Clear Separation of Concerns
- **Domain**: What the business does
- **Application**: How use cases are orchestrated  
- **Infrastructure**: Technical implementation details

## 7. Common Patterns Used

### 7.1 Repository Pattern
`ProductRepository` abstracts data access details.

### 7.2 Mapper Pattern
`ProductMapper` handles object transformations between layers.

### 7.3 Use Case Pattern
Each business capability is represented as a specific use case interface.

### 7.4 Adapter Pattern
Concrete implementations adapt external technologies to internal interfaces.

## 8. Best Practices Implemented

- ✅ **Single Responsibility**: Each class has one reason to change
- ✅ **Dependency Inversion**: High-level modules don't depend on low-level modules
- ✅ **Interface Segregation**: Small, focused interfaces
- ✅ **Open/Closed**: Open for extension, closed for modification

## 9. Extending the Architecture

### Adding New Features
1. Create domain model if needed
2. Define input port (use case interface)
3. Define output port if new external dependency needed
4. Implement domain service
5. Create adapters as needed

### Adding New Technologies
1. Implement existing output ports with new technology
2. Create new input adapters for new interaction methods
3. No changes needed in domain or application layers

---

*This architecture promotes clean, maintainable, and testable code by keeping business logic pure and dependencies properly managed.*