# 📚 Library Management  REST API

A RESTful web service for managing a book library. This application allows users to perform CRUD operations on books, search books by various criteria, and switch between development and production profiles with different database configurations.

---

## 🚀 Features

- 🔧 Full CRUD operations for `Book` entities
- 📖 Search by:
  - Book ID
  - ISBN
  - Title
  - Topic (e.g., Java, Operating Systems, Networking)
  - Reader Level (Beginner, Intermediate, Advanced)
- ✅ Input validation with annotations like `@Valid`, `@NotBlank`, `@Size`, `@Positive`
- ⚠️ Exception handling with meaningful HTTP responses
- 🧪 Unit and integration testing with MockMvc and H2
- 🗃️ Profile support:
  - `dev`: in-memory H2 database
  - `prod`: PostgreSQL
- 🧩 DTO-based architecture and entity mappers
- ✅ Logging for important events and errors

---

## 🔐 User Management and Security

- User registration and login stored in the `users` table with password encryption  
- Role-based access control (`USER`, `ADMIN`) with Spring Security
- Session-based authentication 

---
## 📦 Technologies

- Java 21
- Spring Boot 3
- Spring Web (REST)
- Spring Security
- Spring Data JPA + Hibernate
- PostgreSQL (prod)
- H2 Database (dev)
- Bean Validation (`jakarta.validation`)
- Lombok
- Maven
- JUnit + Mockito

---

## 🛠️ Getting Started

### Prerequisites

- JDK 21
- PostgreSQL (for production)
- Maven 

### Clone the Repository

```bash
git clonehttps://github.com/imelia78/library-management-system.git
cd library-management-system
