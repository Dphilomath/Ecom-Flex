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
src/main/java/com/dm/ecommerce/
├── config/               # Configuration classes
│   ├── AuthMode.java     # Authentication mode enum (JWT/Session)
│   ├── DataInitializer.java # Sample data initialization
│   ├── OpenApiConfig.java # Swagger/OpenAPI configuration
│   ├── SecurityConfig.java # Security configuration
│   └── WebConfig.java    # Web-related configurations
├── controller/           # REST controllers
├── graphql/              # GraphQL resolvers
├── model/                # Entity classes
├── payload/              # Request/Response DTOs
├── repository/           # JPA repositories
├── security/             # Security-related classes
│   └── jwt/              # JWT authentication components
├── service/              # Business logic services
│   └── impl/             # Service implementations
└── EcommerceApplication.java # Main application class
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

You can configure the authentication mode in `application.properties`:

```properties
app.auth.mode=JWT  # or SESSION
```

## Configuration Options

Key configuration options in `application.properties`:

```properties
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:h2:mem:ecommercedb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT configuration
app.jwt.secret=yourSecretKey
app.jwt.expiration=86400000  # 24 hours

# Authentication mode (JWT or SESSION)
app.auth.mode=JWT
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

To activate a profile, set the `SPRING_PROFILES_ACTIVE` environment variable:

```
# For local development
export SPRING_PROFILES_ACTIVE=local

# For production
export SPRING_PROFILES_ACTIVE=prod
```

## Deployment

### Local Development

For local development, you can use the H2 in-memory database:

```
mvn spring-boot:run
```

### Docker Deployment

Build and run the Docker container:

```
docker build -t ecommerce-backend .
docker run -p 8080:8080 ecommerce-backend
```

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