# Use a more general OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/ebill-estimator-0.0.1-SNAPSHOT.jar app.jar

# Make the jar executable
RUN chmod +x app.jar

# Expose the port the app will run on
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]




