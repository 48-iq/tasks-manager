FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY gateway/pom.xml ./
COPY gateway/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/gateway-*.jar /gateway.jar
EXPOSE 8080
CMD ["java", "-jar", "gateway.jar"]

