package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProtectedResourceMetadataControllerTest {

    @Test
    void shouldPointAtTheConfiguredAuthorizationServer() {
        // Given
        McpProperties properties = new McpProperties("http://localhost:8100", "http://localhost:8000");
        ProtectedResourceMetadataController controller = new ProtectedResourceMetadataController(properties);

        // When
        ProtectedResourceMetadataDto metadata = controller.protectedResourceMetadata();

        // Then
        assertEquals("http://localhost:8100/mcp/sse", metadata.resource());
        assertTrue(metadata.authorizationServers().contains("http://localhost:8000"));
    }
}
