FROM eclipse-temurin:17-jdk-alpine
COPY *.jar /opt/app.jar
ENTRYPOINT ["java","-jar","/opt/app.jar"]
EXPOSE 8080