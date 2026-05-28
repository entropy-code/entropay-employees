package com.entropyteam.entropay.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(McpProperties.class)
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider mcpToolCallbackProvider(RosterMcpTools rosterMcpTools,
            Employee360McpTools employee360McpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(rosterMcpTools, employee360McpTools)
                .build();
    }
}
