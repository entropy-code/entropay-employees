package com.entropyteam.entropay.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import com.entropyteam.entropay.auth.AuthorizationService;
import com.entropyteam.entropay.auth.AuthorizationService.Identity;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/actuator/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final AuthorizationService authorizationService;

    public WebSecurityConfig(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtCustomConverter())));
        return httpSecurity.build();
    }

    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtCustomConverter() {
        return jwt -> {
            String tokenValue = jwt.getTokenValue();
            Identity identity = authorizationService.identity(tokenValue);
            return new JwtAuthenticationToken(jwt, getGrantedAuthorities(identity.rolesByTenant()));
        };
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Map<UUID, Set<String>> rolesByTenant) {
        //TODO get role for current tenant
        Collection<String> roles = rolesByTenant.values().stream().findFirst().orElse(Collections.emptySet());
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
