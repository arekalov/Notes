# 📝 Notes API

> Modern REST API for notes management with JWT authentication

[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)](https://jwt.io/)

## 🚀 Description

Notes API is a RESTful web service for creating, reading, updating, and deleting notes. The application is built using modern technology stack and provides secure authentication through JWT tokens.

### ✨ Key Features

- 🔐 **JWT Authentication** - Secure login system with access and refresh tokens
- 📝 **CRUD Operations** - Complete set of operations for notes management
- 👤 **Multi-user Support** - Each user sees only their own notes
- 🎨 **Color Coding** - Ability to assign colors to notes
- 📚 **Swagger UI** - Interactive API documentation
- 🛡️ **Data Validation** - Input data validation for correctness
- 🏗️ **Clean Architecture** - Clear separation of application layers

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.25 | Main programming language |
| **Spring Boot** | 3.5.5 | Application framework |
| **Spring Security** | 6.5.3 | Security and authentication |
| **Spring Data JPA** | 3.5.3 | Database operations |
| **PostgreSQL** | 42.7.7 | Relational database |
| **JWT** | 0.12.6 | JSON Web Tokens for authentication |
| **Swagger/OpenAPI** | 2.2.0 | API documentation |
| **Gradle** | 8.x | Build system |

## 📋 Requirements

- **Java 17+**
- **PostgreSQL 12+**
- **Gradle 8.x**

## 🚀 Quick Start

### 1. Clone the repository
```bash
git clone https://github.com/arekalov/notes-api.git
cd notes-api
```

### 2. Database setup
Create a PostgreSQL database and update settings in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notes_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the application
```bash
./gradlew bootRun
```

The application will be available at: `http://localhost:8080`

### 4. Swagger UI
Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`

## 🚀 Deployment

### JAR Build
```bash
./gradlew build
java -jar build/libs/notes-0.0.1-SNAPSHOT.jar
```
