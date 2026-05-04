# Library Management System

A backend application for managing digital library operations. Supports user registration,
book catalog management, borrowing and returning books, and overdue tracking.
Includes role-based access control, JWT authentication, and real-time stock updates via WebFlux.

## Features

- User registration and JWT authentication
- Role-based authorization (LIBRARIAN, PATRON)
- Book management (CRUD)
- Borrowing and returning books
- Overdue tracking and reporting
- Real-time book stock updates via Server-Sent Events (WebFlux)
- Unit and integration testing with H2 and MockMvc
- Dockerized for easy deployment

## Tech Stack

| Area | Technology |
|------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security, JWT |
| Persistence | Spring Data JPA, PostgreSQL |
| Test Database | H2 |
| Reactive | Spring WebFlux, Project Reactor |
| API Docs | Swagger (Springdoc OpenAPI) |
| Testing | JUnit, Mockito |
| Logging | SLF4J, Logback |
| Containerization | Docker, Docker Compose |

## Running Locally

Prerequisites: Java 17+, Maven 3+, PostgreSQL (optional if using Docker)

```bash
git clone https://github.com/bahajyy/Library-Management-System.git
cd library-management-system
./mvnw spring-boot:run
