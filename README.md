# Finance Tracker Backend

A RESTful backend system for managing financial transactions with role-based access control, built using Spring Boot and MySQL.

---

## Overview

Finance Tracker is a backend API that allows users to manage financial records based on their roles. The system supports transaction management, dashboard analytics, user management, and enforces strict role-based access control across all endpoints.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Validation | Spring Boot Validation |
| Build Tool | Maven |
| Boilerplate | Lombok |

---

## Project Structure
```
src/main/java/com/example/financetracker/
 ├── config/
 │    └── RoleGuard.java
 ├── controller/
 │    ├── TransactionController.java
 │    └── UserController.java
 ├── exception/
 │    └── GlobalExceptionHandler.java
 ├── model/
 │    ├── Transaction.java
 │    └── User.java
 ├── repository/
 │    ├── TransactionRepository.java
 │    └── UserRepository.java
 └── service/
      ├── TransactionService.java
      └── UserService.java
```
---

## How to Run

### Prerequisites
- Java 17+
- MySQL running locally
- Maven installed

### Steps

1. Clone the repository
     git clone https://github.com/vignesh05047/finanace-tracker cd finanace-tracker
2. Create MySQL database
     CREATE DATABASE finance_db;
3.  Update src/main/resources/application.properties
     spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
4.  Run the application
      mvn spring-boot:run
5.  API runs at http://localhost:8080
    ---

## Role Model

The system supports three roles passed via the X-Role request header:

| Role | Permissions |
|---|---|
| ADMIN | Full access — create, read, update, delete |
| ANALYST | Read and update only — cannot create or delete |
| VIEWER | Read only — cannot modify any data |

All requests must include the X-Role header. Requests without it return 400 Bad Request.

---

## API Endpoints

### Transactions — /api/transactions

| Method | Endpoint | Description | Role Required |
|---|---|---|---|
| POST | /api/transactions | Add a new transaction | ADMIN |
| GET | /api/transactions?userId=&role= | Get transactions | ALL |
| PUT | /api/transactions/{id}?role= | Update a transaction | ADMIN |
| DELETE | /api/transactions/{id}?role= | Delete a transaction | ADMIN |
| GET | /api/transactions/dashboard | Get dashboard summary | ALL |
| GET | /api/transactions/filter?type=&category= | Filter by type and category | ALL |
| GET | /api/transactions/filter/date?date= | Filter by date | ALL |

### Users — /users

| Method | Endpoint | Description | Role Required |
|---|---|---|---|
| POST | /users | Create a new user | ADMIN |
| GET | /users | Get all users | ADMIN |
| GET | /users/{id} | Get user by ID | ALL |
| PUT | /users/{id}/role?newRole= | Update user role | ADMIN |
| PATCH | /users/{id}/status | Toggle active/inactive | ADMIN |
| DELETE | /users/{id} | Delete a user | ADMIN |
| POST | /users/login | Login | ALL |

---

## Request and Response Examples

### Add Transaction
POST /api/transactions
Headers: X-Role: ADMIN
Content-Type: application/json
{
"type": "income",
"userId": "u1",
"role": "ADMIN",
"amount": 5000,
"category": "salary",
"date": "2026-04-04"
}
Response: Transaction added successfully

### Dashboard Response
GET /api/transactions/dashboard
Headers: X-Role: ADMIN
{
"totalIncome": 5000.0,
"totalExpense": 2000.0,
"netBalance": 3000.0,
"byCategory": {
"salary": 5000.0,
"food": 2000.0
},
"recentActivity": [...]
}

### Validation Error Response
{
"status": 400,
"error": "Validation failed",
"fields": {
"amount": "amount must be greater than zero",
"category": "category is required"
}
}

### Access Denied Response
{
"error": "VIEWER can only view data"
}

---

## Access Control Behavior

| Role | POST | GET | PUT | DELETE |
|---|---|---|---|---|
| ADMIN | YES | YES | YES | YES |
| ANALYST | NO 403 | YES | YES | NO 403 |
| VIEWER | NO 403 | YES | NO 403 | NO 403 |
| No header | NO 400 | NO 400 | NO 400 | NO 400 |

---

## Assumptions Made

- Authentication is simulated via X-Role request header — no JWT implemented to keep focus on backend logic and access control
- userId is passed as a request parameter to identify the user making the request
- Date is stored as a String in yyyy-MM-dd format for simplicity
- Passwords are stored as plain text — no encryption applied as this is an assessment project
- ADMIN role can manage both transactions and users
- Soft delete was not implemented — records are permanently removed on delete

---

## Key Design Decisions

- Separation of concerns — controller, service, repository, and model layers are clearly separated
- Centralized error handling — GlobalExceptionHandler catches all validation errors, runtime exceptions, and unexpected errors and returns consistent JSON responses
- Role enforcement at filter level — RoleGuard intercepts every request before it reaches the controller, ensuring no endpoint can be accessed without proper authorization
- Clean service layer — all business logic lives in the service layer, keeping controllers thin and readable

---

## Author

- Name: Vignesh A
- Role Applied: Backend Developer Intern
- Submission Date: April 2026