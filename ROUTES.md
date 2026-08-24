# API Routes & Endpoints Documentation

This document provides a comprehensive overview of all API routes, web endpoints, and their associated security vulnerabilities in the Java Spring Vulny application.

## Table of Contents

- [Authentication](#authentication)
- [Hotel Management API](#hotel-management-api)
- [User Management API](#user-management-api)
- [Item Management API](#item-management-api)
- [Search Functionality](#search-functionality)
- [Web Pages](#web-pages)
- [Admin Endpoints](#admin-endpoints)
- [Payload Endpoints](#payload-endpoints)
- [Known Vulnerabilities Summary](#known-vulnerabilities-summary)

---

## Authentication

### JWT Authentication

**Base Path:** `/api/jwt/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/jwt/auth/signin` | Authenticate and receive JWT token | No |

**Test Credentials:**
- Username: `user` (Tenant: `1234567`) - Password: `password`
- Username: `janesmith` (Tenant: `1234567`) - Password: `password`
- Username: `ronburgandy` (Tenant: `12345678`) - Password: `password`

### Form-Based Authentication

| Method | Endpoint | Description | Vulnerabilities |
|--------|----------|-------------|----------------|
| GET | `/login` | Login form | None |
| GET | `/login-code` | Login with code authentication | None |
| GET | `/login-form-multi` | Multi-step login form | None |
| POST | `/login-form-multi` | Submit multi-step login | None |
| GET | `/login-multi-check` | Check login status | None |

---

## Hotel Management API

**Base Path:** `/example/v1/hotels`

### Standard Hotel Endpoints

| Method | Endpoint | Description | Tenant Filtered | Auth Required |
|--------|----------|-------------|----------------|---------------|
| GET | `/example/v1/hotels` | Get paginated list of hotels | ✅ Yes | Yes |
| GET | `/example/v1/hotels/{id}` | Get hotel by ID | ✅ Yes | Yes |
| POST | `/example/v1/hotels` | Create new hotel | ✅ Yes | Yes |
| PUT | `/example/v1/hotels/{id}` | Update hotel | ✅ Yes | Yes |
| DELETE | `/example/v1/hotels/{id}` | Delete hotel | ✅ Yes | Yes |
| GET | `/example/v1/hotels/locations` | Get hotels grouped by location | ✅ Yes | Yes |
| GET | `/example/v1/hotels/random` | Get random hotel | ✅ Yes | Yes |
| GET | `/example/v1/hotels/upper-bounded-list` | Get hotels (upper bounded) | ✅ Yes | Yes |
| GET | `/example/v1/hotels/lower-bounded-list` | Get hotels (lower bounded) | ✅ Yes | Yes |
| GET | `/example/v1/hotels/unbounded-list` | Get hotels (unbounded) | ✅ Yes | Yes |

### 🚨 Vulnerable Hotel Endpoints (BOLA)

| Method | Endpoint | Description | Vulnerability | Impact |
|--------|----------|-------------|---------------|--------|
| GET | `/example/v1/hotels/all-unfiltered` | Get ALL hotels across ALL tenants | **BOLA - Broken Object Level Authorization** | Users can view hotels from other tenants |
| GET | `/example/v1/hotels/unfiltered/{id}` | Get ANY hotel by ID regardless of tenant | **BOLA - Broken Object Level Authorization** | Horizontal privilege escalation via ID enumeration |

**⚠️ Security Note:** These endpoints bypass tenant isolation by using direct JPQL queries through `EntityManager` that explicitly avoid the tenant filter. Unlike the standard endpoints which rely on `HotelRepository` (and thus benefit from the `HotelServiceAspect`), these vulnerable methods demonstrate BOLA vulnerabilities where authenticated users can access resources belonging to other tenants.

**How the Vulnerability Works:**
- Standard endpoints use `hotelRepository.findAll()` → Tenant filter is applied via `HotelServiceAspect` ✅
- Vulnerable endpoints use `entityManager.createQuery("SELECT h FROM Hotel h")` → Bypasses tenant filter ❌

**Example Attack:**
```bash
# Authenticate as ronburgandy (tenant 12345678)
curl -X POST https://localhost:9000/api/jwt/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"ronburgandy","password":"password"}'

# Access hotels from OTHER tenants
curl -X GET https://localhost:9000/example/v1/hotels/all-unfiltered \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Enumerate hotel IDs from other tenants
curl -X GET https://localhost:9000/example/v1/hotels/unfiltered/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## User Management API

**Base Path:** `/api/jwt/users`

### User Endpoints

| Method | Endpoint | Description | Tenant Filtered | Vulnerabilities |
|--------|----------|-------------|----------------|----------------|
| GET | `/api/jwt/users/search/` | Search all users | ✅ Yes | None |
| GET | `/api/jwt/users/search/{text}` | Search users by name | ✅ Yes | None |
| GET | `/api/jwt/users/search/bad/{text}` | Search users (vulnerable) | ✅ Yes | **SQL Injection** |

**🚨 SQL Injection Vulnerability:**

The `/api/jwt/users/search/bad/{text}` endpoint is vulnerable to SQL injection. Example payload:
```
/api/jwt/users/search/bad/test' OR '1'='1
```

---

## Item Management API

**Base Path:** `/api/jwt/items`

### JWT Item Endpoints

| Method | Endpoint | Description | Vulnerabilities |
|--------|----------|-------------|----------------|
| GET | `/api/jwt/items/search/` | Search all items | **SQL Injection, XSS** |
| GET | `/api/jwt/items/search/{text}` | Search items by text | **SQL Injection, XSS** |
| POST | `/api/jwt/items/search` | Search items (POST) | **SQL Injection, XSS** |
| GET | `/api/jwt/items/{id}` | Get item by ID | Type confusion bug |

### Basic Auth Item Endpoints

**Base Path:** `/api/basic/items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/basic/items/search/` | Search all items (Basic Auth) |
| GET | `/api/basic/items/search/{text}` | Search items by text (Basic Auth) |
| POST | `/api/basic/items/search` | Search items POST (Basic Auth) |

### Token Item Endpoints

**Base Path:** `/api/token/items`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/token/items/search/` | Search all items (Token Auth) |
| GET | `/api/token/items/search/{text}` | Search items by text (Token Auth) |
| POST | `/api/token/items/search` | Search items POST (Token Auth) |

---

## Search Functionality

### Web Search

| Method | Endpoint | Description | Vulnerabilities |
|--------|----------|-------------|----------------|
| GET | `/search` | Search form page | None |
| POST | `/search` | Submit search query | **SQL Injection, XSS** |

**🚨 Known Vulnerabilities:**

1. **SQL Injection Example:**
   ```
   a%'; insert into item values (999, 'bad bad description', 'hacker item name'); select * from item where name like  '%banan
   ```

2. **XSS Example:**
   ```html
   <script>alert('XSS');</script>
   ```

---

## Web Pages

### Public Pages

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Home page |
| GET | `/links/**` | Links pages (catch-all) |

### Hidden Pages

| Method | Endpoint | Description | Purpose |
|--------|----------|-------------|---------|
| GET | `/hidden` | Hidden page | Scanner discovery test |
| GET | `/hidden/hidden2` | Second hidden page | Scanner discovery test |
| GET | `/hidden/cypress` | Cypress test page | E2E testing |
| GET | `/hidden/selenium` | Selenium test page | E2E testing |
| GET | `/hidden/playwright` | Playwright test page | E2E testing |

---

## Admin Endpoints

### Admin Pages

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/admin` | Admin dashboard | Yes (Admin role) |
| GET | `/admin/payload/{size}` | Generate payload of specific size | Yes (Admin role) |
| GET | `/admin/payloads` | List available payloads | Yes (Admin role) |
| GET | `/admin/payload/stream/{size}` | Stream payload data | Yes (Admin role) |

---

## Payload Endpoints

**Purpose:** Generate various payload sizes for testing and scanning

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/payload/{size}` | Generate payload of specific size |
| GET | `/payloads` | List all available payload sizes |
| GET | `/payload/stream/{size}` | Stream payload data |

---

## API Documentation

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/openapi` | OpenAPI specification (JSON) |
| GET | `/openapi.yaml` | OpenAPI specification (YAML) |
| GET | `/swagger-ui.html` | Swagger UI documentation |

---

## Known Vulnerabilities Summary

### 🚨 Critical Vulnerabilities

| Vulnerability Type | Affected Endpoints | Severity | Description |
|-------------------|-------------------|----------|-------------|
| **BOLA (Broken Object Level Authorization)** | `/example/v1/hotels/all-unfiltered`<br>`/example/v1/hotels/unfiltered/{id}` | 🔴 **CRITICAL** | Bypasses tenant isolation, allows cross-tenant data access |
| **SQL Injection** | `/search` (POST)<br>`/api/jwt/items/search/{text}`<br>`/api/jwt/users/search/bad/{text}` | 🔴 **CRITICAL** | Allows arbitrary SQL execution |
| **Cross-Site Scripting (XSS)** | `/search` (POST)<br>`/api/jwt/items/search/{text}` | 🟠 **HIGH** | Stored/Reflected XSS in search functionality |

### ⚠️ High Risk Issues

| Issue | Affected Endpoints | Description |
|-------|-------------------|-------------|
| **Type Confusion** | `/api/jwt/items/{id}` | Incorrect type handling for ID parameter |

**Note:** Standard hotel endpoints are now properly secured with `HotelServiceAspect` which automatically enables tenant filtering. Only the deliberately vulnerable endpoints (`/all-unfiltered` and `/unfiltered/{id}`) bypass tenant isolation.

### 📊 Tenant Information

**Configured Tenants:**
- **Tenant ID:** `1234567`
  - Users: `user`, `janesmith`, `user1`, `user2`, `user3`
  - Hotels: Grand Plaza Hotel, Ocean View Resort, Mountain Lodge, City Center Inn

- **Tenant ID:** `12345678`
  - Users: `ronburgandy`, `user4`, `user5`, `user6`
  - Hotels: Royal Palace Hotel, Tokyo Tower Hotel, Safari Lodge, Sydney Harbor Inn

---

## Testing & Scanning Notes

### Authentication Testing

1. **JWT Token Authentication:**
   ```bash
   # Tenant 1234567
   curl -X POST https://localhost:9000/api/jwt/auth/signin \
     -H "Content-Type: application/json" \
     -d '{"username":"janesmith","password":"password"}'

   # Tenant 12345678
   curl -X POST https://localhost:9000/api/jwt/auth/signin \
     -H "Content-Type: application/json" \
     -d '{"username":"ronburgandy","password":"password"}'
   ```

2. **Form-Based Authentication:**
   - Navigate to `/login`
   - Use credentials: `user` / `password`, `janesmith` / `password`, or `ronburgandy` / `password`

### BOLA Testing

Test tenant isolation by authenticating as different users and attempting to access resources:

```bash
# Login as ronburgandy (tenant 12345678)
TOKEN=$(curl -X POST https://localhost:9000/api/jwt/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"ronburgandy","password":"password"}' | jq -r '.token')

# Test SECURE endpoint - should only see tenant 12345678's hotels (4 hotels)
curl -X GET "https://localhost:9000/example/v1/hotels?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# Test VULNERABLE endpoint - will see ALL hotels from ALL tenants (8 hotels)
curl -X GET https://localhost:9000/example/v1/hotels/all-unfiltered \
  -H "Authorization: Bearer $TOKEN"

# Test VULNERABLE endpoint - access a specific hotel from another tenant by ID enumeration
curl -X GET https://localhost:9000/example/v1/hotels/unfiltered/1 \
  -H "Authorization: Bearer $TOKEN"
```

### SQL Injection Testing

```bash
# Test SQL injection in search
curl -X POST https://localhost:9000/search \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "searchText=a%27%20OR%20%271%27%3D%271"
```

---

## Security Best Practices

### ✅ Implemented
1. **Aspect for Hotel Service:** `HotelServiceAspect` automatically enables tenant filters for all `HotelService` methods (similar to `UserServiceAspect` for users)
2. **JWT Authentication:** All hotel endpoints require JWT authentication for access control

### ⚠️ What's Still Missing
1. **Authorization Checks:** Add explicit tenant ownership checks before returning resources
2. **Input Validation:** Implement parameterized queries and input sanitization for all search endpoints
3. **Output Encoding:** Properly encode output to prevent XSS attacks
4. **Remove Vulnerable Endpoints:** Delete `/all-unfiltered` and `/unfiltered/{id}` endpoints in production

---

## Resources

- **Application Base URL:** `https://localhost:9000`
- **Default Credentials:**
  - Tenant 1234567: `user` / `password` or `janesmith` / `password`
  - Tenant 12345678: `ronburgandy` / `password`
- **OpenAPI Spec:** `https://localhost:9000/openapi`
- **Swagger UI:** `https://localhost:9000/swagger-ui.html`

---

**⚠️ IMPORTANT:** This application is intentionally vulnerable for security testing and educational purposes. Never deploy this application to a production environment.
