# 📚 Book Library REST API

A RESTful web service for managing a book library. This application allows users to perform CRUD operations on books, search books by various criteria, and switch between development and production profiles with different database configurations.

---

## 🚀 Features

- ✅ Full CRUD support for `Book` entities
- ✅ REST API with JSON serialization
- ✅ Search books by:
  - ID
  - ISBN
  - Title
  - Topic (Java, OS, Computer Architecture, etc.)
  - Reader Level (Beginner, Intermediate, Advanced)
- ✅ Input validation using `@Valid`, `@NotBlank`, etc.
- ✅ Exception handling with proper HTTP status codes
- ✅ Profile support:
  - **`dev`** → uses in-memory H2 database
  - **`prod`** → uses PostgreSQL

---

## 📦 Technologies

- Java 21
- Spring Boot 3
- Spring Web (REST)
- Spring Data JPA
- Hibernate ORM
- PostgreSQL (prod)
- H2 Database (dev)
- Bean Validation (`jakarta.validation`)
- Lombok
- Maven

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
