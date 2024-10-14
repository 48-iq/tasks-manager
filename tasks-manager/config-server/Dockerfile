FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY config-server/pom.xml ./
COPY config-server/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre AS production
COPY --from=build /app/target/config-server-*.jar /config-server.jar
EXPOSE 8761
CMD ["java", "-jar", "config-server.jar"]

