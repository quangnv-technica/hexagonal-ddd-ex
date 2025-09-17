# User Service

**Module Name:** `user-service`
**Team/Owner:** TaskFlow Core Services Team
**Last Updated:** 2025-09-08

---

## 1. Overview

The User Service is a core microservice responsible for managing all aspects of user data within the `TaskFlow` application. This includes user registration, authentication, profile management, and role-based access control. It provides a RESTful API for other services and clients to interact with user data.

## 2. Core Responsibilities

- **User Authentication:** Handles user sign-up and login, generating and validating JWT tokens.
- **Profile Management:** Allows users to view and update their profile information (e.g., name, email, avatar).
- **Role-Based Access Control (RBAC):** Manages user roles (e.g., `admin`, `standard`, `guest`) and permissions.
- **Account Deactivation:** Provides a mechanism for users to deactivate their accounts.

## 3. Technology Stack

- **Language:** Node.js
- **Framework:** Express.js
- **Database:** MongoDB (using Mongoose ODM)
- **Authentication Library:** `jsonwebtoken`, `bcrypt`
- **Testing:** Jest, Supertest
- **Containerization:** Docker

## 4. API Endpoints

The service exposes a RESTful API. All requests require a valid JWT in the `Authorization` header, unless specified otherwise.

### User Authentication

- `POST /users/register`: Registers a new user.
    - **Request Body:** `{ "username": "string", "email": "string", "password": "string" }`
    - **Response:** `201 Created`, `{ "message": "User created successfully" }`

- `POST /users/login`: Authenticates a user and returns a JWT token.
    - **Request Body:** `{ "username": "string", "password": "string" }`
    - **Response:** `200 OK`, `{ "token": "string" }`

### Profile Management

- `GET /users/me`: Retrieves the current authenticated user's profile.
    - **Response:** `200 OK`, `{ "id": "string", "username": "string", "email": "string", ... }`

- `PUT /users/me`: Updates the current authenticated user's profile.
    - **Request Body:** `{ "email": "string" }` (or other fields)
    - **Response:** `200 OK`, `{ "message": "Profile updated successfully" }`

### Admin Endpoints (Requires `admin` role)

- `GET /users/{id}`: Retrieves a specific user's profile by ID.
- `PUT /users/{id}/role`: Updates a user's role.
    - **Request Body:** `{ "role": "string" }`

## 5. Data Model

The primary data model is the `User` schema, defined in `src/models/User.js`.

### `User` Schema

```javascript
// src/models/User.js

const userSchema = new mongoose.Schema({
  username: { type: String, required: true, unique: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  role: { type: String, enum: ['user', 'admin'], default: 'user' },
  createdAt: { type: Date, default: Date.now },
});