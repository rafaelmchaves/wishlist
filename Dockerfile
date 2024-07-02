#Use the OpenJDK 17 as a parent image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR into the container
COPY target/wishlist-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
