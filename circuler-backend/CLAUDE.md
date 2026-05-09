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
| `GET /api/users/**` | Authenticated (any role) |
| `PATCH /api/users/{id}` | Authenticated (any role) |
| `DELETE /api/users/{id}` | `ROLE_ROOT_ADMIN` only |
| `POST /api/admin/**` | `ROLE_ROOT_ADMIN` only |
| `GET /api/books/**` | Authenticated (any role) |
| `POST /api/books` | `ROLE_ADMIN` only |
| `PATCH /api/books/{id}` | `ROLE_ADMIN` only |
| `PATCH /api/books/{id}/approve` | `ROLE_ROOT_ADMIN` only |
| `DELETE /api/books/{id}` | `ROLE_ROOT_ADMIN` only |
| `GET /api/collection-points/**` | Authenticated (any role) |
| `GET /api/collection-points/{id}/books` | Authenticated — returns point + books list (`CollectionPointDetailDTO`) |
| `POST /api/collection-points` | `ROLE_ROOT_ADMIN` only |
| `PATCH /api/collection-points/{id}` | `ROLE_ADMIN` only (ownership check in service) |
| `DELETE /api/collection-points/{id}` | `ROLE_ROOT_ADMIN` only |
| `GET /api/book-instances` | Authenticated (any role) |
| `GET /api/book-instances/{id}` | Authenticated (any role) |
| `GET /api/book-instances/pending` | `ROLE_ROOT_ADMIN` only |
| `GET /api/book-instances/my-point/pending` | `ROLE_ADMIN` only |
| `POST /api/book-instances/point/{pointId}` | `ROLE_ROOT_ADMIN` only |
| `POST /api/book-instances/my-point` | `ROLE_ADMIN` only |
| `POST /api/book-instances/my-point/new-book` | `ROLE_ADMIN` only |
| `PATCH /api/book-instances/{id}` | `ROLE_ADMIN` only (ownership check in service) |
| `DELETE /api/book-instances/{id}` | `ROLE_ADMIN` only (ownership check in service) |

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

### User & Admin Hierarchy

There are three roles forming a strict hierarchy: `ROLE_USER` < `ROLE_ADMIN` < `ROLE_ROOT_ADMIN`.

- **ROLE_USER**: Default for every new registration. Can read data and update own profile.
- **ROLE_ADMIN**: Promoted by `ROLE_ROOT_ADMIN` via `POST /api/admin/users/{id}/promote`. Manages one collection point (its books and instances).
- **ROLE_ROOT_ADMIN**: Seeded once by `AdminDataSeeder`. Manages the entire system — creates collection points, approves books, promotes admins.

A promoted admin always retains `ROLE_USER` in addition to `ROLE_ADMIN`. The root admin holds `ROLE_ROOT_ADMIN` + `ROLE_ADMIN`. Once promoted, a user **cannot be demoted** (no demotion endpoint exists).

**Dependency chain:**
1. `POST /api/users` → creates user with `ROLE_USER`
2. `POST /api/admin/users/{id}/promote` (ROOT_ADMIN) → adds `ROLE_ADMIN` to that user
3. `POST /api/collection-points` (ROOT_ADMIN) → assigns that promoted admin as responsible (`userAdminId`)

The order matters: a user must be promoted **before** being assigned to a collection point; the collection point must exist **before** the admin can manage book instances in it.

### CollectionPoints

**Creation is restricted to the root admin.** The root admin is identified by having `ROLE_ROOT_ADMIN` in their roles set. Promoted admins have only `ROLE_USER` + `ROLE_ADMIN` and cannot create collection points. This check is done in `CollectionPointService.create()` via `SecurityContextHolder`.

**The assigned admin must have `ROLE_ADMIN`.** When assigning or changing the responsible user (`userAdminId`), the service validates that the target user has `ROLE_ADMIN` in their roles set.

**One collection point per admin.** A user can be responsible for at most one non-deleted collection point. Enforced via `existsByUserAdminIdAndStatusNot` on create, and `existsByUserAdminIdAndStatusNotAndIdNot` on update (excluding the current point from the uniqueness check).

**Status transitions by role:**
- `ROLE_ADMIN` (the responsible admin): Can set status to `ATIVO` or `LOTADO` only.
- `ROLE_ROOT_ADMIN`: Can set any status including `INATIVO`.
- Neither role can set `APAGADO` via `PATCH`; use `DELETE` instead.

**Capacity management:** `CollectionPoint.capacityLimit` limits how many `BookInstance` records (with status != `APAGADO`) can reference that point simultaneously. The capacity check is enforced in `BookInstanceService` on every create, not in `CollectionPointService`.

### Books

Books are created by `ROLE_ADMIN` via **two paths**:

**Path A — Admin creates book directly:**
```
POST /api/books  →  Book.status = ATIVO  (immediately visible in catalog)
```
Then instance can be registered via `POST /api/book-instances/my-point` using the returned `bookId`.

**Path B — Admin donates a new (unregistered) book along with an instance:**
```
POST /api/book-instances/my-point/new-book
    → Book.status = PENDENTE
    → BookInstance.status = PENDENTE
    (not visible in catalog, not available for use)

PATCH /api/books/{id}/approve  (ROOT_ADMIN only)
    → Book.status = PENDENTE → ATIVO
    → All BookInstances of that book with status PENDENTE → DISPONIVEL
    (atomically: book appears in catalog AND instances become available)
```

`GET /api/books` returns only `ATIVO` books. `GET /api/books/pending` (ADMIN+) returns only `PENDENTE` books.

