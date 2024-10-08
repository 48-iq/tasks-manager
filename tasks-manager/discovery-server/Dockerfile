FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/discovery-server-*.jar /discovery-server.jar
EXPOSE 8888
CMD ["java", "-jar", "discovery-server.jar"]

