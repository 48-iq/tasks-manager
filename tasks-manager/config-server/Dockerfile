FROM maven:3.9.9-eclipse-temurin-21-alpine AS dependencies
WORKDIR /app
COPY config-server/pom.xml ./
RUN mvn dependency:resolve

FROM dependencies AS build
COPY config-server/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre AS production
COPY --from=build /app/target/config-server-*.jar /config-server.jar
EXPOSE 8761
CMD ["java", "-jar", "config-server.jar"]

