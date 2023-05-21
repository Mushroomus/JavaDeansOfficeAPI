# Use a base image with Java and Maven support
FROM adoptopenjdk:11-jdk-hotspot as builder
RUN apt-get update && apt-get install -y maven

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

# Create a new image without Maven, using a lighter base image
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/deansoffice-0.0.1-SNAPSHOT.jar .

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "deansoffice-0.0.1-SNAPSHOT.jar"]