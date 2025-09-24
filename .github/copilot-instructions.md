# GitHub Copilot Repository Instructions

Purpose: Guide AI code suggestions so they align with this project's architecture, conventions, and quality bar.

## Project Snapshot
- Name: Entroteam Employees (Spring Boot employee management & reporting)
- Java 21 (virtual threads enabled mindset)
- Spring Boot 3.4.x (Web, Data JPA, Security, OAuth2 Resource Server)
- PostgreSQL + Flyway migrations (src/main/resources/db/migration)
- Testing: JUnit 5 + Mockito
- Integrations: AWS S3 (file storage), Google Calendar API, Slack notifications
- Containerization: Docker / docker-compose

## Core Architectural Patterns
1. Generic CRUD stack with soft delete:
   - BaseEntity: common fields incl. id, deleted, timestamps.
   - BaseRepository<T, ID>: extends JpaRepository with findAllByDeletedIsFalse().
   - BaseService<Entity, Dto, ID>: generic CRUD, dynamic filtering (Criteria API), pagination, sorting, soft delete (sets deleted=true).
   - BaseController<Dto, ID>: standard REST endpoints (GET list, GET by id, POST, PUT, DELETE) delegating to service; role-based security.
2. DTO-centric API: Controllers expose DTOs only (never entities). Services convert between Entity ↔ DTO.
3. Soft Deletion: Never physically delete unless explicitly required; use deleted flag.
4. React-Admin Friendly: List endpoints accept filtering, sorting, pagination parameters (keep stable behavior when modifying).

## When Generating New Feature (Entity)
Provide exactly these layers:
1. Entity extends BaseEntity (add JPA annotations, relationships, validation constraints where appropriate).
2. Repository extends BaseRepository<Entity, ID> (no custom code unless necessary).
3. DTO record/class containing only what API needs. Avoid leaking internal/security fields.
4. Service extends BaseService<Entity, Dto, ID>; implement:
   - getRepository()
   - toDTO(Entity)
   - toEntity(Dto)
   - merge(Entity existing, Dto dto) if partial logic is needed.
5. Controller extends BaseController<Dto, ID>; annotate with @RequestMapping and relevant @PreAuthorize rules.
6. Add Flyway migration for schema changes (naming: V{next}__snake_case_description.sql).
7. Add unit tests (service + controller) using Mockito for collaborators.
8. Update documentation: .junie/guidelines.md and README.md (keep them in sync).

## Coding Conventions & Guidance for Suggestions
- Keep controllers thin: validation + delegation only.
- Put business rules in services.
- Prefer constructor injection; avoid field injection.
- Avoid static util if a Spring bean fits better.
- Use Optional only at API boundaries from repository; not in DTOs.
- Favor records for immutable DTOs where suitable.
- For pageable queries, reuse existing filtering infrastructure (ReactAdmin params) instead of crafting ad-hoc queries.
- Prefer Criteria API / specifications style rather than hardcoded SQL; only fall back to native queries if unavoidable.
- When mapping entities, never trigger N+1 inadvertently; consider fetch joins or @EntityGraph where justified.
- Enforce soft delete filters (exclude deleted=true by default).
- Any new external call should be wrapped in a dedicated service with clear interface.

## Testing Expectations
- Each new service method: at least one happy-path + one edge/negative test.
- Use Given/When/Then naming inside test method body comments.
- Mock external systems (S3, Calendar, Slack) — do not perform network calls.
- For date/time logic, inject Clock where needed to make deterministic.
- Keep test data lightweight; prefer builder/helpers over large SQL fixtures unless necessary.

## Security & Config
- Do not hardcode secrets or tokens; use environment variables (see local.env) or Spring config properties with placeholders.
- Maintain OAuth2 resource server configuration; new endpoints should require appropriate roles unless explicitly public.
- Validate inputs; return meaningful error messages (avoid leaking stack traces).

## Flyway & Database
- One migration per logical change; never edit applied migrations.
- Use proper column types (e.g., NUMERIC for money if precision needed); avoid vendor-specific features unless justified.
- Add indexes for new foreign keys or frequently filtered columns.

## Performance / Virtual Threads
- Avoid blocking I/O inside synchronized blocks.
- For long external calls, consider timeouts and graceful fallbacks.
- Batch repository operations where possible instead of per-row loops.

## Logging & Errors
- Use structured, minimal logging; avoid logging sensitive data (PII, secrets).
- Throw domain-specific exceptions mapping to meaningful HTTP status codes.

## Documentation Sync Rule
Whenever code changes behavior or adds a feature:
- Update .junie/guidelines.md AND README.md.
- If adding architectural nuance, also update this copilot-instructions file if it affects generation heuristics.

## What Copilot Should Avoid
- Generating raw SQL for CRUD where repository/service already handles it.
- Bypassing BaseService / BaseController patterns.
- Exposing entity internals directly in controllers.
- Adding libraries without updating pom.xml.
- Creating migrations with duplicate version numbers.
- Hardcoding region/bucket names, credentials, or secrets.

## Example Outline For New Domain Object (Summary)
Entity -> Repository -> DTO -> Service (override conversions) -> Controller -> Migration -> Tests -> Docs.

## Quick Reference Snippets (Descriptive, not executable)
Service overrides:
// ...existing code...
@Override
protected BaseRepository<MyEntity, UUID> getRepository() { return repository; }
@Override
protected MyDto toDTO(MyEntity e) { /* map */ }
@Override
protected MyEntity toEntity(MyDto d) { /* map */ }
// ...existing code...

Soft delete awareness: queries must exclude deleted=true unless explicitly retrieving historical data.

---
Maintain this file as authoritative for AI assistance. Keep concise and high-signal; prune outdated guidance rapidly.

