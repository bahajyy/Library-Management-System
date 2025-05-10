
# Library Management System

Library Management System is a backend application built to manage the operations of a digital library. It supports user registration, book catalog management, borrowing and returning books, and tracking overdue borrowings. The system includes user role-based access (Librarian & Patron), JWT-based authentication, and reactive stock updates via WebFlux.

 # ✨ Key Features:

🔐 User registration & JWT authentication (login)

👤 Role-based authorization (LIBRARIAN, PATRON)

📘 Book management (CRUD operations)

📚 Borrowing & returning books

🕐 Overdue book tracking & reporting

📡 Real-time book stock updates (WebFlux)

🧪 Unit & Integration Testing with H2 and MockMvc

📦 Dockerized for easy deployment

# 🛠 Technology Stack Used

- Java 21 – Main programming language

- Spring Boot 3 – Backend application framework

- Spring Security – Role-based authentication & authorization

- JWT (JSON Web Tokens) – Stateless token-based authentication

- Spring Data JPA – ORM and repository abstraction

- PostgreSQL – Relational production database

- H2 Database – In-memory test database

- Spring WebFlux – Reactive streams for live stock updates

- Swagger (Springdoc OpenAPI) – API documentation & testing interface

- JUnit & Mockito – Unit and integration testing

- Logback & SLF4J – Centralized and structured logging framework

- Docker & Docker Compose – Containerization & orchestration

- Postman – API testing and sharing (with exported collection)

 # 🚀 Running the Application Locally

✅ Prerequisites
- Java 17+

- Maven 3+

- PostgreSQL (optional if running with Docker)

💻 Steps

1 - Clone the repository

```bash
git clone https://github.com/bahajyy/Library-Management-System.git

cd library-management-system

```
2- Update application properties (optional if not using Docker):

```bash
src/main/resources/application.yml

```
3- Run with Maven

```bash
./mvnw spring-boot:run

```
4- Access API

Base URL: http://localhost:8080/api

Swagger: http://localhost:8080/swagger-ui/index.html

🐳 Docker Setup & Run Instructions

- Build and Run Containers:

```bash
docker-compose up --build

```
📦 Services:
- app → Spring Boot backend

- db → PostgreSQL with volume

- Exposes http://localhost:8080

📁 Docker Files:
- Dockerfile → Spring Boot build image

- docker-compose.yml → PostgreSQL + App

## 🗃️ Database Schema
This application uses a relational schema powered by PostgreSQL. Below is the simplified ER (Entity-Relationship) schema representing the main entities and relationships in the system:

