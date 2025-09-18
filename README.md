# Hexagonal Architecture with Domain-Driven Design Example

This project demonstrates the implementation of **Hexagonal Architecture** (also known as **Ports and Adapters**) combined with **Domain-Driven Design (DDD)** principles using Spring Boot. It provides a clear example of how to structure an application following these architectural patterns.

## 🏗️ Architecture Overview

The Hexagonal Architecture separates the core business logic (domain) from external concerns (infrastructure) through well-defined interfaces (ports) and implementations (adapters).

### Key Benefits

- **Technology Independence**: The core business logic is independent of frameworks, databases, and external services
- **Testability**: Easy to test business logic in isolation without external dependencies
- **Flexibility**: Easy to swap implementations (e.g., change from H2 to PostgreSQL, REST to GraphQL)
- **Maintainability**: Clear separation of concerns makes the code easier to understand and modify

## 📁 Project Structure

```
src/main/java/com/martinachov/hexagonal/
├── domain/                          # Business Logic Layer (Core)
│   ├── model/                       # Domain Entities
│   │   └── Product.java
│   ├── service/                     # Domain Services
│   │   └── ProductService.java
│   └── exception/                   # Domain Exceptions
│       └── ProductNotFoundException.java
├── application/                     # Application Layer
│   └── ports/                       # Port Interfaces
│       ├── input/                   # Primary Ports (Use Cases)
│       │   ├── CreateProductUseCase.java
│       │   └── GetProductUseCase.java
│       └── output/                  # Secondary Ports (Infrastructure)
│           └── ProductOutputPort.java
└── infrastructure/                  # Infrastructure Layer
    └── adapters/                    # Adapter Implementations
        ├── input/                   # Primary Adapters (Controllers)
        │   ├── ProductRestAdapter.java
        │   └── rest/data/
        │       ├── request/
        │       │   └── ProductRequest.java
        │       └── response/
        │           └── ProductResponse.java
        ├── output/                  # Secondary Adapters (Repositories)
        │   └── persistence/
        │       ├── ProductPersistenceAdapter.java
        │       ├── entity/
        │       │   └── ProductEntity.java
        │       ├── mapper/
        │       │   └── ProductMapper.java
        │       └── repository/
        │           └── ProductRepository.java
        └── config/                  # Configuration
            └── BeanConfiguration.java
```

## 🔄 Architecture Layers Explained

### 1. Domain Layer (Core/Business Logic)
- **Pure business logic** with no external dependencies
- Contains domain models, business rules, and domain services
- **Independent** of frameworks, databases, and external services

### 2. Application Layer (Use Cases)
- Defines **input ports** (primary ports) - what the application can do
- Defines **output ports** (secondary ports) - what the application needs
- Orchestrates domain objects to fulfill business use cases

### 3. Infrastructure Layer (Technical Details)
- Contains **adapters** that implement the ports
- **Primary Adapters**: Handle input (REST controllers, message listeners)
- **Secondary Adapters**: Handle output (databases, external APIs, file systems)

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/quangnv-technica/hexagonal-ddd-ex.git
cd hexagonal-ddd-ex
```

2. Build the project:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. The application will start on `http://localhost:8080`

### API Endpoints

#### Create a Product
```bash
POST http://localhost:8080/v1/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop for development"
}
```

#### Get a Product by ID
```bash
GET http://localhost:8080/v1/products/{id}
```

## 🔍 Understanding the Implementation

### Data Flow Example

1. **HTTP Request** → `ProductRestAdapter` (Primary Adapter)
2. **REST Adapter** → `CreateProductUseCase` (Input Port)
3. **Use Case** → `ProductService` (Domain Service)
4. **Domain Service** → `ProductOutputPort` (Output Port)
5. **Output Port** → `ProductPersistenceAdapter` (Secondary Adapter)
6. **Persistence Adapter** → `ProductRepository` (Database)

### Dependency Direction

The key principle is that **dependencies point inward** toward the domain:

```
Infrastructure → Application → Domain
```

- Infrastructure depends on Application
- Application depends on Domain  
- Domain depends on nothing external

This is achieved through **Dependency Inversion Principle** using interfaces (ports).

## 🧪 Testing Strategy

The hexagonal architecture makes testing straightforward:

- **Unit Tests**: Test domain logic in isolation
- **Integration Tests**: Test adapters with real infrastructure
- **Contract Tests**: Test that adapters correctly implement ports

## 📚 Learn More

### References
- [Hexagonal Architecture by Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- [Clean Architecture by Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Related Patterns
- **Clean Architecture**: Similar layered approach with dependency inversion
- **Onion Architecture**: Another implementation of the same principles
- **Ports and Adapters**: The original name for Hexagonal Architecture
