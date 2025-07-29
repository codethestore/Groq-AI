FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy all project files
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# === Stage 2: Run the built jar ===
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built jar from previous stage
COPY --from=builder /app/target/profAI-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8085

# Allow optional JVM args via JAVA_OPTS env variable
ENV JAVA_OPTS=""

# Run the Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
