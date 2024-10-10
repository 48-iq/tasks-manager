FROM eclipse-temurin:21-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw user-service/pom.xml ./
RUN ./mvnw dependency:resolve
COPY user-service/src ./src

FROM base AS build
RUN ./mvnw package

FROM eclipse-temurin:21-jre-jammy AS production
COPY --from=build /app/target/user-service-*.jar /user-service.jar
EXPOSE 8080
CMD ["java", "-jar", "user-service.jar"]

