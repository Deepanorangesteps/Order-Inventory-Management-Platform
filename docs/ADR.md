# ADR-001: Use a Modular Monolith
## Context

The assignment contains several closely related business modules including organization, user, product, warehouse, inventory, customer, and order management.

Deploying each module as an independent microservice would add unnecessary operational complexity for the scope of the assignment.

## Decision

Implement the application as a modular monolith using Spring Boot with clear package/module boundaries.

Each module follows the controller/service/repository/entity pattern.
=================================================================
# ADR-002: JWT Authentication and Role-Based Authorization
## Context

The API contains operations that should not be available to every authenticated user.

## Decision

Use Spring Security with JWT authentication and role-based authorization.

Authentication establishes the user identity. Authorization rules determine which roles can access protected endpoints.

=====================================================================
# ADR-003: Organization-Based Tenant Isolation
## Context

The application manages data for multiple organizations.

A user from one organization must not access another organization's business records.

## Decision

Every organization-owned business entity stores an organization reference.

Service methods obtain the current organization from the authenticated security context and include that organization in repository queries.
===========================================================================
# ADR-004: Use Request and Response DTOs

## Context

Exposing JPA entities directly from controllers couples the API contract to the persistence model.

## Decision

Use separate request and response DTOs.

HTTP Request
    |
Request DTO
    |
Service
    |
Entity
    |
Response DTO
    |
HTTP Response
===================================================================================
# ADR-005: Reserve Inventory During Order Creation

## Context

An order should not consume physical inventory immediately when it is merely created, but the requested quantity must not remain available to another order.

## Decision

When an order is created, the requested quantity is added to inventory's reserved quantity.

Available inventory is calculated as:


available = quantity - reservedQuantity


When an order is completed, physical quantity is reduced and the reservation is released.

When an order is cancelled before completion, the reservation is released without reducing physical quantity.
================================================================================
# ADR-006: Use Spring Data Pagination

## Context

Returning all records from large collections is inefficient and does not scale.

## Decision

Use Spring Data `Pageable` and `Page` for list/search endpoints.

The API accepts:

page
size

and module-specific search/filter parameters.


