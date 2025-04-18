FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and src directory
COPY pom.xml .
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Second stage: Setup runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set JVM options optimized for containers with virtual threads
ENV JAVA_OPTS="-XX:+UseParallelGC -XX:MaxRAMPercentage=75.0 -Djava.awt.headless=true"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 