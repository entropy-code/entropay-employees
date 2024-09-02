# Build
FROM maven:3.9.6-amazoncorretto-21 AS build-env
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY . ./
RUN mvn package

# Add New Relic
RUN mkdir -p /usr/local/newrelic
ADD ./newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

# Run
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build-env /app/target/entropay-employees.jar ./entropay-employees.jar
CMD ["java", "-javaagent:/usr/local/newrelic/newrelic.jar", "-jar", "/app/entropay-employees.jar", "-Xmx256m"]