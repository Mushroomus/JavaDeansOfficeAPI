FROM maven:3.8.3-openjdk-19 AS build
WORKDIR /app
COPY . /app/
RUN mvn clean package

FROM openjdk:19-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080