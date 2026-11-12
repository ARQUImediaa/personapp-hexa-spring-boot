# Build multi-módulo: compila la API REST con sus dependencias del reactor (JDK 11)
FROM maven:3.8-eclipse-temurin-11 AS build
WORKDIR /src
COPY . .
RUN mvn -B -DskipTests package -pl rest-input-adapter -am

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /src/rest-input-adapter/target/rest-input-adapter-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
