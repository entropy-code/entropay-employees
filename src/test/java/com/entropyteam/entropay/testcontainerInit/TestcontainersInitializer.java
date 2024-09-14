package com.entropyteam.entropay.testcontainerInit;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    static {
        postgresqlContainer.start();
        Flyway flyway = Flyway.configure()
                .dataSource(postgresqlContainer.getJdbcUrl(),
                        postgresqlContainer.getUsername(),
                        postgresqlContainer.getPassword())
                .load();
        flyway.migrate();
        try (Connection connection = postgresqlContainer.createConnection("")) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("employees-test-data.sql"));
        } catch (Exception e) {
            throw new RuntimeException("Error executing init script", e);
        }
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
                "spring.datasource.username=" +postgresqlContainer.getUsername(),
                "spring.datasource.password=" + postgresqlContainer.getPassword(),
                "spring.datasource.driver-class-name=org.postgresql.Driver"
        ).applyTo(ctx.getEnvironment());
    }
}