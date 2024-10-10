FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw auth-service/pom.xml ./
RUN ./mvnw dependency:resolve
COPY auth-service/src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/auth-service-*.jar /auth-service.jar
EXPOSE 8080
CMD ["java", "-jar", "auth-service.jar"]

