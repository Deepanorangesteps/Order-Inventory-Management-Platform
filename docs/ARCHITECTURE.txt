# Architecture

## 1. Architectural Style

The application is implemented as a modular monolith using Spring Boot.

The primary goal is separation of responsibilities while keeping deployment simple as a single application.

+------------------------------------------------+
|                 REST API Layer                 |
| Controllers                                    |
+------------------------------------------------+
|                 Business Layer                 |
| Services / Service Implementations             |
+------------------------------------------------+
|                  Data Layer                    |
| Spring Data JPA Repositories                   |
+------------------------------------------------+
|                 Persistence                    |
| MySQL                                          |
+------------------------------------------------+
=================================================================================================
## 2. Modules

### Organization

Represents the tenant/company boundary.


### User and Security

Manages application users, roles, authentication, and authorization.

### Product

Manages products belonging to an organization.

### Warehouse

Manages warehouses belonging to an organization.

### Inventory

Maintains product stock for warehouses.

### Customer

Maintains customers belonging to an organization.

### Order

Manages customer orders, order items, status transitions, and inventory reservation/fulfillment.
==================================================================================================================
## 3. Layer Responsibilities

### Controller

Responsible for:

- HTTP endpoints
- Request DTOs
- Validation triggering
- HTTP status codes
- Response DTOs

Controllers should not contain business rules.

### Service

Responsible for:

- Business rules
- Transaction boundaries
- Organization/tenant context
- Entity-to-response mapping where applicable
- Coordination between repositories

### Repository

Responsible for:

- Database queries
- Entity persistence
- Organization-scoped queries
- Pagination/filtering

### Entity

Represents the persistence model and relationships.

### DTO

Request DTOs isolate API input from persistence entities.

Response DTOs prevent persistence entities from being exposed directly.
====================================================================================================

## 4. Security Flow


HTTP Request
     |
     v
JWT Authentication Filter
     |
     v
Authenticated User
     |
     v
Role Authorization
     |
     v
Controller
     |
     v
Service
 =========================================================================================================
## 5. Tenant Isolation

Business data is scoped to an organization.

Services obtain the current organization from the authenticated user/security context and repository queries include the organization identifier.

Conceptually:
Authenticated User
       |
       v
organizationId
       |
       v
Service
       |
       v
Repository query
       |
       v
Only records belonging to that organization
======================================================================================================

## 6. Order and Inventory Flow


Create Order
    |
    v
Validate Customer
    |
    v
Validate Product/Warehouse
    |
    v
Check Available Inventory
    |
    v
Reserve Inventory
    |
    v
Create Order

Completion:


CONFIRMED
    |
    v
COMPLETED
    |
    +--> Deduct physical quantity
    |
    +--> Release reserved quantity


Cancellation:


PENDING/CONFIRMED
    |
    v
CANCELLED
    |
    +--> Release reserved quantity
==============================================================================
## 7. Error Handling

A centralized `@RestControllerAdvice` handles application exceptions.

Examples:

- Resource not found -> 404
- Invalid business operation -> 400
- Validation failure -> 400
- Unexpected exception -> 500
==============================================================================
## 9. Future Extensions

The architecture allows adding:

- Redis caching
- asynchronous workflows
- audit/event logging
- inventory transaction history
- advanced reporting
- external integrations

without changing the basic controller/service/repository structure.

				   