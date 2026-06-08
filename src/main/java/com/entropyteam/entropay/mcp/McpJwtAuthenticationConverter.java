package com.entropyteam.entropay.mcp;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import com.entropyteam.entropay.auth.AuthorizationService;

/**
 * Maps an MCP request's Cognito token to Spring authorities. Roles are resolved through
 * entropay-users-auth ({@code /auth/identity}) — the platform's source of truth — exactly
 * like the REST API converter. The platform's roles live in the users-auth database, not
 * in {@code cognito:groups}; users-auth resolves the email via Cognito userInfo when the
 * token is an access token (which is what MCP clients send, and which has no email claim).
 */
public class McpJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final AuthorizationService authorizationService;

    public McpJwtAuthenticationConverter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        AuthorizationService.Identity identity = authorizationService.identity(jwt.getTokenValue());
        //TODO get role for current tenant
        Collection<String> roles = identity.rolesByTenant().values().stream()
                .findFirst()
                .orElse(Collections.emptySet());
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
