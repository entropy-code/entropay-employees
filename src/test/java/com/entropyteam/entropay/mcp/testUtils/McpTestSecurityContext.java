package com.entropyteam.entropay.mcp.testUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Shared helper for tests that need to assert behaviour of MCP query services and tool
 * callbacks under different authenticated roles. Mirrors the platform's runtime path:
 * roles arrive as {@code SimpleGrantedAuthority} on the {@code SecurityContextHolder},
 * matching what {@link com.entropyteam.entropay.mcp.McpJwtAuthenticationConverter} sets
 * for real requests.
 *
 * <p>Always pair {@link #authenticateWithRoles(String...)} with {@link #clear()} (typically
 * in an {@code @AfterEach}) to avoid leaking auth between tests.
 */
public final class McpTestSecurityContext {

    private McpTestSecurityContext() {
    }

    public static void authenticateWithRoles(String... roles) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "mcp-test-user", "n/a",
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}
