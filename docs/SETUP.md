# Setup Guide

## Prerequisites

Install:

- Java 22
- Maven
- MySQL
- Docker (optional)
- Eclipse IDE (optional)
- Git

Swagger:

http://localhost:8080/swagger-ui/index.html


## 8. Recommended Test Order

1. Create organization
2. Create user
3. Authenticate
4. Verify role authorization
5. Create product
6. Create warehouse
7. Create inventory
8. Create customer
9. Create order
10. Verify inventory reservation
11. Update order status
12. Verify completion/cancellation inventory behavior
13. Test pagination
14. Test validation errors
15. Test unauthorized/forbidden access


Check:

- MySQL is running
- database name is correct
- username/password are correct
- port is correct

### 401
Check that the JWT is present and valid.

### 403
Check the authenticated user's role and endpoint authorization rule.

### 400
Check request validation and business-rule error messages.

### 404
Check the requested entity ID and organization context.
