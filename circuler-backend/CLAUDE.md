# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Start MySQL database (required before running the app)
docker-compose up -d

# Run the application
./mvnw spring-boot:run

# Build
./mvnw clean package

# Run tests
./mvnw test
```

The app connects to MySQL on port **3307** (not 3306). Schema is pre-created by `database/init/circuler-scriptbd.sql`; DDL is set to `validate`.

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when the app is running. Use the **Authorize** button (top-right) to paste a JWT Bearer token and authenticate requests.

## Architecture

Domain-oriented layered architecture under `com.backend.circuler`:

```
entity → dto → mapper → repository → service → controller
```

- **entity**: JPA entities with `@CreationTimestamp`/`@UpdateTimestamp` for audit fields
- **enums**: Enum values stored as `Integer` in DB via JPA `AttributeConverter`; serialized/deserialized as numbers in JSON via `@JsonCreator`/`@JsonValue`
- **dto**: Three DTOs per entity — `EntityCreateDTO` (input), `EntityResponseDTO` (output), `EntityUpdateDTO` (partial update). Auth DTOs live under `dto/auth/`
- **mapper**: Explicit entity↔DTO conversion logic as a `@Component`
- **repository**: `JpaRepository` extensions with custom `@Query` methods
- **service**: Business logic with `@Transactional`; constructor injection; field-by-field null checks for partial updates
- **controller**: `ResponseEntity` returns with appropriate HTTP status codes; `@Tag` annotations for Swagger grouping
- **security**: `JwtUtil` (token generation/validation), `JwtAuthFilter` (`OncePerRequestFilter`), `CustomUserDetailsService` (loads user by email for Spring Security)
- **config**: `SecurityConfig` (STATELESS, CSRF disabled, JWT filter, route rules), `SwaggerConfig` (OpenAPI + Bearer auth scheme), `AdminDataSeeder` (creates root admin on startup)

## Authentication (JWT)

The API uses stateless JWT Bearer authentication.

- **Login:** `POST /api/auth/login` → returns `{ token, email, roles }`
- Token must be sent as `Authorization: Bearer <token>` on protected routes
- Token expiration: configured via `jwt.expiration` in `application.properties` (default: 86400000 ms = 24h)
- Secret key: configured via `jwt.secret` — must be at least 32 characters for HS256

**Root admin** is created automatically on first startup by `AdminDataSeeder`:
- Email: `admin@circuler.com`
- Password: `Admin@123`

## Route Authorization

| Route | Access |
|---|---|
| `POST /api/auth/login` | Public |
| `POST /api/users` | Public (registration) |
| `/swagger-ui/**`, `/v3/api-docs/**` | Public |
| `GET/PATCH/DELETE /api/users/**` | Authenticated (any role) |
| `POST /api/admin/**` | `ROLE_ADMIN` only |

## Critical Conventions

**No Lombok, no Records.** All entities, DTOs, getters, setters, and mappers must be written explicitly.

**Logical deletion only.** Never use physical DELETE. Mark records as deleted by updating the status field to the `APAGADO` (0) enum value via a `@Modifying @Query`. All `findAll`/`findById` queries must filter out `APAGADO` records.

**Enum persistence.** Enums are persisted as integers using a JPA `AttributeConverter` (see `enums/converter/`). Never use `@Enumerated(EnumType.STRING)` or `@Enumerated(EnumType.ORDINAL)`.

**Partial updates.** Service update methods check each field individually with `isBlank()`/null checks, updating only fields present in the request DTO.

**Password encoding.** All passwords are encoded with BCrypt via `PasswordEncoder`. Never store plain-text passwords. Encoding happens in `UserService`, not in the mapper.

**Role assignment.** New users created via `POST /api/users` receive `ROLE_USER` automatically. Promotion to `ROLE_ADMIN` is done via `POST /api/admin/users/{id}/promote` (ADMIN only). The `DaoAuthenticationProvider` constructor in Spring Security 7.x (Spring Boot 4.x) requires `UserDetailsService` as a constructor argument — `setUserDetailsService()` was removed.

**Error messages in Portuguese.** Domain-level exception messages should be in Portuguese.

## Database Tables (defined in `database/init/circuler-scriptbd.sql`)

`users`, `roles`, `role_users`, `books`, `book_instances`, `collection_points`, `reservations`

**Implemented:** `users` (full CRUD + logical delete), `roles` and `role_users` (read-only via `RoleRepository`; seeded by SQL script with `ROLE_ADMIN` and `ROLE_USER`).

**Not yet implemented:** `books`, `book_instances`, `collection_points`, `reservations` — tables exist in the schema but have no entity/service/repository yet.
