# Use lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build JAR (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose API Gateway port
EXPOSE 8080

# Run JAR
ENTRYPOINT ["java", "-jar", "target/api-gateway-0.0.1-SNAPSHOT.jar"]
