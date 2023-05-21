FROM eclipse-temurin:17-jdk-alpine
COPY *.jar app.jar
ENTRYPOINT ["java","-jar","target/app.jar"]
EXPOSE 8080