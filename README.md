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
- `POST /api/auth/verify-otp` verifies a six-digit email code and activates a student.
- `POST /api/auth/resend-otp` issues a replacement code subject to cooldown controls.
- `POST /api/auth/forgot-password` requests a reset code without revealing whether an email exists.
- `POST /api/auth/reset-password` consumes a valid reset code and BCrypt-hashes the new password.
- `GET/PUT /api/student/profile` reads or updates the authenticated student's profile.
- `GET /api/student/skills`, `POST /api/student/skills`, and `DELETE /api/student/skills/{skillId}` manage normalized student skills.
- `GET /api/internships` provides paginated active internships with optional `search`, `domain`, and `location` filters.
- Admin company and internship CRUD is available under `/api/admin/companies` and `/api/admin/internships`.
- `GET /api/internships/{id}` returns an active internship detail response.
- `GET /api/recommendations` refreshes deterministic profile matches and returns ranked, persisted recommendations with score explanations.
- `POST/GET /api/applications` supports student applications and application tracking; duplicate, closed, inactive, and expired applications are rejected.
- `GET /api/admin/applications` and `PATCH /api/admin/applications/{id}/status` support admin review and status updates.
- `GET /api/notifications`, `GET /api/notifications/unread-count`, and `PATCH /api/notifications/{id}/read` support student notifications.
- Application submission and status changes create student notifications automatically.
- `GET /api/auth/me` returns the authenticated user without password data.
- JWT middleware and backend role enforcement protect `/api/student/**` and `/api/admin/**`.
- DTO validation and structured exception responses are enabled.
- Normalized skills, flexible student profiles, companies, and internships are modeled for multiple academic domains.
- `MatchingEngine` provides deterministic, explainable weighted matching and has a cross-domain unit test.

OTP and password-reset records are stored as BCrypt hashes, expire after ten minutes, allow five attempts, and are never returned or logged. Configure `MAIL_HOST` and related mail variables to deliver messages; local development safely stores the verification state without attempting SMTP when no host is configured.

## Tests

```bash
mvn test
```
