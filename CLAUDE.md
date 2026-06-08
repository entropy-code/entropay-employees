# CLAUDE.md — entropay-employees

Spring Boot backend that owns the core domain model for the Entroteam platform — employees, projects, contracts, time off, reports, and the integrations that surround them. This file covers everything specific to this repo. For cross-app context (auth flow, RBAC, full-stack module workflow), see the **`entroteam` meta-repo**.

---

## Development commands

### Build and run
```bash
./mvnw clean package           # Build
./mvnw spring-boot:run         # Run locally on port 8100
docker-compose up --build      # Full stack (app + Postgres + pgweb)
docker-compose up postgres pgweb  # Database only
```

### Tests
```bash
./mvnw test                              # All
./mvnw test -Dtest=ClassName             # Single class
./mvnw test -Dtest=ClassName#methodName  # Single method
```

### Database
- App on `http://localhost:8100`, pgweb on `http://localhost:8081`.
- Migrations live in `src/main/resources/db/migration/` and are applied by Flyway on startup.
- Naming: `V{version}__{description}.sql`.

---

## Stack

- Spring Boot 3.5+ with Web, Data JPA, Security, OAuth2 Resource Server.
- Java 21 (virtual threads available).
- PostgreSQL with Flyway-managed schema.
- AWS S3 for file storage.
- Google Calendar API for calendar integration.
- Maven for builds, Docker for containerization.

---

## Generic CRUD pattern

Almost every entity flows through the same four base classes. Use them. Bypassing the pattern should be the exception.

1. **`BaseEntity`** — soft-delete-aware base for every JPA model. Sets the `deleted` flag and lifecycle metadata.
2. **`BaseRepository<T, K>`** — extends `JpaRepository`, exposes `findAllByDeletedIsFalse()` and other delete-aware queries. **Always use the active-records query for list endpoints**; only reach for raw `findAll` when you explicitly need historical/deleted rows.
3. **`CrudService<DTO, Key>`** — interface with `findOne(Key id)`, `findAllActive(ReactAdminParams)`, `create(DTO)`, `update(Key, DTO)`, `delete(Key)`. Concrete services map between entity and DTO.
4. **`BaseController<T, K>`** — REST surface: `GET /` (list with pagination), `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}` (soft delete). Secured with role-based access via Spring Security.

**Soft delete invariant**: list/get queries never return `deleted=true` rows unless the controller explicitly opts in. New code that touches the data layer must respect this — it is a load-bearing assumption of the frontend, the reports, and the data exports.

**React Admin contract**: list endpoints accept `ReactAdminParams` (filter / sort / range) and respond with an `X-Total-Count` header. Custom collection endpoints must follow the same contract or `entropay-admin-ui` cannot consume them.

---

## Domain package map

Source root: `src/main/java/com/entropyteam/entropay/`.

### `employees/` — the core domain
The bulk of the business lives here. Standard layered layout repeats per area:
- `controllers/` — REST endpoints, thin (validation + delegation).
- `services/` — business logic.
- `repositories/` — `BaseRepository` extensions; `repositories/projections/` holds JPA projections for reports.
- `models/` — JPA entities extending `BaseEntity`.
- `dtos/` — request/response shapes (records preferred).

Sub-packages with self-contained concerns:
- **`calendar/`** — Google Calendar integration (sync, event creation, OAuth credentials).
- **`jobs/`** — scheduled tasks / batch jobs.
- **`leakcheck/`** — checks for data leakage / inconsistencies.
- **`timetracking/`** — time tracking entities and services.

### `notifications/`
External notification dispatch. Includes `slack/` for Slack integration. Notification triggers usually live in `employees/services/` and call into here.

### `auth/`
Resource-server-side auth utilities — JWT validation, role extraction, security context helpers. The OAuth2 issuer is `entropay-users-auth` (see meta-repo). This package does **not** issue tokens; it only validates them.

