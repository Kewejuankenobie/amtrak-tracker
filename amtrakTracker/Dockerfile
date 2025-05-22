FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DSkipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY ./target/amtrakTracker-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "/app/amtrakTracker-0.0.1-SNAPSHOT.jar"]