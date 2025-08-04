# Domestic Services API (v1.0.2)

Welcome to the Domestic Services REST API!  
This API allows you to manage and interact with domestic service offerings, users, and requests.  
The API is built with Spring Boot and secured using JWT (JSON Web Tokens).

## Table of Contents

- [Base URL](#base-url)
- [Authentication](#authentication)
- [Endpoints](#endpoints)
- [Example Usage](#example-usage)
- [Error Handling](#error-handling)
- [Running Locally](#running-locally)

---

## Base URL

```
https://your-domain.com/api/v1/
```

---

## Authentication

All endpoints (except for login/register) require a valid JWT token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

### Obtaining a Token

Send a POST request to `/auth/login` with your credentials:

```http
POST /auth/login
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

Successful response:

```json
{
  "token": "<your_jwt_token>"
}
```

---

## Endpoints

> **Note:** Actual endpoints may vary. Here is a common structure for REST APIs:

### Auth

- `POST /auth/login` – Login and receive a JWT token
- `POST /auth/register` – Register a new account

### Users

- `GET /users` – List all users (JWT required)
- `GET /users/{id}` – Get user details
- `PUT /users/{id}` – Update user info
- `DELETE /users/{id}` – Delete a user

### Services

- `GET /services` – List all services
- `GET /services/{id}` – Get service details
- `POST /services` – Create a new service
- `PUT /services/{id}` – Update a service
- `DELETE /services/{id}` – Delete a service

### Requests

- `GET /requests` – List all service requests
- `POST /requests` – Create a new service request
- `GET /requests/{id}` – Get request details

---

## Example Usage

### Get List of Services

```http
GET /services
Authorization: Bearer <your_jwt_token>
```

### Create a Service

```http
POST /services
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
  "name": "Cleaning",
  "description": "Professional house cleaning service",
  "price": 80
}
```

### Register a User

```http
POST /auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "newuser@example.com"
}
```

---

## Error Handling

- All errors are returned with a relevant HTTP status code and message.
- Example:

```json
{
  "error": "Unauthorized",
  "message": "JWT token is missing or invalid"
}
```

---

## Running Locally

1. Clone the repository:
    ```
    git clone https://github.com/lopezenzoa/domestic_services.git
    ```
2. Switch to branch v1.0.2:
    ```
    git checkout v1.0.2
    ```
3. Build and run the application:
    ```
    ./mvnw spring-boot:run
    ```

---

## Contact & Support

For questions, open an issue in this repository or contact the maintainer.

---

**Note:** This README is a template. Please refer to source code and endpoint documentation for precise details.