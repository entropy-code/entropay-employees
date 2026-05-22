package com.entropyteam.entropay.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the payroll MCP tools with the Spring AI MCP server and enables MCP config
 * properties. {@link PayrollMcpTools} is passed as the tool object directly (it carries no
 * AOP annotations) so Spring AI can scan its {@code @Tool} methods without a proxy.
 */
@Configuration
@EnableConfigurationProperties(McpProperties.class)
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider payrollToolCallbackProvider(PayrollMcpTools payrollMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(payrollMcpTools)
                .build();
    }
}
