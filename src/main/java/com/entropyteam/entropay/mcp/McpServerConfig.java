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
    public ToolCallbackProvider rosterToolCallbackProvider(RosterMcpTools rosterMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(rosterMcpTools)
                .build();
    }
}
