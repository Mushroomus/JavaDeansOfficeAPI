FROM maven:4.0.0-openjdk-17 AS build
WORKDIR /app
COPY . /app/
RUN rm -rf /root/.m2
RUN mvn clean package

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080