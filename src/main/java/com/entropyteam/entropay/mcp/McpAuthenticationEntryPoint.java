package com.entropyteam.entropay.mcp;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Returns 401 with an RFC 9728 {@code WWW-Authenticate} header pointing MCP clients at the
 * Protected Resource Metadata document, so they can discover the authorization server.
 */
public class McpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String resourceMetadataUrl;

    public McpAuthenticationEntryPoint(String resourceMetadataUrl) {
        this.resourceMetadataUrl = resourceMetadataUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                "Bearer resource_metadata=\"" + resourceMetadataUrl + "\"");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"error\":\"unauthorized\","
                        + "\"error_description\":\"Missing or invalid access token\"}");
    }
}
