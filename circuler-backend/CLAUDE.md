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
| `POST /api/admin/**` | `ROLE_ROOT_ADMIN` only |
| `GET /api/books/**` | Authenticated (any role) |
| `POST/PATCH/DELETE /api/books/**` | `ROLE_ADMIN` only |
| `GET /api/collection-points/**` | Authenticated (any role) |
| `GET /api/collection-points/{id}/books` | Authenticated — returns point + books list (`CollectionPointDetailDTO`) |
| `POST/PATCH/DELETE /api/collection-points/**` | `ROLE_ADMIN` only |
| `GET /api/book-instances` | Authenticated (any role) |
| `GET /api/book-instances/{id}` | Authenticated (any role) |
| `POST /api/book-instances/point/{pointId}` | `ROLE_ROOT_ADMIN` only |
| `POST /api/book-instances/my-point` | `ROLE_ADMIN` only |
| `PATCH/DELETE /api/book-instances/{id}` | `ROLE_ADMIN` only (ownership check in service) |

## Critical Conventions

**No Lombok, no Records.** All entities, DTOs, getters, setters, and mappers must be written explicitly.

**Logical deletion only.** Never use physical DELETE. Mark records as deleted by updating the status field to the `APAGADO` (0) enum value via a `@Modifying @Query`. All `findAll`/`findById` queries must filter out `APAGADO` records.

**Enum persistence.** Enums are persisted as integers using a JPA `AttributeConverter` (see `enums/converter/`). Never use `@Enumerated(EnumType.STRING)` or `@Enumerated(EnumType.ORDINAL)`.

**Partial updates.** Service update methods check each field individually with `isBlank()`/null checks, updating only fields present in the request DTO.

**Password encoding.** All passwords are encoded with BCrypt via `PasswordEncoder`. Never store plain-text passwords. Encoding happens in `UserService`, not in the mapper.

**Role assignment.** New users created via `POST /api/users` receive `ROLE_USER` automatically. Promotion to `ROLE_ADMIN` is done via `POST /api/admin/users/{id}/promote` (`ROLE_ROOT_ADMIN` only). The root admin (seeded by `AdminDataSeeder`) holds `ROLE_ROOT_ADMIN` + `ROLE_ADMIN`. The `DaoAuthenticationProvider` constructor in Spring Security 7.x (Spring Boot 4.x) requires `UserDetailsService` as a constructor argument — `setUserDetailsService()` was removed.

**Error messages in Portuguese.** Domain-level exception messages should be in Portuguese.

**Repository derived query naming.** Spring Data property traversal through associations uses camelCase, not underscore notation. Use `existsByUserAdminIdAndStatusNot` (not `existsByUserAdmin_IdAndStatusNot`) for traversing the `userAdmin.id` path.

## Business Rules

### CollectionPoints

**Creation is restricted to the root admin.** The root admin is identified by having `ROLE_ROOT_ADMIN` in their roles set. Promoted admins have only `ROLE_USER` + `ROLE_ADMIN` and cannot create collection points. This check is done in `CollectionPointService.create()` via `SecurityContextHolder`.

**The assigned admin must have `ROLE_ADMIN`.** When assigning or changing the responsible user (`userAdminId`), the service validates that the target user has `ROLE_ADMIN` in their roles set.

**One collection point per admin.** A user can be responsible for at most one non-deleted collection point. Enforced via `existsByUserAdminIdAndStatusNot` on create, and `existsByUserAdminIdAndStatusNotAndIdNot` on update (excluding the current point from the uniqueness check).

## Database Tables (defined in `database/init/circuler-scriptbd.sql`)

`users`, `roles`, `role_users`, `books`, `book_instances`, `collection_points`, `reservations`

**Implemented:**
- `users` — full CRUD + logical delete; statuses: `APAGADO(0)`, `ATIVO(1)`, `INATIVO(2)`
- `roles` and `role_users` — read-only via `RoleRepository`; seeded with `ROLE_ADMIN`, `ROLE_USER`, and `ROLE_ROOT_ADMIN`
- `books` — full CRUD + logical delete (ADMIN write, authenticated read); statuses: `APAGADO(0)`, `ATIVO(1)`; categories: `INFANTIL_JUVENIL(1)`, `AUTOAJUDA(2)`, `DIDATICO(3)`, `ESCOLAR(4)`. Note: `status INT NOT NULL DEFAULT 1` was added to the SQL init script.
- `collection_points` — full CRUD + logical delete (root ADMIN create, ADMIN write, authenticated read); statuses: `APAGADO(0)`, `ATIVO(1)`, `LOTADO(2)`, `INATIVO(3)`; @ManyToOne with `users` (EAGER); ResponseDTO exposes `userAdminId`, `userAdminName`, `userAdminEmail`

- `book_instances` — full CRUD + logical delete; statuses: `APAGADO(0)`, `DISPONIVEL(1)`, `RESERVADO(2)`, `RETIRADO(3)`; @ManyToOne to Book (EAGER), CollectionPoint (EAGER) and User/donor (EAGER, nullable); two create endpoints: `POST /point/{pointId}` (ROOT_ADMIN, any point) and `POST /my-point` (ADMIN, auto-resolves their point via JWT email); ownership check in PATCH/DELETE (ROOT_ADMIN bypasses, ADMIN restricted to their point); `GET /api/collection-points/{id}/books` returns `CollectionPointDetailDTO` (point fields + `List<CollectionPointBookDTO>` with id, bookId, bookTitle, bookAuthor, bookCategory, status, userDonorId, userDonorName)

**Not yet implemented:** `reservations` — table exists in the schema but has no entity/service/repository yet.
