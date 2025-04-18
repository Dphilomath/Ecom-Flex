# Spring E-Commerce Backend

A modern e-commerce backend built with Spring Boot featuring both REST and GraphQL APIs.

## Features

- Dual API support (REST and GraphQL)
- Role-based access control (admin and regular users)
- Flexible authentication (JWT or session-based)
- Product and category management
- User management
- Shopping cart functionality
- Order processing
- Product reviews and ratings
- OpenAPI/Swagger documentation
- Exception handling with proper HTTP status codes
- Data validation
- Docker support

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Docker (optional, for containerized deployment)

### Installation

1. Clone the repository
   ```
   git clone https://github.com/Dphilomath/Ecom-Flex.git
   cd Ecom-Flex
   ```

2. Build the project with Maven:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

### Access Points

- Main application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- GraphQL playground: http://localhost:8080/graphiql
- H2 Database console: http://localhost:8080/h2-console

## Project Structure

```
src/
├── main/
│   ├── java/com/dm/ecommerce/
│   │   ├── config/               # Configuration classes
│   │   │   ├── AuthMode.java     # Authentication mode enum (JWT/Session)
│   │   │   ├── DataInitializer.java # Sample data initialization
│   │   │   ├── OpenApiConfig.java # Swagger/OpenAPI configuration
│   │   │   ├── SecurityConfig.java # Security configuration
│   │   │   └── WebConfig.java    # Web-related configurations
│   │   ├── controller/           # REST controllers
│   │   ├── graphql/              # GraphQL resolvers
│   │   ├── model/                # Entity classes
│   │   ├── payload/              # Request/Response DTOs
│   │   ├── repository/           # JPA repositories
│   │   ├── security/             # Security-related classes
│   │   │   └── jwt/              # JWT authentication components
│   │   ├── service/              # Business logic services
│   │   │   └── impl/             # Service implementations
│   │   └── EcommerceApplication.java # Main application class
│   │
│   └── resources/
│       ├── application.yml       # Main application properties
│       ├── application-local.yaml # Local development properties
│       ├── application-prod.yaml # Production properties
│       ├── application-docker.yml # Docker-specific properties
│       ├── static/               # Static web resources
│       └── graphql/              # GraphQL schema definitions
```

## API Documentation

Comprehensive API documentation is available through Swagger UI at `/swagger-ui/index.html`. The OpenAPI specification describes all available endpoints, request parameters, and response models.

## Database

This project uses H2 in-memory database by default for development. You can access the H2 console at `/h2-console`.

For production, you can configure the application to use MySQL, PostgreSQL, or any other database supported by Spring Data JPA by updating the application properties.

## Security

The application supports both stateless (JWT) and stateful (session-based) authentication modes.

Default users:
- Admin: username: `admin`, password: `admin123`
- Regular user: username: `user`, password: `user123`

### Authentication Modes

You can configure the authentication mode in `application.yml`:

```yaml
jwt:
  secret: yourSecretKey
  expiration: 86400000  # 24 hours
```

## Configuration Options

Key configuration options in `application.yml`:

```yaml
spring:
  # Database Configuration
  datasource:
    url: jdbc:h2:mem:ecommercedb
    driverClassName: org.h2.Driver
    username: sa
    password: password
  
  # GraphQL Configuration
  graphql:
    graphiql:
      enabled: true
    
# JWT Configuration
jwt:
  secret: yourSecretKeyHere
  expiration: 86400000  # 24 hours

# Server Configuration
server:
  port: 8080
```

### Environment Variables

The following environment variables can be used to control application behavior in production:

| Variable | Description | Default |
|----------|-------------|---------|
| `ENABLE_SWAGGER` | Enable/disable Swagger UI and OpenAPI docs in production | `true` |
| `ENABLE_GRAPHIQL` | Enable/disable GraphiQL playground in production | `true` |
| `PORT` | Server port (for cloud deployments) | `8080` |
| `JDBC_DATABASE_URL` | Database connection URL | - |
| `JDBC_DATABASE_USERNAME` | Database username | - |
| `JDBC_DATABASE_PASSWORD` | Database password | - |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile (`local` or `prod`) | - |

### Profile-Specific Configuration

The application includes profile-specific configurations:

- `application-local.yaml`: Used for local development (Swagger UI and GraphiQL always enabled)
- `application-prod.yaml`: Used for production deployment (Swagger UI and GraphiQL controlled via environment variables)
- `application-docker.yml`: Used for Docker deployments

#### Activating Profiles

There are several ways to activate a specific profile:

1. **Using environment variables**:
   ```
   export SPRING_PROFILES_ACTIVE=local
   mvn spring-boot:run
   ```

2. **Using JVM system properties**:
   ```
   mvn spring-boot:run -Dspring.profiles.active=local
   ```

3. **In Docker Compose**:
   ```yaml
   environment:
     - SPRING_PROFILES_ACTIVE=docker
   ```

4. **In application.yml** (default profile that will be used if none is specified):
   ```yaml
   spring:
     profiles:
       active: local
   ```

## Deployment

### Local Development

For local development with the H2 in-memory database:

```
# Option 1: Using system property
mvn spring-boot:run -Dspring.profiles.active=local

# Option 2: Using environment variable
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

### Docker Deployment

The application exposes port 8080 in the Dockerfile.

Build and run the Docker container:

```
docker build -t ecommerce-backend .
docker run -p 8080:8080 ecommerce-backend
```

You can map to a different host port if needed:

```
docker run -p 9000:8080 ecommerce-backend
```

This will make the application available at http://localhost:9000 while it still runs on port 8080 inside the container.

### Docker Compose

You can also use Docker Compose to run the application with a PostgreSQL database:

```
docker-compose up
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 