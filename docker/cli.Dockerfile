# Build multi-módulo: compila el CLI con sus dependencias del reactor (JDK 11)
FROM maven:3.8-eclipse-temurin-11 AS build
WORKDIR /src
COPY . .
RUN mvn -B -DskipTests package -pl cli-input-adapter -am

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /src/cli-input-adapter/target/cli-input-adapter-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
