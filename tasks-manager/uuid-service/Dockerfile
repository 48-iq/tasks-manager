FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw uuid-service/pom.xml ./
RUN ./mvnw dependency:resolve
COPY uuid-service/src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/uuid-service-*.jar /uuid-service.jar
EXPOSE 8080
CMD ["java", "-jar", "uuid-service.jar"]

