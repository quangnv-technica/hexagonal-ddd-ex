# Hexagonal Architecture Visual Guide

## 1. Hexagonal Architecture Diagram

```
                                    ┌─────────────────────────────────────────┐
                                    │          INFRASTRUCTURE LAYER           │
                                    │                                         │
                ┌───────────────────┤         PRIMARY ADAPTERS               │
                │                   │                                         │
                │  ┌─────────────┐  │  ┌─────────────────────────────────┐   │
                │  │    REST     │  │  │      ProductRestAdapter         │   │
                │  │   Client    │  │  │   - @RestController             │   │
                │  │             │  │  │   - HTTP endpoints              │   │
                │  └─────────────┘  │  │   - Request/Response mapping    │   │
                │         │         │  └─────────────────────────────────┘   │
                │         │ HTTP    │                    │                    │
                │         │         │                    │ implements         │
                │         ▼         │                    ▼                    │
                │  ┌─────────────┐  │  ┌─────────────────────────────────┐   │
                │  │    JSON     │  │  │        INPUT PORTS              │   │
                │  │  Request    │  │  │   ┌─────────────────────────┐   │   │
                │  │             │  │  │   │ CreateProductUseCase    │   │   │
                │  └─────────────┘  │  │   │ GetProductUseCase       │   │   │
                │                   │  │   └─────────────────────────┘   │   │
                └───────────────────┤                    │                    │
                                    │                    │                    │
                                    └────────────────────┼────────────────────┘
                                                         │
                    ┌─────────────────────────────────────────────────────┐
                    │              APPLICATION LAYER                      │
                    │                                                     │
                    │                       │                             │
                    │                       ▼                             │
                    │            ┌─────────────────────┐                  │
                    │            │   ProductService    │                  │
                    │            │   - Business Logic  │                  │
                    │            │   - Use Case Impl   │                  │
                    │            │   - Domain Rules    │                  │
                    │            └─────────────────────┘                  │
                    │                       │                             │
                    │                       │ uses                        │
                    │                       ▼                             │
                    │            ┌─────────────────────┐                  │
                    │            │    OUTPUT PORTS     │                  │
                    │            │  ProductOutputPort  │                  │
                    │            │   - saveProduct()   │                  │
                    │            │   - getById()       │                  │
                    │            └─────────────────────┘                  │
                    │                       │                             │
                    └───────────────────────┼─────────────────────────────┘
                                            │
                    ┌─────────────────────────────────────────────────────┐
                    │                 DOMAIN LAYER                        │
                    │                                                     │
                    │                       │                             │
                    │                       ▼                             │
                    │              ┌─────────────────┐                    │
                    │              │     Product     │                    │
                    │              │   - id: Long    │                    │
                    │              │   - name        │                    │
                    │              │   - description │                    │
                    │              └─────────────────┘                    │
                    │                       ▲                             │
                    │                       │                             │
                    │          ┌─────────────────────────┐                │
                    │          │ ProductNotFoundException │                │
                    │          │   - Domain Exception    │                │
                    │          └─────────────────────────┘                │
                    │                                                     │
                    └───────────────────────┼─────────────────────────────┘
                                            │
                    ┌─────────────────────────────────────────────────────┐
                    │          INFRASTRUCTURE LAYER                       │
                    │                                                     │
                    │                       │                             │
                    │                       ▼                             │
                    │        ┌─────────────────────────────────┐          │
                    │        │      SECONDARY ADAPTERS         │          │
                    │        │                                 │          │
                    │        │  ProductPersistenceAdapter      │          │
                    │        │   - implements OutputPort       │          │
                    │        │   - Database operations         │          │
                    │        │   - Entity mapping              │          │
                    │        └─────────────────────────────────┘          │
                    │                       │                             │
                    │                       │ uses                        │
                    │                       ▼                             │
                    │        ┌─────────────────────────────────┐          │
                    │        │      ProductRepository          │          │
                    │        │   - Spring Data JPA             │          │
                    │        │   - Database Interface          │          │
                    │        └─────────────────────────────────┘          │
                    │                       │                             │
                    │                       │ persists to                 │
                    │                       ▼                             │
                    │        ┌─────────────────────────────────┐          │
                    │        │         DATABASE                │          │
                    │        │      (H2 / PostgreSQL)         │          │
                    │        │                                 │          │
                    │        │  ProductEntity Table            │          │
                    │        │   - id (Primary Key)            │          │
                    │        │   - name                        │          │
                    │        │   - description                 │          │
                    │        └─────────────────────────────────┘          │
                    └─────────────────────────────────────────────────────┘
```

## 2. Dependency Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    DEPENDENCY DIRECTION                         │
│                                                                 │
│  Infrastructure  ──────────►  Application  ──────────►  Domain │
│                                                                 │
│     Adapters     ──────────►     Ports     ──────────►  Models │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 3. Data Transformation Flow

```
HTTP Request (JSON)
         │
         ▼
┌─────────────────┐
│ ProductRequest  │ ──── REST Layer ────┐
│ - name         │                     │
│ - description  │                     │
└─────────────────┘                     │
         │                              │
         │ ModelMapper                  │
         ▼                              │
┌─────────────────┐                     │
│    Product      │ ──── Domain Layer ──┤
│ - id           │                     │
│ - name         │                     │
│ - description  │                     │
└─────────────────┘                     │
         │                              │
         │ ProductMapper                │
         ▼                              │
┌─────────────────┐                     │
│ ProductEntity   │ ── Persistence ─────┘
│ - id (@Id)     │     Layer
│ - name         │
│ - description  │
└─────────────────┘
         │
         ▼
    Database Table
```

## 4. Port Types

### Primary Ports (Input Ports)
- **Purpose**: Define what the application can do
- **Location**: Application layer
- **Implementation**: Domain services
- **Called by**: Primary adapters (REST controllers, etc.)

### Secondary Ports (Output Ports)  
- **Purpose**: Define what the application needs
- **Location**: Application layer
- **Implementation**: Secondary adapters (repositories, external APIs)
- **Called by**: Domain services

## 5. Key Characteristics

### ✅ Benefits Achieved
- **Testability**: Domain logic can be tested without database
- **Flexibility**: Easy to change web framework or database
- **Maintainability**: Clear separation of concerns
- **Technology Independence**: Core logic independent of frameworks

### 🏗️ Architecture Rules Enforced
1. **Domain layer** has no external dependencies
2. **Application layer** defines contracts (ports)
3. **Infrastructure layer** implements the contracts (adapters)
4. **Dependencies point inward** toward the domain

---

*This visual guide illustrates how the hexagonal architecture maintains clean separation between business logic and technical implementation details.*