# Employee Task Manager - Copilot Instructions

## Project
Build a RESTful Employee Task Management application using Java 25 and Spring Boot.

## Architecture
Follow a clean layered architecture:

Controller → Service → Repository → Database

Use DTOs between the API layer and persistence layer.

## Technology
- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Jakarta Bean Validation
- JUnit 5
- Mockito
- Maven

## Coding Standards
- Use constructor injection.
- Follow SOLID principles.
- Keep controllers thin.
- Keep business logic in services.
- Keep database operations in repositories.
- Use meaningful class, method, and variable names.
- Do not add unnecessary dependencies.
- Do not introduce Lombok unless explicitly required.
- Prefer clear, maintainable code over unnecessary complexity.

## API
Create RESTful APIs with appropriate HTTP methods and status codes.

Use:
- GET for retrieval
- POST for creation
- PUT/PATCH for updates
- DELETE for deletion

Use DTOs for request and response payloads.

## Validation
Use Jakarta Bean Validation annotations where appropriate.

Invalid requests should return clear validation errors.

## Error Handling
Implement centralized exception handling using `@RestControllerAdvice`.

Return appropriate HTTP status codes and meaningful error responses.

## Persistence
Use Spring Data JPA.

Keep entity classes focused on persistence concerns.

Use appropriate relationships between entities and avoid unnecessary bidirectional relationships.

## Testing
Write unit tests for service-layer business logic using JUnit 5 and Mockito.

Add controller/integration tests where appropriate.

Tests should cover:
- Successful operations
- Validation failures
- Not-found scenarios
- Business-rule failures

## Database
Use H2 for the initial implementation.

Keep database configuration in `application.properties`.

Do not hard-code database credentials or environment-specific values in Java code.

## Copilot Agent Rules
Before making changes:
1. Inspect the existing project structure.
2. Inspect `pom.xml` and existing source files.
3. Reuse existing dependencies where possible.
4. Do not overwrite working code unnecessarily.
5. Implement one logical feature at a time.
6. Explain significant architectural decisions.
7. After implementation, run the relevant tests.
8. Fix compilation or test failures before considering the task complete.

Do not create unnecessary files, classes, dependencies, or abstractions.