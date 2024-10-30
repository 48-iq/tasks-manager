FROM maven:3.9.9-eclipse-temurin-21-alpine AS dependencies
WORKDIR /app
COPY discovery-server/pom.xml ./
RUN mvn dependency:resolve

FROM dependencies AS build
COPY discovery-server/src ./src
RUN mvn package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/discovery-server-*.jar /discovery-server.jar
EXPOSE 8888
CMD ["java", "-jar", "discovery-server.jar"]

