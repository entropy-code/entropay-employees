# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS build-env
WORKDIR /app

# Copy configuration files first for better cache utilization
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Download dependencies (better caching)
RUN mvn dependency:go-offline -B

# Copy source code only (excludes target/, .git/, etc.)
COPY src/ ./src/

# Build with optimizations and parallelization
RUN mvn package -B -T 1C -DskipTests=true --no-transfer-progress

# Runtime stage with lighter image
FROM amazoncorretto:21-alpine AS runtime
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the final JAR
COPY --from=build-env /app/target/entropay-employees.jar ./entropay-employees.jar

# Switch to non-root user
USER appuser

# JVM optimized for containers
ENV JAVA_MAX_RAM_PERCENTAGE=75.0
CMD java \
     -XX:+UseContainerSupport \
     -XX:MaxRAMPercentage=${JAVA_MAX_RAM_PERCENTAGE} \
     -XX:+UseG1GC \
     -XX:+UseStringDeduplication \
     -jar /app/entropay-employees.jar