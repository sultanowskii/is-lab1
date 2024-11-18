FROM amazoncorretto:21.0.5

WORKDIR /app

# Copy the Gradle wrapper and project files
COPY gradlew .
COPY settings.gradle .
COPY gradle gradle
COPY app app

# Grant execute permissions for the Gradle wrapper
RUN chmod +x ./gradlew

# Run the application
ENTRYPOINT ["./gradlew", "app:run"]
