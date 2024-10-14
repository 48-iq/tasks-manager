FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY uuid-service/pom.xml ./
COPY uuid-service/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/uuid-service-*.jar /uuid-service.jar
EXPOSE 8080
CMD ["java", "-jar", "uuid-service.jar"]

