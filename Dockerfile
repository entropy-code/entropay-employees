# Build
FROM maven:3.8.4-openjdk-17 AS build-env
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY . ./
RUN mvn package
# Run
FROM openjdk:17
WORKDIR /app
COPY --from=build-env /app/target/entropay-employees.jar ./entropay-employees.jar
CMD ["java", "-jar", "/app/entropay-employees.jar", "-Xmx256m"]