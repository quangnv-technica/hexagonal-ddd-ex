# Project Architecture Document

**Project Name:** TaskFlow
**Last Updated:** 2025-09-08
**Authors:** [Nguyen Viet Quang], [Tran Duc Minh]

---

## 1. Introduction

This document provides a high-level overview of the `TaskFlow` application's architecture. It is intended for developers, system administrators, and stakeholders to understand the system's structure, components, and how they interact. The goal of this architecture is to be scalable, maintainable, and robust.

## 2. Guiding Principles

- **Microservices-based:** The system is composed of small, independent services.
- **Stateless Services:** Services should not store session data or state. State should be managed by a database or cache.
- **Asynchronous Communication:** Services communicate primarily through a message queue to ensure loose coupling.
- **API-First:** All service interactions are defined by well-documented APIs.
- **Fault Tolerance:** The system should be resilient to the failure of individual components.

## 3. High-Level Architecture Diagram
+----------------+      +----------------+      +----------------+
|                |      |                |      |                |
|  User Interface  |----->| API Gateway  |----->|   Microservice A  |
| (Web/Mobile)   |      |                |      |  (User Service)  |
|                |      |                |      |                |
+----------------+      +------+---------+      +----------------+
|      |
|      |
|      v
|  +----------------+
|  |   Microservice B |
|  |  (Task Service)  |
|  +----------------+
|
v
+----------+
|  Message |
|  Queue   |
+----------+
|
v
+----------+
|  Worker  |
+----------+

## 4. Architectural Components

### 4.1. Core Services

#### **User Service**
- **Purpose:** Manages user authentication, profiles, and roles.
- **Technology Stack:**
    - **Language:** Python
    - **Framework:** FastAPI
    - **Database:** PostgreSQL (using SQLAlchemy ORM)
    - **Authentication:** OAuth2/JWT
- **Key Endpoints:**
    - `POST /users/register`
    - `POST /users/login`
    - `GET /users/{id}`

#### **Task Service**
- **Purpose:** Manages the creation, retrieval, and modification of tasks.
- **Technology Stack:**
    - **Language:** Node.js
    - **Framework:** Express.js
    - **Database:** MongoDB
- **Key Endpoints:**
    - `POST /tasks`
    - `GET /tasks/{id}`
    - `PUT /tasks/{id}`

### 4.2. Supporting Infrastructure

#### **API Gateway**
- **Purpose:** Single entry point for all client requests. Handles request routing, authentication, and rate limiting.
- **Technology Stack:** Nginx or Amazon API Gateway.
- **Integration:** Routes requests to the appropriate microservice based on the URL path.

#### **Message Queue**
- **Purpose:** Facilitates asynchronous communication between services. Used for events like "task created" or "user updated."
- **Technology Stack:** RabbitMQ or Redis Streams.
- **Message Format:** JSON. All messages must adhere to a predefined schema.

#### **Databases**
- **PostgreSQL:** Primary relational database for structured data (e.g., user profiles, roles).
- **MongoDB:** Document database for flexible data (e.g., tasks, comments).

#### **Caching**
- **Purpose:** Reduces database load and improves performance for frequently accessed data.
- **Technology Stack:** Redis.
- **Caching Strategy:**
    - User session data.
    - Frequently viewed tasks.

### 5. Data Flow and Communication

#### **Synchronous Communication**
- The API Gateway routes requests directly to the relevant service.
- Example: `GET /users/{id}` -> API Gateway -> User Service -> PostgreSQL.

#### **Asynchronous Communication**
- A service publishes an event to the message queue. Other services that are "interested" in that event can consume it.
- Example: A user creates a new task.
    1. The Task Service receives the request and creates the task in MongoDB.
    2. The Task Service publishes a "task.created" event to the message queue.
    3. A separate Worker service consumes this event and sends a notification to the user.

### 6. Deployment and Operations

- **Containerization:** All services are containerized using Docker.
- **Orchestration:** Kubernetes is used for container orchestration, scaling, and service discovery.
- **CI/CD:** GitHub Actions are used for continuous integration and deployment.
- **Monitoring:** Prometheus and Grafana are used for monitoring service metrics and creating dashboards.

### 7. Future Considerations

- **Serverless Functions:** Explore using AWS Lambda or similar for event-driven, short-lived tasks to reduce operational overhead.
- **Service Mesh:** Consider implementing a service mesh (e.g., Istio) for advanced traffic management and security.
- **GraphQL API:** Implement a GraphQL API to give clients more flexibility in data retrieval.

---

*This document is a living artifact and will be updated as the system evolves. For more detailed information on a specific service, refer to its dedicated `README.md` file.*