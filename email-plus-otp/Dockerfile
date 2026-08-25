# Dependencies / Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy Maven configuration first for better Docker layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot application
RUN mvn clean package -DskipTests


# Runtime
FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

# Copy generated JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# Start Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