![db_schema](https://github.com/user-attachments/assets/bb9d71ac-92c6-4a57-9a6d-9bb1163ae12d)


📄 Entities Overview:

- User: Stores user details such as name, email, role (LIBRARIAN / PATRON).

- Book: Represents a book in the library, including stock, genre, and metadata.

- Borrowing: Keeps track of which user borrowed which book, and tracks return status, due dates, and overdue states.

🔗 Relationships:

- A User can borrow multiple Books → One-to-Many between User and Borrowing

- A Book can be borrowed multiple times → One-to-Many between Book and Borrowing

## 🏗️ System Architecture

The application follows a Layered Architecture with clear separation of concerns:

![system_architecture](https://github.com/user-attachments/assets/23e8bb8a-58e1-451e-9170-7a45b6442bcc)


🔧 Key Components:

- Controller Layer: Exposes RESTful endpoints to clients (e.g., Postman or Web UI).

- Service Layer: Contains business logic such as validations, calculations, and orchestration.

- Repository Layer: Uses Spring Data JPA for persistence, managing data access via UserRepository, BookRepository, etc.

- Security Layer: Implements JWT-based authentication and method-level authorization.

- Reactive Layer: Publishes real-time stock updates using Spring WebFlux + Project Reactor (via BookStockPublisher).

- Database: PostgreSQL, accessed through JPA/Hibernate.

🔐 Auth Flow:

- User logs in with email and password.

- JWT is generated and sent back.

- JWT is included in headers for protected endpoints.

- Roles (LIBRARIAN, PATRON) determine endpoint access.

 ## 📌 API Reference

This application provides RESTful endpoints to manage users, books, and borrowings in a library system. All endpoints are secured using JWT-based authentication and role-based authorization.

- 🔗 Full API documentation is available via Swagger UI at:

```http
http://localhost:8080/swagger-ui/index.html

```

🧑‍💼 AuthController (/api/auth):

| Method | Endpoint      | Description                          | Request Body   | Auth Required |
| ------ | ------------- | ------------------------------------ | -------------- | ------------- |
| POST   | `/auth/login` | Authenticates a user and returns JWT | `LoginRequest` | ❌ No          |

👤 UserController

| Method | Endpoint          | Description               | Request Body        | Roles       |
| ------ | ----------------- | ------------------------- | ------------------- | ----------- |
| POST   | `/users/register` | Registers a new user      | `CreateUserRequest` | ❌ No        |
| GET    | `/users/{id}`     | Get a specific user by ID | —                   | ✅ LIBRARIAN |
| PUT    | `/users/{id}`     | Update user details       | `UpdateUserRequest` | ✅ LIBRARIAN |
| DELETE | `/users/{id}`     | Delete a user             | —                   | ✅ LIBRARIAN |

📚 BookController

| Method | Endpoint      | Description                    | Request Body        | Roles               |
| ------ | ------------- | ------------------------------ | ------------------- | ------------------- |
| POST   | `/books`      | Create a new book              | `CreateBookRequest` | ✅ LIBRARIAN         |
| PUT    | `/books/{id}` | Update an existing book        | `UpdateBookRequest` | ✅ LIBRARIAN         |
| DELETE | `/books/{id}` | Delete a book                  | —                   | ✅ LIBRARIAN         |
| GET    | `/books`      | Search books (with pagination) | — (query params)    | ✅ LIBRARIAN, PATRON |
| GET    | `/books/{id}` | Get a book by ID               | —                   | ✅ LIBRARIAN, PATRON |


🔄 BorrowingController

| Method | Endpoint                          | Description                          | Request Body        | Roles       |
| ------ | --------------------------------- | ------------------------------------ | ------------------- | ----------- |
| POST   | `/borrowings/borrow`              | Borrow a book                        | `BorrowBookRequest` | ✅ PATRON    |
| POST   | `/borrowings/return/{id}`         | Return a borrowed book               | —                   | ✅ PATRON    |
| GET    | `/borrowings/my-history/{id}`     | Get current user’s borrow history    | —                   | ✅ PATRON    |
| GET    | `/borrowings/all`                 | Get all borrowings                   | —                   | ✅ LIBRARIAN |
| GET    | `/borrowings/overdue`             | Get all overdue borrowings           | —                   | ✅ LIBRARIAN |
| GET    | `/borrowings/overdueBooks`        | Get detailed overdue book list       | —                   | ✅ LIBRARIAN |
| GET    | `/borrowings/overdueBooks/report` | Generate overdue report (plain text) | —                   | ✅ LIBRARIAN |

📌 Authorization: Bearer <token> must be included in headers for secured endpoints.

📘 For request/response details, refer to Swagger UI at /swagger-ui/index.html.

# 📬 Postman API Collection
You can explore and test all API endpoints using the Postman collection provided below:

🔑 Note: For authenticated endpoints, you must first use the /auth/login endpoint to obtain a JWT token and set it in the Authorization header as: Bearer <your_token_here>

[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/44797193-fdbdd7d5-038d-44b7-b917-686f3b36d150?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D44797193-fdbdd7d5-038d-44b7-b917-686f3b36d150%26entityType%3Dcollection%26workspaceId%3Dec8eb9df-3f99-48db-bb29-2aabdeb10261)

## 🛡️ Error Handling & Logging
Global Exception Handling

- This project includes a centralized error handling mechanism using @RestControllerAdvice. All exceptions thrown in the application are caught and returned in a consistent JSON structure.

Logging with SLF4J + Logback

- SLF4J is used as the abstraction layer.

- Logback is the underlying logging implementation.

- All controller and service actions are logged, including important events such as login attempts, book borrowings, and stock updates.

- Logs are written both to the console and to a file located in /logs/app.log (configurable in logback-spring.xml).

## ✅ Testing
This project has been extensively tested to ensure correctness, robustness, and reliability. All service, controller, mapper, exception handler, and security components have been covered with unit and integration tests.

✔️ Test Results

- All 69 test cases were executed successfully as shown in the screenshot below. The system has passed 100% of the defined test scenarios.

![image](https://github.com/user-attachments/assets/bd6dab5f-3aed-4b66-b7c2-446ad6069959)


🧪 Code Coverage

- I achieved 94% line coverage and 88% method coverage across the entire project, ensuring high confidence in the stability of the codebase.

![image](https://github.com/user-attachments/assets/e28e924e-fa5a-4f57-a04e-9aca4204d968)


 ## ⏰ Scheduled Tasks
I added a bonus scheduler mechanism designed to run background tasks at specific intervals to support automated maintenance operations.

📘 Overdue Book Checker

- A scheduled job named OverdueCheckScheduler has been implemented to:

- Run daily at midnight

- Automatically check for overdue borrowings

- Update their status to OVERDUE if the due date has passed and the book has not been returned

✅ Benefits

- Keeps borrowing statuses up-to-date in real time

- Ensures that patrons are correctly flagged for late returns

- Prepares data for reports or alerts triggered by overdue statuses


## ⚡ Reactive Programming – Real-Time Stock Updates

To enable real-time tracking of book availability, the application exposes a server-sent event (SSE) endpoint that continuously streams stock updates to subscribed clients.

📍 Endpoint

```http
GET /api/reactive/books/stream
Accept: text/event-stream
```
🔄 How It Works

- Every time a book is borrowed or returned, a BookStockUpdate event is published via BookStockPublisher.

- These updates are emitted as a Flux<BookStockUpdate>.

- The /stream endpoint continuously sends new stock states to the client in real time using Server-Sent Events (SSE).

📦 Example JSON Payload

```JSON
{
  "bookId": 3,
  "title": "Effective Java",
  "stock": 7
}

```

🧪 How to Test It

```
http://localhost:8080/api/reactive/books/stream

```
You’ll see stream chunks appearing as stock updates.


