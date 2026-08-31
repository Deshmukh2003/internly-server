# Internly Server

Spring Boot REST API for Internly, a domain-independent internship recommendation platform.

## Stack

- Java 21+
- Spring Boot
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- JWT and BCrypt
- Maven
- OpenAPI / Swagger UI

## Configuration

Configuration is in `src/main/resources/application.yml`. Use environment variables for real credentials:

`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`, and `CORS_ALLOWED_ORIGINS`.

The development admin defaults to `admin@mail.com` / `admin123`. The password is BCrypt-hashed before persistence, and startup seeding is idempotent. Override these values outside local development.

## Run locally

Create a MySQL database or use the configured `createDatabaseIfNotExist=true` URL, then run:

```bash
mvn spring-boot:run
```

Swagger UI is available at `/swagger-ui/index.html` after startup.

## Current API and architecture

- `POST /api/auth/register` creates a student account in an unverified state.
- `POST /api/auth/login` authenticates verified students and the seeded admin.
- `GET /api/auth/me` returns the authenticated user without password data.
- JWT middleware and backend role enforcement protect `/api/student/**` and `/api/admin/**`.
- DTO validation and structured exception responses are enabled.
- Normalized skills, flexible student profiles, companies, and internships are modeled for multiple academic domains.
- `MatchingEngine` provides deterministic, explainable weighted matching and has a cross-domain unit test.

The next slices add email OTP verification, password reset, profile/skill APIs, internship/company CRUD, recommendation persistence, applications, and notifications.

## Tests

```bash
mvn test
```
