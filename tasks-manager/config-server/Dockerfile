FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre AS production
COPY --from=build /app/target/config-server-*.jar /config-server.jar
EXPOSE 8761
CMD ["java", "-jar", "config-server.jar"]

