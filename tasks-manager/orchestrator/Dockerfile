FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw orchestrator/pom.xml ./
RUN ./mvnw dependency:resolve
COPY orchestrator/src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/orchestrator-*.jar /orchestrator.jar
EXPOSE 8080
CMD ["java", "-jar", "orchestrator.jar"]

