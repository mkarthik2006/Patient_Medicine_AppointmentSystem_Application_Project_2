# -------- Stage 1: Build the application --------
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set working directory inside container
WORKDIR /app

# Copy project files to the container
COPY . .

# Build the application and skip tests for faster build
RUN ./mvnw clean package -DskipTests

# -------- Stage 2: Run the application --------
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port (adjust if needed)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]




