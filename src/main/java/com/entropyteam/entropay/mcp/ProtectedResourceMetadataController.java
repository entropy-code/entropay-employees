package com.entropyteam.entropay.mcp;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Publishes OAuth 2.0 Protected Resource Metadata (RFC 9728) for the MCP server, telling
 * MCP clients which authorization server (the entropay-users-auth gateway) backs it.
 * Public — discovery happens before any token exists.
 */
@RestController
public class ProtectedResourceMetadataController {

    private final McpProperties properties;

    public ProtectedResourceMetadataController(McpProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/.well-known/oauth-protected-resource")
    public ProtectedResourceMetadataDto protectedResourceMetadata() {
        return new ProtectedResourceMetadataDto(
                properties.resourceBaseUrl() + "/mcp/sse",
                List.of(properties.authServerUrl()),
                List.of("openid", "email", "profile"),
                List.of("header"));
    }
}
