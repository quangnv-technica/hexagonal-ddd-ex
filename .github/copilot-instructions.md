# Copilot Instructions - Hexagonal DDD Example

## Repository Overview

This is a **Hexagonal Architecture (Ports & Adapters) with Domain-Driven Design (DDD)** example application built with Spring Boot 3.4.4. The project demonstrates clean architecture principles by separating business logic from infrastructure concerns using hexagonal patterns.

**Repository Type**: Spring Boot REST API demonstrating architectural patterns  
**Languages**: Java 21  
**Framework**: Spring Boot 3.4.4 with Spring Data JPA  
**Database**: H2 (in-memory for testing), PostgreSQL support available  
**Build Tool**: Maven 3.x  
**Size**: Small educational project (~15 Java files)

## Critical Build Requirements

### Java Version Requirement (CRITICAL)
- **REQUIRED: Java 21** - The project will NOT compile with earlier versions
- **VERIFY**: Always check `java -version` shows Java 21 before building
- **Environment Setup**:
  ```bash
  export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
  export PATH=/usr/lib/jvm/temurin-21-jdk-amd64/bin:$PATH
  ```

### Maven Wrapper Issue (CRITICAL)
- **DO NOT USE**: `./mvnw` - The wrapper is broken (missing .mvn/wrapper/ files)
- **ALWAYS USE**: System Maven with `mvn` command instead
- **Root Cause**: Maven wrapper configuration files are missing from repository

## Build and Test Instructions

### Clean Build Process
Always run these commands in sequence for reliable builds:

```bash
# 1. Set Java 21 environment (REQUIRED)
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=/usr/lib/jvm/temurin-21-jdk-amd64/bin:$PATH

# 2. Clean and compile (takes ~20-30 seconds)
mvn clean compile

# 3. Run tests (takes ~15 seconds)
mvn test

# 4. Package application (optional)
mvn package
```

### Running the Application
```bash
# Set Java environment first
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=/usr/lib/jvm/temurin-21-jdk-amd64/bin:$PATH

# Start application (runs on port 8080)
mvn spring-boot:run

# Application will be available at http://localhost:8080
# H2 console available at http://localhost:8080/h2-console
```

### Testing API Endpoints
```bash
# Create a product
curl -X POST http://localhost:8080/v1/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","description":"A test product"}'

# Get a product by ID
curl http://localhost:8080/v1/products/1
```

## Code Quality and Linting

### Checkstyle (Configured)
The project has strict Checkstyle rules configured:
```bash
# Run checkstyle (currently has 113+ violations)
mvn checkstyle:check

# Common violations to fix:
# - Missing Javadoc comments
# - Line length > 80 characters  
# - Missing final parameters
# - Trailing whitespace
# - Tab characters (use spaces)
```

**Note**: Checkstyle violations do NOT block compilation or tests, but should be addressed for code quality.

## Project Architecture

### Hexagonal Architecture Structure
```
src/main/java/com/martinachov/hexagonal/
├── domain/                           # Core business logic
│   ├── model/Product.java           # Domain entities
│   ├── service/ProductService.java  # Domain services
│   └── exception/                   # Domain exceptions
├── application/ports/               # Application layer interfaces
│   ├── input/                      # Use cases (inbound ports)
│   └── output/                     # Repository contracts (outbound ports)
└── infrastructure/adapters/        # Infrastructure layer
    ├── input/                      # REST controllers (inbound adapters)
    ├── output/persistence/         # JPA repositories (outbound adapters)
    └── config/                     # Spring configuration
```

### Key Architectural Components

**Domain Layer** (`domain/`):
- `Product.java`: Core domain entity with business rules
- `ProductService.java`: Domain service implementing use cases
- Pure business logic, no framework dependencies

**Application Layer** (`application/ports/`):
- `CreateProductUseCase.java`: Interface for creating products
- `GetProductUseCase.java`: Interface for retrieving products  
- `ProductOutputPort.java`: Interface for persistence operations

**Infrastructure Layer** (`infrastructure/adapters/`):
- `ProductRestAdapter.java`: REST API endpoints (/v1/products)
- `ProductPersistenceAdapter.java`: JPA implementation
- `ProductRepository.java`: Spring Data JPA repository
- `BeanConfiguration.java`: Spring bean wiring

## Common Issues and Workarounds

### Build Failures
1. **"release version 21 not supported"**: Java version mismatch
   - Solution: Set JAVA_HOME to Java 21 (see above)

2. **Maven wrapper errors**: Missing wrapper files
   - Solution: Use system `mvn` instead of `./mvnw`

3. **First build slow**: Maven downloading dependencies
   - Expected: Initial build takes 2-3 minutes due to downloads

### Application Issues
1. **Port 8080 already in use**: 
   ```bash
   # Find and kill process using port 8080
   lsof -ti:8080 | xargs kill -9
   ```

2. **Application won't start**: Check Java version and dependencies
   ```bash
   java -version  # Must show Java 21
   mvn dependency:tree  # Check for conflicts
   ```

## Configuration Files

- **`pom.xml`**: Maven configuration, dependencies, Spring Boot 3.4.4
- **`src/main/resources/application.yml`**: Spring Boot configuration
  - Server port: 8080
  - H2 database: jdbc:h2:mem:testdb
  - JPA: show-sql enabled, ddl-auto: update

## Testing

### Current Test Suite
- **Single test**: `HexagonalArchitectureApplicationTests.java`
- **Purpose**: Spring context loading verification
- **Runtime**: ~5 seconds
- **Database**: H2 in-memory (auto-created for tests)

### Adding New Tests
Follow existing patterns:
- Use `@SpringBootTest` for integration tests
- Use `@MockBean` for mocking adapters
- Test through use case interfaces, not direct service calls

## Documentation

Additional documentation in `cpguide/`:
- `technical/architecture.md`: Detailed architecture documentation (TaskFlow project context)
- `category.md`: Category entity specification
- `store.md`: Store entity specification

**Note**: The cpguide documentation describes a larger TaskFlow application - this repository implements only the Product entity as a hexagonal architecture example.

## Trust These Instructions

These instructions are comprehensive and tested. Only perform additional searches if:
1. Commands fail despite following exact steps
2. New features require understanding not covered here
3. Java/Maven version requirements change

The build and test processes have been validated and should work reliably when Java 21 is properly configured.