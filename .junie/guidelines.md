# Entroteam Employees Project Guidelines

## Project Overview
Entroteam Employees is a Spring Boot application that manages employee data, integrates with external services, and provides reporting capabilities.

## Tech Stack
- **Java 21** with virtual threads support
- **Spring Boot 3.4.5** (Web, Data JPA, Security, OAuth2)
- **PostgreSQL** database
- **Flyway** for database migrations
- **Maven** for build management
- **Docker** for containerization
- **JUnit 5** and **Mockito** for testing
- **AWS S3** for file storage
- **Google Calendar API** for calendar integration

## Project Structure
```
entropay-employees/
├── src/
│   ├── main/
│   │   ├── java/com/entropyteam/entropay/
│   │   │   ├── auth/             # Authentication components
│   │   │   ├── common/           # Shared utilities and exceptions
│   │   │   ├── config/           # Application configuration
│   │   │   ├── employees/        # Core employee functionality
│   │   │   │   ├── calendar/     # Calendar integration
│   │   │   │   ├── controllers/  # REST endpoints
│   │   │   │   ├── dtos/         # Data transfer objects
│   │   │   │   ├── jobs/         # Scheduled jobs
│   │   │   │   ├── models/       # Entity models
│   │   │   │   ├── repositories/ # Data access
│   │   │   │   ├── services/     # Business logic
│   │   │   │   └── timetracking/ # Time tracking specific logic    
│   │   │   └── notifications/    # Notification services
│   │   └── resources/
│   │       ├── application.properties # Application configuration
│   │       └── db/migration/   # Flyway database migrations
│   └── test/                   # Test classes
├── Dockerfile                  # Docker build configuration
├── docker-compose.yml         # Local development environment
├── pom.xml                    # Maven dependencies and build config
└── local.env                  # Local environment variables
```

## Development Workflow

### Setting Up Local Environment
1. Clone the repository
2. Run `docker-compose up postgres pgweb` to start the database
3. Run the application using Maven: `./mvnw spring-boot:run`
4. Access the application at http://localhost:8100
5. Access pgweb (database UI) at http://localhost:8081

### Running Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=SlackNotifierTest

# Run specific test method
./mvnw test -Dtest=SlackNotifierTest#shouldSendMessageToSlack
```

### Building the Application
```bash
# Build JAR file
./mvnw clean package

# Build Docker image
docker build -t entropay-employees .
```

### Running with Docker Compose
To run the entire application stack including the database and pgweb:

```bash
docker-compose up --build
```

To run only the database and pgweb:

```bash
docker-compose up postgres pgweb
```

### Database Migrations
- Migrations are managed with Flyway
- Migration files are located in `src/main/resources/db/migration/`
- Naming convention: `V{version}__{description}.sql`

## Best Practices
1. **Code Organization**
   - Follow the package structure for new components
   - Keep controllers thin, business logic in services
   - Use DTOs for API requests/responses 

2. **Testing**
   - Write unit tests for all new functionality
   - Follow the Given-When-Then pattern
   - Use Mockito for mocking dependencies
   - Annotate the classes with `@ExtendWith(MockitoExtension.class)`

3. **Database**
   - Create Flyway migrations for schema changes
   - Use JPA repositories for data access
   - Avoid N+1 query problems with proper fetch strategies
   - Use HQL in preference of SQL

4. **Security**
   - Don't hardcode credentials in source code
   - Use environment variables for sensitive information
   - Follow OAuth2 security patterns

5. **API Design**
   - Use RESTful principles
   - Document APIs with clear request/response examples
   - Implement proper error handling and validation

## Deployment
The application is containerized using Docker and can be deployed to any container orchestration platform.

## Configuration
The application can be configured using environment variables or by modifying the `application.properties` file. Key configuration properties include:

- `server.port`: The port on which the application runs (default: 8100)
- `spring.datasource.url`: The JDBC URL for the database connection
- `spring.datasource.username`: The database username
- `spring.datasource.password`: The database password
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`: The JWT issuer URI for OAuth2 authentication

For local development, these properties are set in the `local.env` file and the `docker-compose.yml` file.

## Documentation Guidelines
When making meaningful changes to the project, please ensure that both this file (.junie/guidelines.md) and the README.md file are kept in sync and up-to-date. This includes:

1. Adding new sections or information about new features
2. Updating existing sections to reflect changes in the codebase
3. Removing outdated information
4. Ensuring that technical details (versions, configurations, etc.) are accurate

This helps maintain consistent documentation and ensures that all team members have access to the most current information about the project.
