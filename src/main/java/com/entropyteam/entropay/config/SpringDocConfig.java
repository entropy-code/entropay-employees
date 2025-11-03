package com.entropyteam.entropay.config;

import java.util.Optional;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI(Optional<BuildProperties> buildProperties) {
        String version = buildProperties.map(BuildProperties::getVersion).orElse("dev");

        return new OpenAPI()
                .info(new Info()
                        .title("Entroteam Employees API")
                        .description("API for managing employees, projects, and related resources")
                        .version(version)
                        .contact(new Contact()
                                .name("Entroteam")
                                .email("support@entroteam.com")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token (without 'Bearer ' prefix)")));
    }
}