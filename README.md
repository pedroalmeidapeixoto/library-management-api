# Library Management API

REST API for managing a library, built with **Java 17, Spring Boot, JPA and PostgreSQL**.

The project models users, books, physical copies and loans, with CRUD and loan rules in the service layer and a PostgreSQL **PL/pgSQL procedure**, functions and triggers responsible for processing book returns, late fees and loan validation. The schema is versioned with **Flyway**.

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
- Flyway (database migrations)
- Lombok
- JUnit 5, Mockito, Spring Boot Test, Testcontainers
- GitHub Actions
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
- PostgreSQL procedure, functions and triggers integration
- Versioned schema migrations with Flyway
- Unit tests (mapper and service layers) and integration tests on a real PostgreSQL
- Continuous integration with GitHub Actions
- Postman collection for API testing

## Database Logic

One of the project's main technical points is the use of **PL/pgSQL** for database-side business logic.

The procedure `prc_realizar_devolucao`, created by [`V2__procedure_realizar_devolucao.sql`](src/main/resources/db/migration/V2__procedure_realizar_devolucao.sql), handles a book return by:

1. Finding the loan and its physical copy.
2. Validating that the loan exists.
3. Recording the actual return date.
4. Calculating the number of overdue days.
5. Creating a late-fee record when necessary at **R$ 1.50 per overdue day**.
6. Updating the loan status.
7. Making the physical copy available again.

[`V4__functions_and_triggers.sql`](src/main/resources/db/migration/V4__functions_and_triggers.sql) adds the database-side pieces used by the loan and fine endpoints:

- `fn_exemplar_disponivel`, `fn_calcular_total_multa_pendente` and `fn_multas_usuario`
- `trg_validar_emprestimo`: rejects a loan for a copy that is not available
- `trg_marcar_exemplar_emprestado`: marks the copy as borrowed when the loan is created
- `trg_auditoria_emprestimo`: records every loan insert and update in `auditoria_emprestimo`

## Database Migrations

The schema is managed by Flyway. Migrations run automatically on startup from `src/main/resources/db/migration`:

| Migration | Purpose |
|---|---|
| `V1__baseline_schema.sql` | Original schema: tables, enums, constraints, indexes and the `vw_usuario_saldo` view |
| `V2__procedure_realizar_devolucao.sql` | Return procedure and late-fee calculation |
| `V3__align_schema_with_jpa_model.sql` | Enums become `VARCHAR` + `CHECK`, ids become `BIGINT`, missing columns are added |
| `V4__functions_and_triggers.sql` | Functions and triggers described above |

Hibernate runs with `ddl-auto=validate`, so the application only starts when the migrated schema matches the JPA entities.

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
└── library-management-api.postman_collection.json
```

The requests use the local API:

```text
http://localhost:8080
```

## Project Structure

```text
library-management-api/
├── .github/workflows/
│   └── ci.yml
├── database/
│   └── dev_seed.sql
├── postman/
│   └── library-management-api.postman_collection.json
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
│   │       ├── application-example.properties
│   │       └── db/migration/
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
- Docker (only to run the integration tests)
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

Flyway creates the whole schema when the application starts, so no table needs to be created by hand.

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

### 5. Load sample data (optional)

The Postman collection expects a few users, books, copies and one overdue loan:

```bash
psql -d biblioteca -f database/dev_seed.sql
```

### Upgrading a database created before Flyway

If you already have a database with the original schema (the one in `V1` and `V2`), tell Flyway to treat it as already at version 2 by adding to `application.properties`:

```properties
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=2
```

Only `V3` and `V4` are then applied. `V3` converts the enum columns of existing rows (for example `aluno` becomes `ALUNO` in `usuario`).

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

```bash
mvn verify
```

The suite has two layers:

- **Unit tests** (JUnit 5 and Mockito): `UsuarioMapper`, `EmprestimoService` and `DevolucaoService`.
- **Integration tests** (`DatabaseIntegrationTest`, Testcontainers): start a PostgreSQL container, run the Flyway migrations and verify, through the application services, the return procedure, the late fee of R$ 1.50 per day, the loan triggers and the fine functions. Docker must be running.

The same command runs in GitHub Actions on every push and pull request to `main`.

The Postman collection can be used for manual endpoint testing against the running API.

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
- Versioned database migrations with Flyway
- Unit testing with JUnit 5 and Mockito
- Integration testing on a real PostgreSQL with Testcontainers
- Continuous integration with GitHub Actions
- API testing with Postman
- Maven project management

## License

This project is available for educational and portfolio purposes.
