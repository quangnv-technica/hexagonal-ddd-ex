# Testing the Hexagonal Architecture

This guide demonstrates how to run and test the hexagonal architecture implementation to see the architecture in action.

## 🚀 Quick Start

### 1. Build and Run the Application

```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 2. Test the REST API

#### Create a Product

```bash
curl -X POST http://localhost:8080/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop for software development"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "name": "Laptop", 
  "description": "High-performance laptop for software development"
}
```

#### Get a Product by ID

```bash
curl http://localhost:8080/v1/products/1
```

**Expected Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop for software development"
}
```

#### Test Error Handling

```bash
curl http://localhost:8080/v1/products/999
```

**Expected Response:**
```json
{
  "error": "Product not found",
  "message": "No se encontro el producto con ID: 999"
}
```

## 🧪 Understanding the Architecture Through Testing

### Data Flow Demonstration

When you call `POST /v1/products`, here's what happens:

1. **HTTP Request** hits `ProductRestAdapter`
2. **Request Validation** using Bean Validation annotations  
3. **Data Transformation** from `ProductRequest` to `Product` domain model
4. **Business Logic** execution in `ProductService`
5. **Persistence** through `ProductOutputPort` interface
6. **Database Storage** via `ProductPersistenceAdapter`
7. **Response Transformation** from `Product` to `ProductResponse`

### Testing Different Layers

#### 1. Testing Domain Logic (Unit Tests)

```java
@Test
void shouldCreateProductWithValidData() {
    // Given
    ProductOutputPort mockOutputPort = mock(ProductOutputPort.class);
    ProductService service = new ProductService(mockOutputPort);
    Product inputProduct = Product.builder()
        .name("Test Product")
        .description("Test Description")
        .build();
    
    Product savedProduct = Product.builder()
        .id(1L)
        .name("Test Product")
        .description("Test Description")
        .build();
    
    when(mockOutputPort.saveProduct(inputProduct)).thenReturn(savedProduct);
    
    // When
    Product result = service.createProduct(inputProduct);
    
    // Then
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Test Product");
}
```

#### 2. Testing Adapters (Integration Tests)

```java
@SpringBootTest
@AutoConfigureTestDatabase
class ProductPersistenceAdapterTest {
    
    @Autowired
    private ProductPersistenceAdapter adapter;
    
    @Test
    void shouldSaveAndRetrieveProduct() {
        // Given
        Product product = Product.builder()
            .name("Integration Test Product")
            .description("Testing persistence")
            .build();
        
        // When
        Product saved = adapter.saveProduct(product);
        Optional<Product> retrieved = adapter.getProductById(saved.getId());
        
        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Integration Test Product");
    }
}
```

## 🔄 Architecture Benefits in Action

### 1. Technology Independence

You can easily switch from H2 to PostgreSQL:

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/products
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
```

**No changes needed** in domain or application layers!

### 2. Multiple Adapters

You could add a GraphQL adapter alongside REST:

```java
@Component
public class ProductGraphQLAdapter {
    
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    
    // GraphQL resolvers using the same use cases
    @SchemaMapping
    public Product createProduct(ProductInput input) {
        Product product = mapper.map(input, Product.class);
        return createProductUseCase.createProduct(product);
    }
}
```

### 3. Easy Testing

Each layer can be tested independently:

- **Domain**: Test business rules without external dependencies
- **Application**: Test use case orchestration with mocked ports
- **Infrastructure**: Test adapters with real external dependencies

## 🔍 Observing the Architecture

### Console Output Analysis

When you run the application and make requests, you'll see:

```
Creando Producto          # From ProductService.createProduct()
Retornando Producto por ID # From ProductService.getProductById()
```

This demonstrates that:
1. The **domain service** contains the business logic
2. The **adapters** handle technical details
3. The **ports** define clean interfaces

### Database Verification

You can check the H2 console at `http://localhost:8080/h2-console`:

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave empty)

Query: `SELECT * FROM PRODUCT_ENTITY`

## 🏗️ Extending the Architecture

### Adding New Use Cases

1. **Create the port interface**:
```java
public interface UpdateProductUseCase {
    Product updateProduct(Long id, Product product);
}
```

2. **Implement in domain service**:
```java
public class ProductService implements CreateProductUseCase, GetProductUseCase, UpdateProductUseCase {
    
    @Override
    public Product updateProduct(Long id, Product product) {
        // Business logic for updating
        Optional<Product> existing = productOutputPort.getProductById(id);
        if (existing.isEmpty()) {
            throw new ProductNotFoundException("Product not found: " + id);
        }
        
        Product updated = existing.get().toBuilder()
            .name(product.getName())
            .description(product.getDescription())
            .build();
            
        return productOutputPort.saveProduct(updated);
    }
}
```

3. **Add REST endpoint**:
```java
@PutMapping("/products/{id}")
public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
    Product product = mapper.map(request, Product.class);
    Product updated = updateProductUseCase.updateProduct(id, product);
    return ResponseEntity.ok(mapper.map(updated, ProductResponse.class));
}
```

### Adding New Adapters

To add email notifications when products are created:

1. **Define output port**:
```java
public interface NotificationOutputPort {
    void sendNotification(String message);
}
```

2. **Implement adapter**:
```java
@Component
public class EmailNotificationAdapter implements NotificationOutputPort {
    
    @Override
    public void sendNotification(String message) {
        // Email sending logic
        System.out.println("Email sent: " + message);
    }
}
```

3. **Use in domain service**:
```java
public class ProductService {
    
    private final ProductOutputPort productOutputPort;
    private final NotificationOutputPort notificationOutputPort;
    
    @Override
    public Product createProduct(Product product) {
        Product saved = productOutputPort.saveProduct(product);
        notificationOutputPort.sendNotification("Product created: " + saved.getName());
        return saved;
    }
}
```

## 📊 Architecture Validation

### Dependency Check

Run this command to verify dependencies point inward:

```bash
mvn dependency:analyze
```

### Package Structure Validation

The package structure enforces architectural boundaries:

```
domain/           # No external dependencies
application/      # Only depends on domain
infrastructure/   # Depends on application and domain
```

---

*This testing guide demonstrates how the hexagonal architecture provides flexibility, testability, and maintainability through practical examples.*