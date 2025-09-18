# Documentation Index

This directory contains comprehensive documentation for the Hexagonal Architecture implementation.

## 📚 Available Documentation

### Architecture Documentation
- **[Architecture Overview](technical/architecture.md)** - Complete explanation of the hexagonal architecture implementation
- **[Visual Guide](../docs/hexagonal-architecture-visual-guide.md)** - Visual diagrams and architecture flow charts

### Practical Guides
- **[Testing Guide](../docs/testing-guide.md)** - How to test and run the application
- **[Main README](../README.md)** - Project overview and getting started

## 🏗️ Architecture Layers

### Domain Layer (Core Business Logic)
- `Product.java` - Domain model
- `ProductService.java` - Business logic
- `ProductNotFoundException.java` - Domain exceptions

### Application Layer (Ports)
- `CreateProductUseCase.java` - Input port for creating products
- `GetProductUseCase.java` - Input port for retrieving products  
- `ProductOutputPort.java` - Output port for persistence

### Infrastructure Layer (Adapters)
- `ProductRestAdapter.java` - REST API adapter
- `ProductPersistenceAdapter.java` - Database adapter
- `ProductRepository.java` - JPA repository

## 🚀 Quick Navigation

1. **Start here**: [README.md](../README.md) for project overview
2. **Understand the architecture**: [Architecture Documentation](technical/architecture.md)
3. **See it visually**: [Visual Guide](../docs/hexagonal-architecture-visual-guide.md)
4. **Try it out**: [Testing Guide](../docs/testing-guide.md)

## ⚠️ Note

The `category.md` and `store.md` files contain domain specifications that are not yet implemented in the current codebase. They represent potential future extensions to the Product domain.