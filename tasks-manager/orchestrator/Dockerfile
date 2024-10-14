FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR ./core
COPY ./core/pom.xml ./
COPY ./core/src ./src
RUN mvn install

WORKDIR /app
COPY orchestrator/pom.xml ./
COPY orchestrator/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/orchestrator-*.jar /orchestrator.jar
EXPOSE 8080
CMD ["java", "-jar", "orchestrator.jar"]

