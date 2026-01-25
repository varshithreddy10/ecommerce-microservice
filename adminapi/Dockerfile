FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 7002

ENTRYPOINT ["java","-jar","app.jar"]
