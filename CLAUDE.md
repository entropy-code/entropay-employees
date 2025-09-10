# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Run
```bash
# Build the application
./mvnw clean package

# Run the application locally
./mvnw spring-boot:run

# Run with Docker Compose (includes PostgreSQL and pgweb)
docker-compose up --build

# Run only database services
docker-compose up postgres pgweb
```

### Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ClassName

# Run specific test method
./mvnw test -Dtest=ClassName#methodName
```

### Database
- Access pgweb (database UI) at http://localhost:8081
- Application runs on http://localhost:8100
- Database migrations are managed by Flyway in `src/main/resources/db/migration/`
- Migration naming: `V{version}__{description}.sql`

## Architecture Overview

This is a Spring Boot 3.5+ application using Java 21 with a layered architecture following these core patterns:

### Base Architecture Pattern
The application uses a generic CRUD pattern with these base classes:

1. **BaseRepository<T, K>** extends JpaRepository with soft delete support
   - `findAllByDeletedIsFalse()` method for active records
   
2. **CrudService<DTO, Key>** interface with standard CRUD operations
   - `findOne(Key id)`, `findAllActive(ReactAdminParams)`, `create(DTO)`, `update(Key, DTO)`, `delete(Key)`

3. **BaseController<T, K>** provides standard REST endpoints
   - GET `/` (list with pagination)
   - GET `/{id}` (single entity)
   - POST `/` (create)
   - PUT `/{id}` (update) 
   - DELETE `/{id}` (soft delete)
   - All secured with role-based access control

### Domain Structure
- **employees/** - Core employee management functionality
  - **controllers/** - REST API endpoints
  - **services/** - Business logic layer
  - **repositories/** - Data access layer
  - **models/** - JPA entities (extend BaseEntity with soft delete)
  - **dtos/** - Data transfer objects for API
  - **calendar/** - Google Calendar integration
  - **timetracking/** - Time tracking functionality
- **auth/** - Authentication and authorization utilities
- **common/** - Shared base classes and utilities
- **notifications/** - External notification services

### Key Technologies
- **Spring Boot 3.5+** with Web, Data JPA, Security, OAuth2 Resource Server
- **Java 21** with virtual threads support
- **PostgreSQL** with Flyway migrations
- **AWS S3** for file storage
- **Google Calendar API** for calendar integration
- **Maven** for dependency management
- **Docker** for containerization

### Soft Delete Pattern
All entities use soft delete (set `deleted=true`) rather than physical deletion. Always filter out deleted records in queries unless explicitly retrieving historical data.

### React Admin Integration
The API is designed for React Admin frontend:
- List endpoints support filtering, sorting, and pagination via ReactAdminParams
- Returns `X-Total-Count` header for pagination
- DTOs should contain only fields needed by the frontend

### Security Model
- OAuth2 resource server with JWT tokens
- Role-based access control with roles: ADMIN, MANAGER_HR, ANALYST, DEVELOPMENT, HR_DIRECTOR
- Environment variables for sensitive configuration (see `local.env`)

### Adding New Features
When creating a new domain entity:
1. Create Entity extending BaseEntity
2. Create Repository extending BaseRepository<Entity, ID>
3. Create DTO record/class
4. Create Service implementing CrudService with entity/DTO conversion
5. Create Controller extending BaseController
6. Add Flyway migration with proper naming
7. Add unit tests for service and controller layers
8. Update this documentation

### Testing Conventions
- Use JUnit 5 and Mockito for unit tests
- Test classes annotated with `@ExtendWith(MockitoExtension.class)`
- Follow Given-When-Then pattern in test methods
- Mock external dependencies (AWS S3, Google Calendar, etc.)

### Code Conventions
- Constructor injection preferred over field injection
- Keep controllers thin - only validation and delegation
- Business logic belongs in service layer
- Use Optional only at repository boundaries, not in DTOs
- Prefer records for immutable DTOs
- Use Criteria API over native SQL where possible