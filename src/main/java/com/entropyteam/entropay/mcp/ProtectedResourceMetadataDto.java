package com.entropyteam.entropay.mcp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OAuth 2.0 Protected Resource Metadata (RFC 9728). Tells MCP clients which authorization
 * server backs this resource server.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProtectedResourceMetadataDto(
        @JsonProperty("resource") String resource,
        @JsonProperty("authorization_servers") List<String> authorizationServers,
        @JsonProperty("scopes_supported") List<String> scopesSupported,
        @JsonProperty("bearer_methods_supported") List<String> bearerMethodsSupported) {
}
