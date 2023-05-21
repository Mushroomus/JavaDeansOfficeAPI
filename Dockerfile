# Use a base image with Java support
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY ./pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline -B

# Copy the application source code to the container
COPY ./src ./src

# Build the application
RUN mvn package -DskipTests

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "target/deansoffice-0.0.1-SNAPSHOT.jar"]