### BookInstances

A `BookInstance` represents a physical copy of a `Book` at a specific `CollectionPoint`. It optionally records a donor (`userDonor`).

**Dependencies for creation:**
1. `Book` must exist and status != `APAGADO`
2. `CollectionPoint` must exist and status != `APAGADO`
3. `CollectionPoint` must have available capacity (`activeInstances < capacityLimit`)
4. `userDonorId` (optional) — if provided, user must exist and be active

**Two create endpoints and who can use them:**

| Endpoint | Role | Point resolved from |
|---|---|---|
| `POST /api/book-instances/point/{pointId}` | `ROLE_ROOT_ADMIN` | Path variable — any point |
| `POST /api/book-instances/my-point` | `ROLE_ADMIN` | JWT email → find admin's point |
| `POST /api/book-instances/my-point/new-book` | `ROLE_ADMIN` | JWT email → find admin's point |

For `my-point` variants, the service extracts the authenticated user's email from `SecurityContextHolder`, finds the user, then finds their assigned `CollectionPoint` (status `ATIVO`). If the admin has no active point, the request fails.

**Ownership check on PATCH/DELETE:**
- `ROLE_ROOT_ADMIN`: allowed on any instance
- `ROLE_ADMIN`: only allowed if `instance.collectionPoint.userAdmin.email == currentUserEmail`
- All others: `403 Forbidden`

**Status flow:**
```
PENDENTE(4) → DISPONIVEL(1)   (triggered by book approval)
DISPONIVEL(1) → RESERVADO(2)  (reservation created — not yet implemented)
RESERVADO(2) → RETIRADO(3)    (user picked up the book)
Any → APAGADO(0)              (logical delete via DELETE endpoint)
```
PATCH on a BookInstance only accepts a `status` field update (no other fields). The transition PENDENTE → DISPONIVEL is performed automatically by `BookService.approve()`, not by the PATCH endpoint.

### Reservations (not yet implemented)

The `reservations` table exists in the database schema but has no entity, repository, service, or controller. When implemented it will connect `User` ↔ `BookInstance` and drive the `DISPONIVEL → RESERVADO → RETIRADO` transitions.

## Entity Relationships

```
User ──────────────────────────── Role  (ManyToMany via role_users)
 │
 ├─── CollectionPoint.userAdmin    (ManyToOne, NOT NULL — one admin per point)
 │
 └─── BookInstance.userDonor       (ManyToOne, NULLABLE — optional donor)

Book ──── BookInstance.book        (ManyToOne, NOT NULL)

CollectionPoint ──── BookInstance.collectionPoint  (ManyToOne, NOT NULL)
```

Key constraints:
- Deleting a `User` who is `CollectionPoint.userAdmin` is **not safe** — the FK is `NOT NULL`. The admin must be reassigned first via `PATCH /api/collection-points/{id}`.
- Deleting a `User` who is `BookInstance.userDonor` leaves `userDonor = null` (nullable FK, soft-delete only marks user as `APAGADO`).
- Deleting a `CollectionPoint` leaves its `BookInstance` records orphaned (they still reference the deleted point). No cascade delete exists.
- Deleting a `Book` does **not** cascade to its `BookInstance` records.

## Status Enums

| Entity | APAGADO | ATIVO | Other values |
|---|---|---|---|
| User | 0 | 1 | INATIVO(2) |
| Book | 0 | 1 | PENDENTE(3) |
| BookInstance | 0 | DISPONIVEL(1) | RESERVADO(2), RETIRADO(3), PENDENTE(4) |
| CollectionPoint | 0 | 1 | LOTADO(2), INATIVO(3) |

All queries that return "active" records filter `status != APAGADO`. There are dedicated `findAll()` methods (no filter) used by internal services only.

## Database Tables (defined in `database/init/circuler-scriptbd.sql`)

`users`, `roles`, `role_users`, `books`, `book_instances`, `collection_points`, `reservations`

**Implemented:**
- `users` — full CRUD + logical delete; statuses: `APAGADO(0)`, `ATIVO(1)`, `INATIVO(2)`
- `roles` and `role_users` — read-only via `RoleRepository`; seeded with `ROLE_ADMIN`, `ROLE_USER`, and `ROLE_ROOT_ADMIN`
- `books` — full CRUD + logical delete; statuses: `APAGADO(0)`, `ATIVO(1)`, `PENDENTE(3)`; categories: `INFANTIL_JUVENIL(1)`, `AUTOAJUDA(2)`, `DIDATICO(3)`, `ESCOLAR(4)`
- `collection_points` — full CRUD + logical delete; statuses: `APAGADO(0)`, `ATIVO(1)`, `LOTADO(2)`, `INATIVO(3)`; `@ManyToOne` with `users` (EAGER); ResponseDTO exposes `userAdminId`, `userAdminName`, `userAdminEmail`
- `book_instances` — full CRUD + logical delete; statuses: `APAGADO(0)`, `DISPONIVEL(1)`, `RESERVADO(2)`, `RETIRADO(3)`, `PENDENTE(4)`; `@ManyToOne` to Book (EAGER), CollectionPoint (EAGER) and User/donor (EAGER, nullable); two create endpoints; ownership check on PATCH/DELETE; `GET /api/collection-points/{id}/books` returns `CollectionPointDetailDTO` (point fields + `List<CollectionPointBookDTO>` with id, bookId, bookTitle, bookAuthor, bookCategory, status, userDonorId, userDonorName)

**Not yet implemented:** `reservations` — table exists in the schema but has no entity/service/repository yet.
