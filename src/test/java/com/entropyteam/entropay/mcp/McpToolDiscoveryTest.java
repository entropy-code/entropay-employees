package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import com.entropyteam.entropay.employees.services.EmployeeService;

/**
 * Snapshot of the MCP tool surface advertised by the production tool-callback providers.
 * Acceptance criterion of the parent story: "The complete list of tools is exposed to the
 * MCP client at connection time so the assistant can discover them." If a tool is added,
 * renamed, or accidentally dropped, this test fails and forces the change to be intentional.
 *
 * <p>As new tool providers land in subsequent PRs (Employee 360, Time off, Reimbursements,
 * Reports), this expected-set is extended in lockstep.
 */
@ExtendWith(MockitoExtension.class)
class McpToolDiscoveryTest {

    @Mock
    private EmployeeService employeeService;

    @Test
    @DisplayName("Roster tool callback provider advertises exactly the expected tool names")
    void rosterToolCallbackProviderAdvertisesExpectedTools() {
        RosterMcpTools rosterMcpTools = new RosterMcpTools(new RosterQueryService(employeeService));

        ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
                .toolObjects(rosterMcpTools)
                .build()
                .getToolCallbacks();

        Set<String> advertisedNames = Arrays.stream(callbacks)
                .map(cb -> cb.getToolDefinition().name())
                .collect(Collectors.toSet());

        assertEquals(Set.of("list_roster"), advertisedNames,
                "Advertised tool names must match the expected snapshot. Update this test when a tool "
                        + "is intentionally added or removed.");
    }

    @Test
    @DisplayName("Every advertised tool exposes a non-blank name and description")
    void everyAdvertisedToolHasNameAndDescription() {
        RosterMcpTools rosterMcpTools = new RosterMcpTools(new RosterQueryService(employeeService));

        ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
                .toolObjects(rosterMcpTools)
                .build()
                .getToolCallbacks();

        List<ToolCallback> all = Arrays.asList(callbacks);
        assertTrue(all.size() > 0, "Expected at least one advertised tool");
        all.forEach(cb -> {
            assertTrue(cb.getToolDefinition().name() != null && !cb.getToolDefinition().name().isBlank(),
                    "Tool must have a non-blank name");
            assertTrue(cb.getToolDefinition().description() != null
                            && !cb.getToolDefinition().description().isBlank(),
                    "Tool '" + cb.getToolDefinition().name() + "' must have a non-blank description");
        });
    }
}
