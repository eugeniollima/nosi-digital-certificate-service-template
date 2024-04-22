FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR usr/src/service

COPY .mvn .mvn
COPY src src
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml

RUN chmod +x mvnw
RUN ./mvnw clean package

FROM eclipse-temurin:21-jdk-alpine

COPY --from=build /usr/src/service/target/ndc-api-0.0.1-SNAPSHOT.jar ./

EXPOSE 8080

CMD ["java", "-jar", "./ndc-api-0.0.1-SNAPSHOT.jar"]
