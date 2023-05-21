# Use a base image with Java support
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/deansoffice-0.0.1-SNAPSHOT.jar .

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "deansoffice-0.0.1-SNAPSHOT.jar"]