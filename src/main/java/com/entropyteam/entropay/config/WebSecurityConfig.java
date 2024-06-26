package com.entropyteam.entropay.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import com.entropyteam.entropay.auth.TokenService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/actuator/**"
    };

    private final TokenService tokenService;

    public WebSecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated().and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtCustomConverter());
        return httpSecurity.build();
    }

    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtCustomConverter() {
        return jwt -> new JwtAuthenticationToken(jwt, getGrantedAuthorities(jwt.getClaims()));
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Map<String, Object> appClaims) {
        Map<String, Collection<String>> rolesByTenant = tokenService.getRoles(appClaims);
        //TODO get role for current tenant
        Collection<String> roles = rolesByTenant.values().stream().findFirst().orElse(Collections.emptySet());
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
