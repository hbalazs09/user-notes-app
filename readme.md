# Notes Backend API

A Spring Boot application providing a REST API to manage users and their associated notes.

---

## Technologies Used

- **Java 21**
- **Spring Boot 3.5.0**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
- **H2 Database (in-memory)**
- **Flyway** for database migrations
- **Springdoc OpenAPI (Swagger UI)**
- **JUnit 5** for unit testing
- **Maven** for build and dependency management

---


##  How to Build and Run

### 1. **Clone the repository**
```bash
git clone https://github.com/hbalazs09/user-notes-app.git
cd user-notes-app
```

### 2. **Build the application**
```bash
mvn clean install
```

### 3. **Run the application**
```bash
mvn spring-boot:run
```

The application starts on: [http://localhost:8080](http://localhost:8080)

---

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

---

## H2 Console

Accessible at:
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:usernotesdb`
- User: `sa`
- Password: *(leave blank)*

---

## Migrations

Flyway migration scripts are located in:
```
src/main/resources/db/migration/
```
- Tables and seed data are created automatically on app startup.

