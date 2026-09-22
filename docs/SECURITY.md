# Security

## Authentication

The application uses Spring Security for API authentication.

JWT is used to carry authenticated user information between requests.

Typical request:
http
Authorization: Bearer <JWT>


## Authorization

Access is controlled using application roles.

Controller methods use Spring Security authorization rules such as:

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")


The exact role matrix is defined by the security configuration.

## Password Security

Passwords must be stored using a strong password encoder such as BCrypt.

Never store or log raw passwords.

## JWT Security

JWT secrets must not be hard-coded in source code.

Use environment variables or external configuration for:


JWT_SECRET
JWT_EXPIRATION


Do not commit real secrets to Git.

## Tenant Isolation

Authentication identifies the current user and organization.

Business services obtain the organization from the authenticated context.

A client must not be trusted simply because it sends an organization ID.

Example:

JWT
 |
 +--> userId
 |
 +--> role
 |
 +--> organizationId
```

The service uses the authenticated organization when querying data.

## Unauthorized vs Forbidden

The application should distinguish:

401 Unauthorized
```

from:

403 Forbidden


401 indicates missing/invalid authentication.

403 indicates that the authenticated user does not have permission for the requested operation.

## API Validation

Request DTOs use Bean Validation where applicable.

Examples:

@NotBlank
@NotNull
@Positive
@Email
```

Validation failures are converted to the standard error response by the global exception handler.

## Security Logging

Do not log:

- passwords
- JWT tokens
- secrets
- sensitive authentication data

Logs should contain useful operational information without exposing credentials.

## Security Checklist

Before submission:

- [ ] Passwords are encoded
- [ ] JWT secret is externalized
- [ ] Secured endpoints require authentication
- [ ] Role restrictions are enforced
- [ ] Organization isolation is enforced
- [ ] Validation is enabled
- [ ] Secrets are not committed
- [ ] Authentication failures return appropriate status codes
