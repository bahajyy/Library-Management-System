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
```

Base URL: `http://localhost:8080/api`
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Docker

```bash
docker-compose up --build
```

Services: Spring Boot app + PostgreSQL with volume. Exposes `http://localhost:8080`.

## Database Schema

![db_schema](https://github.com/user-attachments/assets/bb9d71ac-92c6-4a57-9a6d-9bb1163ae12d)

**Entities:**
- User: name, email, role (LIBRARIAN / PATRON)
- Book: title, stock, genre, metadata
- Borrowing: tracks which user borrowed which book, return status, due dates, overdue state

**Relationships:**
- User → Borrowing: one-to-many
- Book → Borrowing: one-to-many

## System Architecture

![system_architecture](https://github.com/user-attachments/assets/23e8bb8a-58e1-451e-9170-7a45b6442bcc)

Layered architecture: Controller → Service → Repository → Database.
Security layer handles JWT validation and role-based access.
Reactive layer publishes real-time stock updates via WebFlux.

**Auth flow:** User logs in → JWT returned → JWT included in subsequent requests → roles determine access.

## API Reference

Full documentation available via Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

**Auth** (`/api/auth`)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/login` | Authenticate and receive JWT | No |

**Users** (`/api/users`)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | `/users/register` | Register a new user | No |
| GET | `/users/{id}` | Get user by ID | LIBRARIAN |
| PUT | `/users/{id}` | Update user | LIBRARIAN |
| DELETE | `/users/{id}` | Delete user | LIBRARIAN |

**Books** (`/api/books`)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | `/books` | Create a book | LIBRARIAN |
| PUT | `/books/{id}` | Update a book | LIBRARIAN |
| DELETE | `/books/{id}` | Delete a book | LIBRARIAN |
| GET | `/books` | Search books (paginated) | LIBRARIAN, PATRON |
| GET | `/books/{id}` | Get book by ID | LIBRARIAN, PATRON |

**Borrowings** (`/api/borrowings`)

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | `/borrowings/borrow` | Borrow a book | PATRON |
| POST | `/borrowings/return/{id}` | Return a book | PATRON |
| GET | `/borrowings/my-history/{id}` | Personal borrow history | PATRON |
| GET | `/borrowings/all` | All borrowings | LIBRARIAN |
| GET | `/borrowings/overdue` | Overdue borrowings | LIBRARIAN |
| GET | `/borrowings/overdueBooks` | Overdue book details | LIBRARIAN |
| GET | `/borrowings/overdueBooks/report` | Overdue report (plain text) | LIBRARIAN |

For secured endpoints, include `Authorization: Bearer <token>` in the request header.

## Postman Collection

[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/44797193-fdbdd7d5-038d-44b7-b917-686f3b36d150?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D44797193-fdbdd7d5-038d-44b7-b917-686f3b36d150%26entityType%3Dcollection%26workspaceId%3Dec8eb9df-3f99-48db-bb29-2aabdeb10261)

## Error Handling and Logging

Global exception handling via `@RestControllerAdvice` returns consistent JSON error responses.
Logging uses SLF4J + Logback — all controller and service actions are logged.
Logs are written to console and to `/logs/app.log` (configurable in `logback-spring.xml`).

## Testing

69 test cases, all passing. 94% line coverage, 88% method coverage.

![test results](https://github.com/user-attachments/assets/bd6dab5f-3aed-4b66-b7c2-446ad6069959)

![coverage](https://github.com/user-attachments/assets/e28e924e-fa5a-4f57-a04e-9aca4204d968)

## Scheduled Tasks

`OverdueCheckScheduler` runs daily at midnight, checks for unreturned books past their due date,
and updates their status to OVERDUE automatically.

## Real-Time Stock Updates

Book availability is streamed via Server-Sent Events.

```
GET /api/reactive/books/stream
Accept: text/event-stream
```

Every borrow or return triggers a `BookStockUpdate` event published through `BookStockPublisher`
and emitted as a `Flux<BookStockUpdate>` to subscribed clients.

Example payload:
```json
{
  "bookId": 3,
  "title": "Effective Java",
  "stock": 7
}
```

Test it by opening `http://localhost:8080/api/reactive/books/stream` in a browser or HTTP client.
