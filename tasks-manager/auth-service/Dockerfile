FROM maven:3.9.9-eclipse-temurin-21-alpine AS dependencies
WORKDIR ./core
COPY ./core/pom.xml ./
RUN mvn dependency:resolve

COPY ./core/src ./src
RUN mvn install

WORKDIR /app
COPY auth-service/pom.xml ./
RUN mvn dependency:resolve

FROM dependencies AS build
COPY auth-service/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/auth-service-*.jar /auth-service.jar
EXPOSE 8080
CMD ["java", "-jar", "auth-service.jar"]

