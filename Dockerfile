# Use an official OpenJDK 21 image
FROM eclipse-temurin:21-jdk


# Set working directory
WORKDIR /app

# Copy Maven build files first to leverage caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the app
RUN ./mvnw package -DskipTests

# Run the jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
