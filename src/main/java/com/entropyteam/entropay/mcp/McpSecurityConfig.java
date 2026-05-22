package com.entropyteam.entropay.mcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.entropyteam.entropay.auth.AuthorizationService;

/**
 * Dedicated Spring Security chain for the MCP endpoints, ordered ahead of the REST API
 * chain. {@code /mcp/**} is validated as an OAuth2 resource server; roles are resolved via
 * entropay-users-auth. The Protected Resource Metadata document stays public so
 * unauthenticated MCP clients can discover the authorization server.
 */
@Configuration
public class McpSecurityConfig {

    static final String MCP_ENDPOINT = "/mcp";
    static final String MCP_PATHS = "/mcp/**";
    static final String PROTECTED_RESOURCE_METADATA_PATH = "/.well-known/oauth-protected-resource";

    @Bean
    @Order(1)
    public SecurityFilterChain mcpSecurityFilterChain(HttpSecurity http, McpProperties properties,
            AuthorizationService authorizationService) throws Exception {
        McpAuthenticationEntryPoint entryPoint = new McpAuthenticationEntryPoint(
                properties.resourceBaseUrl() + PROTECTED_RESOURCE_METADATA_PATH);
        http
                .securityMatcher(MCP_ENDPOINT, MCP_PATHS, PROTECTED_RESOURCE_METADATA_PATH)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PROTECTED_RESOURCE_METADATA_PATH).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(entryPoint)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new McpJwtAuthenticationConverter(authorizationService))));
        return http.build();
    }
}
