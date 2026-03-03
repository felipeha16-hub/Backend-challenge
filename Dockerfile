FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy configuration ofGradle and wrapper
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle settings.gradle ./

# Copy source code
COPY src/ src/

# Compile the application
RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

# Expose port
EXPOSE 8080





