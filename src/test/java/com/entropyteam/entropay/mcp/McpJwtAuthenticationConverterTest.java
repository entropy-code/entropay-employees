package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import com.entropyteam.entropay.auth.AuthorizationService;

@ExtendWith(MockitoExtension.class)
class McpJwtAuthenticationConverterTest {

    @Mock
    private AuthorizationService authorizationService;

    @Test
    void shouldMapRolesFromAuthorizationServiceIdentity() {
        // Given — users-auth resolves the token to an identity with the ADMIN role
        UUID tenantId = UUID.randomUUID();
        AuthorizationService.Identity identity = new AuthorizationService.Identity(
                UUID.randomUUID(), "eazaretzky", "Esteban", "Azaretzky", "esteban@entropyteam.com",
                Map.of(tenantId, Set.of("ADMIN")));
        when(authorizationService.identity("token-value")).thenReturn(identity);
        McpJwtAuthenticationConverter converter = new McpJwtAuthenticationConverter(authorizationService);

        // When
        JwtAuthenticationToken token = converter.convert(jwt());

        // Then
        Set<String> authorities = token.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertTrue(authorities.contains("ROLE_ADMIN"));
    }

    private Jwt jwt() {
        return Jwt.withTokenValue("token-value")
                .header("alg", "none")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("sub", "user-1")
                .build();
    }
}