### `common/`
Cross-cutting infrastructure:
- The `BaseEntity` / `BaseRepository` / `CrudService` / `BaseController` family.
- `exceptions/` — global `@ControllerAdvice` mapping with `dtos/` for error responses.
- `sensitiveInformation/` — utilities for handling fields that need extra care (PII, salaries).

### `config/`
Spring `@Configuration` classes. Beans for AWS S3, security, web, etc.

---

## Business rules cheat sheet

These are platform-wide invariants encoded across many services. Violating them tends to produce subtle bugs that surface in reports.

- **Soft delete is universal.** Every list/get hides `deleted=true` rows; aggregates and reports filter the same way. When designing a new query, default to active-only and opt in to history explicitly.
- **RBAC is enforced server-side.** The frontend hides things based on permissions, but every controller method must also be `@PreAuthorize`-gated. Never trust client-side hiding alone.
- **Roles** (consistent across the platform): `ADMIN`, `HR_DIRECTOR`, `MANAGER_HR`, `ANALYST`, `DEVELOPMENT`. New behaviors should map to existing roles when possible — adding a role is a cross-app change (see meta-repo).
- **Reports are read views.** They live behind `reports/...` endpoints, return projections (not entities), and don't mutate state. Heavy aggregates should use database-side projections from `repositories/projections/`, not in-memory streaming.
- **Calendar / time tracking integrations are eventually consistent.** Failures in those subsystems should not block the main CRUD path; surface them as notifications, not as request errors, unless the user action depended on them.
- **Salaries, billing, and margin** all derive from contracts + assignments + time data. Any change to those entities or to the soft-delete behavior of related rows must be cross-checked against `reports/salaries`, `reports/billing`, and `reports/margin`.

---

## Adding a new CRUD entity (recipe)

1. **Entity** — `models/Foo.java` extending `BaseEntity`. JPA annotations, no business logic.
2. **Repository** — `repositories/FooRepository.java` extending `BaseRepository<Foo, UUID>` (or whatever the key type is). Add custom finders only when needed.
3. **DTO** — `dtos/FooDto.java`. Prefer a `record`. Include only the fields the frontend (or other consumers) need.
4. **Service** — `services/FooService.java` implementing `CrudService<FooDto, UUID>`. Handle entity ↔ DTO conversion here. Constructor injection.
5. **Controller** — `controllers/FooController.java` extending `BaseController<FooDto, UUID>`. Override only when you need non-CRUD endpoints. Annotate with `@PreAuthorize` for role gating.
6. **Migration** — `src/main/resources/db/migration/V{n}__create_foo.sql`. Make sure the version number is the next free integer.
7. **Tests** — at minimum a service-level unit test (`@ExtendWith(MockitoExtension.class)`, given/when/then) covering create/update/delete and any non-trivial logic. Mock S3, Calendar, and Slack.
8. **Frontend** — register the resource in `entropay-admin-ui/src/resources.ts` (see that repo's `CLAUDE.md`). The full-stack workflow lives in the meta-repo.

---

## Code conventions

- **Constructor injection** over field injection. Make services testable.
- Controllers stay thin: parse, validate, delegate, return.
- Business logic belongs in the service layer.
- Use `Optional` only at repository boundaries, not in DTOs or controller signatures.
- Prefer **records** for immutable DTOs.
- Prefer **HQL** over native SQL when feasible; reach for native SQL only when the query genuinely needs it.

### Tests
- JUnit 5 + Mockito.
- `@ExtendWith(MockitoExtension.class)` on unit tests.
- Given-When-Then layout in test methods.
- Mock external dependencies (S3, Google Calendar, Slack, etc.).

---

## Security and configuration

- OAuth2 resource server. JWTs issued by `entropay-users-auth` (Cognito-backed). The issuer URL is environment-configured.
- Role mapping is read from the JWT and translated into Spring authorities.
- Sensitive configuration (DB credentials, AWS keys, Cognito issuer URL) lives in env vars / `local.env`. Never hardcode.

---

## Branch convention

- Default branch: `main`.
- PRs target `main`.
