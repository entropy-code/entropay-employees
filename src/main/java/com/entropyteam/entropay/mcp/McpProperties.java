package com.entropyteam.entropay.mcp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the embedded MCP server.
 */
@ConfigurationProperties(prefix = "mcp")
public record McpProperties(
        /** Public base URL this service is reachable at; used to build the resource metadata. */
        String resourceBaseUrl,
        /** Base URL of the OAuth2 authorization server (the entropay-users-auth gateway). */
        String authServerUrl) {
}
