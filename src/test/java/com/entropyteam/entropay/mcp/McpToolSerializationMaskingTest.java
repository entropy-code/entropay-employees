package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import com.entropyteam.entropay.auth.AuthConstants;
import com.entropyteam.entropay.common.SpringContext;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformationService;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.testUtils.McpTestSecurityContext;

/**
 * Bloqueante-check for the MCP tools work: proves that {@code @SensitiveInformation}-annotated
 * fields are masked by the SAME mechanism the REST API uses when the value is returned through
 * a Spring AI MCP tool callback. The underlying contract is Jackson annotation-driven, so the
 * test wires up a real {@link com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformationSerializer}
 * + real {@link SensitiveInformationService} and invokes a probe {@code @Tool} method via
 * {@link MethodToolCallbackProvider} — the exact path Spring AI uses to serialize tool results
 * before sending them to the MCP client.
 *
 * <p>If this test ever starts failing for the masking case, the masking has stopped flowing
 * through MCP and ALL tool outputs that include sensitive fields must be treated as suspect
 * until the regression is fixed. This guard is what unblocks the rest of the MCP tool surface.
 */
@ExtendWith(MockitoExtension.class)
class McpToolSerializationMaskingTest {

    @Mock
    private EmployeeService employeeService;

    private SensitiveInformationService sensitiveInformationService;
    private SalaryProbeTool probeTool;
    private ToolCallback toolCallback;

    private static Field springContextField;

    static {
        try {
            springContextField = SpringContext.class.getDeclaredField("context");
            springContextField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        sensitiveInformationService = new SensitiveInformationService(employeeService);

        // Wire the static SpringContext so SensitiveInformationSerializer (built lazily by
        // Jackson) can resolve the bean. Mirrors the live wiring in production. lenient()
        // because the discovery-only test does not exercise serialization.
        ApplicationContext mockCtx = mock(ApplicationContext.class);
        lenient().when(mockCtx.getBean(SensitiveInformationService.class)).thenReturn(sensitiveInformationService);
        springContextField.set(null, mockCtx);

        probeTool = new SalaryProbeTool();
        ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
                .toolObjects(probeTool)
                .build()
                .getToolCallbacks();
        toolCallback = Arrays.stream(callbacks)
                .filter(cb -> "probe_salary".equals(cb.getToolDefinition().name()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("probe_salary callback not advertised"));
    }

    @AfterEach
    void tearDown() throws Exception {
        McpTestSecurityContext.clear();
        springContextField.set(null, null);
    }

    @Test
    @DisplayName("Non-admin caller viewing an internal employee through MCP gets the salary masked")
    void nonAdminCallerSeesMaskedSalary() {
        // given
        UUID internalEmployeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalEmployeeId));
        probeTool.setEmployeeId(internalEmployeeId);
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ANALYST);

        // when
        String json = toolCallback.call("{}");

        // then
        assertTrue(json.contains("\"salary\":null"),
                "Expected sensitive salary to be masked through MCP tool serialization. JSON: " + json);
        assertTrue(json.contains("\"firstName\":\"Jane\""),
                "Non-sensitive fields should remain present. JSON: " + json);
    }

    @Test
    @DisplayName("Admin caller viewing an internal employee through MCP sees the salary unmasked")
    void adminCallerSeesUnmaskedSalary() {
        // given
        UUID internalEmployeeId = UUID.randomUUID();
        // No mock for getInternalEmployeeIds — admins short-circuit before the lookup runs.
        probeTool.setEmployeeId(internalEmployeeId);
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ADMIN);

        // when
        String json = toolCallback.call("{}");

        // then
        assertTrue(json.contains("\"salary\":12345"),
                "Admin should see the raw salary. JSON: " + json);
    }

    @Test
    @DisplayName("Non-admin caller viewing an external employee through MCP sees the salary unmasked")
    void nonAdminCallerOnExternalEmployeeSeesUnmaskedSalary() {
        // given — only internal employee IDs are masked; an external ID is not in the set.
        UUID externalEmployeeId = UUID.randomUUID();
        lenient().when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(UUID.randomUUID()));
        probeTool.setEmployeeId(externalEmployeeId);
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ANALYST);

        // when
        String json = toolCallback.call("{}");

        // then
        assertTrue(json.contains("\"salary\":12345"),
                "External employees should not be masked even for non-admin callers. JSON: " + json);
    }

    @ParameterizedTest(name = "{0} callers see internal-employee salary masked")
    @ValueSource(strings = {
            AuthConstants.ROLE_HR_DIRECTOR,
            AuthConstants.ROLE_MANAGER_HR,
            AuthConstants.ROLE_ANALYST,
            AuthConstants.ROLE_DEVELOPMENT})
    @DisplayName("All non-admin roles see masked salary for internal employees")
    void nonAdminRolesAllSeeMaskedSalary(String role) {
        // given
        UUID internalEmployeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalEmployeeId));
        probeTool.setEmployeeId(internalEmployeeId);
        McpTestSecurityContext.authenticateWithRoles(role);

        // when
        String json = toolCallback.call("{}");

        // then
        assertTrue(json.contains("\"salary\":null"),
                role + " should see masked salary. JSON: " + json);
    }

    /**
     * Test-only @Tool whose single method returns a SalariesReportDto. The DTO carries
     * {@code @SensitiveInformation} on its {@code salary} field, which is the contract under
     * test. The tool is invoked via the same {@link MethodToolCallbackProvider} the production
     * MCP server uses, so any masking behaviour observed here is what an MCP client would see.
     */
    public static class SalaryProbeTool {
        private UUID employeeId;

        public void setEmployeeId(UUID employeeId) {
            this.employeeId = employeeId;
        }

        @Tool(name = "probe_salary",
                description = "Test-only probe returning a salaries DTO with one sensitive field.")
        public List<SalariesReportDto> probeSalary() {
            return List.of(new SalariesReportDto(
                    UUID.randomUUID(), employeeId, "INT-001", "Jane", "Doe", "ClientCo",
                    new BigDecimal("12345"), "Mural", "USD", "PlatformX", "CountryY", true));
        }
    }

    @Test
    @DisplayName("Probe tool is advertised by the MethodToolCallbackProvider")
    void probeToolIsAdvertised() {
        assertEquals("probe_salary", toolCallback.getToolDefinition().name());
    }
}
