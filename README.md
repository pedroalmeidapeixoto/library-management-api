# Library Management API

REST API for managing a library, built with **Java 17, Spring Boot, JPA and PostgreSQL**.

The project models users, books, physical copies and loans, with business rules implemented in the service layer and a PostgreSQL **PL/pgSQL procedure** responsible for processing book returns and late fees.

> Backend-focused project created to practice REST API development, relational database modeling, object-oriented programming and database-side business logic.

## Tech Stack

- **Java 17**
- **Spring Boot 3.3.2**
- Spring Web
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- **PostgreSQL**
- **PL/pgSQL**
- Maven
- Lombok
- JUnit 5 / Spring Boot Test
- Postman

## Architecture

The application follows a layered backend architecture:

```text
HTTP Request
     │
     ▼
Controllers
     │
     ▼
Services ──── Business Rules
     │
     ▼
Repositories
     │
     ▼
PostgreSQL
```

DTOs are used at the API boundary, while mappers keep entity/DTO conversion isolated from controllers and services.

## Main Features

- User CRUD
- Book CRUD
- Physical copy management
- Loan management
- Loan filtering by user and status
- Book return processing
- Late-fee calculation
- Standardized error responses
- Bean Validation
- CORS configuration
- PostgreSQL procedure integration
- Unit tests for the mapper layer
- Postman collection for API testing

## Database Logic

One of the project's main technical points is the use of **PL/pgSQL** for database-side business logic.

The procedure [`database/procedures/prc_realizar_devolucao.sql`](database/procedures/prc_realizar_devolucao.sql) handles a book return by:

1. Finding the loan and its physical copy.
2. Validating that the loan exists.
3. Recording the actual return date.
4. Calculating the number of overdue days.
5. Creating a late-fee record when necessary at **R$ 1.50 per overdue day**.
6. Updating the loan status.
7. Making the physical copy available again.

The SQL file is included in the repository so the database-side implementation can be inspected independently from the Java application.

## API Testing

A Postman collection is included in the repository to facilitate API testing.

The collection contains requests covering:

- API health check
- User CRUD
- Book CRUD
- Physical copy operations
- Loan creation and listing
- Loan updates
- Book returns
- Late-fee verification
- Physical copy availability
- Database procedure and trigger-related operations

The collection can be found at:

```text
postman/
└── Library Management API.postman_collection.json
```

The requests use the local API:

```text
http://localhost:8080
```

## Project Structure

```text
library-management-api/
├── database/
│   └── procedures/
│       └── prc_realizar_devolucao.sql
├── postman/
│   └── Library Management API.postman_collection.json
├── src/
│   ├── main/
│   │   ├── java/com/biblioteca/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application-example.properties
│   └── test/
│       └── java/com/biblioteca/
├── .editorconfig
├── .gitattributes
├── .gitignore
├── pom.xml
└── README.md
```

## Local Setup

### Requirements

- Java 17+
- Maven 3.9+
- PostgreSQL 14+ recommended
- Postman (optional, for API testing)

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/library-management-api.git
cd library-management-api
```

### 2. Create the database

Create a PostgreSQL database for the application:

```sql
CREATE DATABASE biblioteca;
```

The JPA configuration is set to `ddl-auto=update`, allowing Hibernate to create and update the mapped tables when the application starts.

### 3. Configure the application

Copy:

```text
src/main/resources/application-example.properties
```

to:

```text
src/main/resources/application.properties
```

Then replace `YOUR_PASSWORD` with the password of your local PostgreSQL user.

`application.properties` is intentionally ignored by Git so local credentials are not committed to the repository.

### 4. Install dependencies and run

```bash
mvn clean install
mvn spring-boot:run
```

The API starts on:

```text
http://localhost:8080
```

### 5. Install the PostgreSQL procedure

After the required tables exist, execute:

```text
database/procedures/prc_realizar_devolucao.sql
```

in the application's PostgreSQL database.

## Main Endpoints

### Users

```text
POST   /usuarios
GET    /usuarios
GET    /usuarios/{id}
PUT    /usuarios/{id}
DELETE /usuarios/{id}
```

Additional filters include email, name, user type and status.

### Books

```text
POST   /livros
GET    /livros
GET    /livros/{id}
PUT    /livros/{id}
DELETE /livros/{id}
```

Additional filters include title and genre.

### Physical Copies

```text
POST   /api/exemplares
GET    /api/exemplares
GET    /api/exemplares/{id}
PUT    /api/exemplares/{id}
DELETE /api/exemplares/{id}
GET    /api/exemplares/disponiveis
```

### Loans

```text
POST   /api/emprestimos
GET    /api/emprestimos
GET    /api/emprestimos/{id}
PUT    /api/emprestimos/{id}
DELETE /api/emprestimos/{id}
```

The API also provides loan queries by user and status.

### Returns and Fines

```text
POST /api/emprestimos/{id}/devolver
GET  /api/usuarios/{id}/multas
GET  /api/exemplares/{id}/disponivel
GET  /api/health
```

The return endpoint integrates the application with the PostgreSQL procedure responsible for processing the return and calculating possible late fees.

## Testing

Run the unit tests with:

```bash
mvn test
```

The current test suite covers the `UsuarioMapper`, including:

- DTO-to-entity conversion
- Entity-to-response conversion
- Partial entity updates

The Postman collection can be used to perform integration and endpoint testing against the running API.

## Security and Configuration

Local database credentials are never intended to be committed.

Use:

```text
application-example.properties
```

as the template for local configuration.

For a production deployment, database credentials and other environment-specific values should be provided through environment variables or a secrets manager rather than committed configuration files.

## Project Highlights

This project demonstrates:

- REST API development with Spring Boot
- Layered backend architecture
- JPA entity modeling and repositories
- DTO-based API contracts
- Object-oriented programming
- Validation with Jakarta Bean Validation
- Centralized exception handling
- PostgreSQL integration
- PL/pgSQL procedure development
- Database-side business rules
- Unit testing with JUnit 5
- API testing with Postman
- Maven project management

## License

This project is available for educational and portfolio purposes.
