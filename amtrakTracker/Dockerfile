FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY ./src/main/resources/static ./src/main/resources/static

COPY --from=build /app/target/amtrakTracker-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "/app/amtrakTracker-0.0.1-SNAPSHOT.jar"]