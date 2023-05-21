FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . /app/

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080