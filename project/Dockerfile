# Stage 1: Build the application
FROM jelastic/maven:3.9.5-openjdk-21 AS builder

WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src
# Build the JAR file
RUN mvn clean install -DskipTests

# Stage 2: Create the final, lightweight image
FROM openjdk:21-jre-slim
# This is a much smaller image for running the app

WORKDIR /app

# Copy the built JAR from the 'builder' stage
COPY --from=builder /app/target/project-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]