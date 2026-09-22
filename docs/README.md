# Order Management Service
# Overview

Order Management Service is a Spring Boot REST backend for managing organizations, users, products, warehouses, inventory, customers, and orders.

The application is designed as a modular monolith with a layered architecture:

- Controller
- Service
- Service Implementation
- Repository
- Entity
- DTO Request / Response
- Security
- Exception Handling
The backend uses MySQL for persistence and Spring Data JPA/Hibernate for database access.

## Technology Stack
- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL
- Spring Security
- JWT-based authentication
- Bean Validation
- Spring Boot Actuator
- Springdoc OpenAPI / Swagger UI
- Maven

## Implemented Modules

- Organization
- User
- Role-based authorization
- Product
- Warehouse
- Inventory
- Customer
- Order
- Order status management
- Inventory reservation during order processing
- Inventory deduction on order completion
- Inventory release on order cancellation
- Global exception handling
- Standard API response
- Pagination and filtering/search
- Swagger/OpenAPI documentation
- Organization-level tenant isolation



## Architecture

The application follows a layered architecture.

Client
  |
  v
REST Controller
  |
  v
Service Interface
  |
  v
Service Implementation
  |
  v
Repository
  |
  v
MySQL


Security is applied before controller execution.


Client
  |
  | JWT
  v
Spring Security
  |
  | Authentication + Authorization
  v
Controller


## Package Structure

com.uphead.order_management

|
+-- controller
+-- dto
|   +-- request
|   +-- response
+-- entity
+-- exception
+-- repository
+-- security
+-- service
|   +-- impl
+-- config


## API Response

Successful APIs use the application's standard response wrapper.

Example:

json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": {}
}
```

Errors are returned through the global exception handler.

## Running the Application

1. Start MySQL.
2. Create/configure the application database.
3. Update `application.properties` or `application.yml`.
4. Build the project:



## Swagger

Swagger UI is enabled through Springdoc OpenAPI.

Open:


http://localhost:8080/swagger-ui/index.html

Use the authentication mechanism configured by the application before calling secured endpoints.

## Testing

Recommended validation before submission:

- Organization CRUD
- User creation/retrieval
- Authentication
- Role-based authorization
- Product CRUD
- Warehouse CRUD
- Inventory CRUD
- Customer CRUD
- Order creation
- Order status changes
- Inventory reservation
- Inventory release on cancellation
- Inventory deduction on completion
- Pagination/search/filter APIs
- Validation failures
- Unauthorized/forbidden requests

## Current Scope

The current implementation is backend-focused. No frontend is required for the backend assignment submission.

