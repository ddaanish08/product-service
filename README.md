# Product and Authentication API

## Overview
This project provides RESTful APIs for product management and user authentication. It includes two primary controllers:

- **ProductController**: Manages product-related operations like creating, fetching, updating, and deleting products.
- **AuthController**: Handles user authentication, registration, and management.


## Prerequisites
To run this application, ensure the following are installed:

- **Java 117**
- **Spring Boot**
- **Gradle 8.5**

## Setup and Installation

1. Install dependencies and run the application:
   ```bash
   gradle clean build
   gradle bootRun
   ```

2. Access the API documentation via Swagger UI:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Endpoints

### Product API

#### Base URL:
`/api/v1/products`

| Method  | Endpoint          | Description                             | Roles Required      |
|---------|-------------------|-----------------------------------------|---------------------|
| POST    | `/`               | Create a new product                   | USER, ADMIN         |
| GET     | `/`               | Get all products                       | USER, ADMIN         |
| DELETE  | `/admin/{id}`     | Delete a product by ID                 | ADMIN               |
| PUT     | `/admin/{id}`     | Update a product by ID                 | ADMIN               |

### Authentication API

#### Base URL:
`/v1/auth`

| Method  | Endpoint          | Description                             |
|---------|-------------------|-----------------------------------------|
| POST    | `/signup`         | Register a new user                    |
| POST    | `/login`          | Authenticate a user and generate token |
| GET     | `/`               | Get all registered users               |

### Security Details

All endpoints except `Auth-Controller API` require JWT-based authentication.

For endpoints restricted to specific roles:
- **ADMIN** role is required for the following endpoints:
   - `DELETE /api/v1/products/admin/{id}`
   - `PUT /api/v1/products/admin/{id}`
- **USER or ADMIN** role is sufficient for the remaining product-related endpoints.

### Swagger Configuration
The application uses **Swagger** for API documentation and testing.
Access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

Add the `bearerAuth` token via the Swagger UI "Authorize" button to access secured endpoints.

## Example Payloads

### Create Product
```json
{
  "name": "Product Name",
  "description": "Product Description",
  "price": 100.0
}
```

### Register User
```json
{
  "username": "user1",
  "password": "password123",
  "role": "USER"
}
```

### Login
```json
{
  "username": "user1",
  "password": "password123"
}
```

### JWT Authentication Header
For secured endpoints, include the JWT token in the `Authorization` header:
```http
Authorization: Bearer <token>
```

## Security
This application uses JWT for secure authentication. Roles (USER, ADMIN) are used to restrict access to certain endpoints. Ensure the `bearerAuth` scheme is properly configured.
