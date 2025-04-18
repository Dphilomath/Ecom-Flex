# Deployment Guide for E-Commerce Backend

This guide provides instructions for deploying the Spring Boot e-commerce application using Docker.

## Prerequisites

- Docker and Docker Compose installed
- Git (to clone the repository)

## Local Deployment with Docker

1. Clone the repository:
   ```bash
   git clone https://github.com/Dphilomath/Ecom-Flex.git
   cd Ecom-Flex
   ```

2. Build and run with Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. Access the application:
   - Main application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - H2 Database console: http://localhost:8080/h2-console
   - GraphQL playground: http://localhost:8080/graphiql
   - Virtual Threads demo: http://localhost:8080/api/demo/virtual-threads/info

## Cloud Deployment Options

### Deploy to Render

1. Sign up for a [Render account](https://render.com/)
2. Create a new Web Service
3. Connect your GitHub repository
4. Configure as follows:
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar -XX:+UseParallelGC -Djava.awt.headless=true target/*.jar`
   - Environment Variables:
     - Add `SPRING_PROFILES_ACTIVE=docker`

### Deploy to Railway

1. Sign up for a [Railway account](https://railway.app/)
2. Create a new project
3. Connect your GitHub repository
4. Set the following environment variables:
   - `SPRING_PROFILES_ACTIVE=docker`

## Notes About H2 In-Memory Database

- The application uses an H2 in-memory database which is reset on application restart
- For development/demo purposes only
- For production, consider using a persistent database service

## Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set to `docker` for containerized environments
- `JAVA_OPTS`: JVM options (already configured in Dockerfile)

## Monitoring

- Health endpoint: `/actuator/health`
- Info endpoint: `/actuator/info`

## Virtual Threads Testing

The application uses Java 21 virtual threads. Test them with:

- `/api/demo/virtual-threads/info` - View information about the current thread
- `/api/demo/virtual-threads/simulate?tasks=1000&delayMs=100` - Simulate concurrent tasks 