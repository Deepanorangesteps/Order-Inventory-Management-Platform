# API Documentation

## Base URL

http://localhost:8080

The exact API prefix should match the controller mappings in the application.

## Authentication

Secured endpoints require the authentication mechanism configured in Spring Security.

Typical request:


Authorization: Bearer <JWT_TOKEN>


## Standard Success Response

json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}


## Standard Error Response

json
{
  "timestamp": "2026-09-21T23:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/products",
  "details": []
}


## Organization

Typical operations:

POST   /api/v1/organizations
GET    /api/v1/organizations/{organizationId}
GET    /api/v1/organizations
PUT    /api/v1/organizations/{organizationId}
DELETE /api/v1/organizations/{organizationId}
```

Use the exact paths exposed by Swagger if they differ.

## Users

Typical operations:

POST /api/v1/users
GET  /api/v1/users/{userId}
GET  /api/v1/users
```

User access is controlled by application roles.

## Products

POST /api/v1/products
GET  /api/v1/products/{productId}
GET  /api/v1/products
PUT  /api/v1/products/{productId}
DELETE /api/v1/products/{productId}
GET  /api/v1/products/search?page=0&size=10&search=laptop
```

## Warehouses

POST /api/v1/warehouses
GET  /api/v1/warehouses/{warehouseId}
GET  /api/v1/warehouses
PUT  /api/v1/warehouses/{warehouseId}
DELETE /api/v1/warehouses/{warehouseId}
GET  /api/v1/warehouses/search?page=0&size=10&search=chennai
```

## Inventory

POST /api/v1/inventory
GET  /api/v1/inventory/{inventoryId}
GET  /api/v1/inventory
PUT  /api/v1/inventory/{inventoryId}
DELETE /api/v1/inventory/{inventoryId}
GET  /api/v1/inventory/search?page=0&size=10
```

Optional filters:
productId
warehouseId

Example:


GET /api/v1/inventory/search?page=0&size=10&productId=1&warehouseId=1


## Customers

POST /api/v1/customers
GET  /api/v1/customers/{customerId}
GET  /api/v1/customers
PUT  /api/v1/customers/{customerId}
DELETE /api/v1/customers/{customerId}
GET  /api/v1/customers/search?page=0&size=10&search=john

## Orders

POST /api/v1/orders
GET  /api/v1/orders/{orderId}
GET  /api/v1/orders
GET  /api/v1/orders/search?page=0&size=10
PATCH /api/v1/orders/{orderId}/status
PATCH /api/v1/orders/{orderId}/cancel


Status filtering:

GET /api/v1/orders/search?page=0&size=10&status=PENDING
GET /api/v1/orders/search?page=0&size=10&status=CONFIRMED
GET /api/v1/orders/search?page=0&size=10&status=COMPLETED
GET /api/v1/orders/search?page=0&size=10&status=CANCELLED


## Swagger

Swagger/OpenAPI is the executable API reference for the exact endpoint paths, request models, response models, and security configuration.

http://localhost:8080/swagger-ui/index.html

