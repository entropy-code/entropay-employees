package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import com.entropyteam.entropay.auth.AuthConstants;
import com.entropyteam.entropay.common.SpringContext;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformationService;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.entropyteam.entropay.employees.services.BillingService;
import com.entropyteam.entropay.employees.services.BillingService.BillingDto;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.employees.services.MarginService;
import com.entropyteam.entropay.employees.services.MarginService.MarginDto;
import com.entropyteam.entropay.employees.services.ReportService;
import com.entropyteam.entropay.employees.services.TurnoverService;
import com.entropyteam.entropay.mcp.testUtils.McpTestSecurityContext;

/**
 * End-to-end masking checks for the Reports tools. Salaries, billing and margin reports all
 * carry {@code @SensitiveInformation} on amount-bearing fields; this test asserts that
 * masking flows through Spring AI's tool serialization for each.
 */
@ExtendWith(MockitoExtension.class)
class ReportsMcpToolsMaskingTest {

    @Mock
    private EmployeeService employeeService;
    @Mock
    private ReportService reportService;
    @Mock
    private BillingService billingService;
    @Mock
    private MarginService marginService;
    @Mock
    private TurnoverService turnoverService;

    private SensitiveInformationService sensitiveInformationService;
    private ToolCallback salariesCallback;
    private ToolCallback billingCallback;
    private ToolCallback marginCallback;

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
        ApplicationContext mockCtx = mock(ApplicationContext.class);
        lenient().when(mockCtx.getBean(SensitiveInformationService.class)).thenReturn(sensitiveInformationService);
        springContextField.set(null, mockCtx);

        ReportsQueryService queryService = new ReportsQueryService(reportService, billingService, marginService,
                turnoverService);
        ReportsMcpTools tools = new ReportsMcpTools(queryService);
        ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
                .toolObjects(tools)
                .build()
                .getToolCallbacks();
        salariesCallback = pick(callbacks, "get_salaries_report");
        billingCallback = pick(callbacks, "get_billing_report");
        marginCallback = pick(callbacks, "get_margin_report");
    }

    @AfterEach
    void tearDown() throws Exception {
        McpTestSecurityContext.clear();
        springContextField.set(null, null);
    }

    @ParameterizedTest(name = "{0} sees masked salary on get_salaries_report")
    @ValueSource(strings = {
            AuthConstants.ROLE_MANAGER_HR,
            AuthConstants.ROLE_HR_DIRECTOR})
    @DisplayName("Non-admin roles allowed to call get_salaries_report see internal-employee salaries masked")
    void nonAdminRolesSeeMaskedSalaries(String role) {
        UUID internalEmployeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalEmployeeId));
        when(reportService.getSalariesReport(any())).thenReturn(new ReportDto<>(List.of(
                new SalariesReportDto(UUID.randomUUID(), internalEmployeeId, "INT-1", "Jane", "Doe", "ClientCo",
                        new BigDecimal("3000"), "Mural", "USD", "PlatformX", "Argentina", true)), 1));
        McpTestSecurityContext.authenticateWithRoles(role);

        String json = salariesCallback.call("{}");

        assertTrue(json.contains("\"salary\":null"), role + " should see masked salary. JSON: " + json);
    }

    @Test
    @DisplayName("Admin sees salaries unmasked on get_salaries_report")
    void adminSeesUnmaskedSalaries() {
        UUID internalEmployeeId = UUID.randomUUID();
        when(reportService.getSalariesReport(any())).thenReturn(new ReportDto<>(List.of(
                new SalariesReportDto(UUID.randomUUID(), internalEmployeeId, "INT-1", "Jane", "Doe", "ClientCo",
                        new BigDecimal("3000"), "Mural", "USD", "PlatformX", "Argentina", true)), 1));
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ADMIN);

        String json = salariesCallback.call("{}");

        assertTrue(json.contains("\"salary\":3000"), "Admin should see raw salary. JSON: " + json);
    }

    @Test
    @DisplayName("Admin sees rate and total unmasked on get_billing_report")
    void adminSeesUnmaskedBilling() {
        UUID internalEmployeeId = UUID.randomUUID();
        when(billingService.generateBillingReport(any())).thenReturn(new ReportDto<>(List.of(
                new BillingDto(UUID.randomUUID(), internalEmployeeId, "INT-1", "A", "B", "ClientCo",
                        "Project", new BigDecimal("80"), 160d, 8d, new BigDecimal("12800"), "notes")), 1));
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ADMIN);

        String json = billingCallback.call("{\"startDate\":\"2025-01-01\",\"endDate\":\"2025-12-31\"}");

        assertTrue(json.contains("\"rate\":80"), "Admin should see raw rate. JSON: " + json);
        assertTrue(json.contains("\"total\":12800"), "Admin should see raw total. JSON: " + json);
    }

    @Test
    @DisplayName("Non-admin seeing the billing payload through MCP gets rate and total masked for internal employees")
    void nonAdminBillingMasked() {
        // The @Secured gate blocks non-admin at runtime; this asserts that even if the
        // payload were ever exposed, the masking pipeline catches @SensitiveInformation
        // fields and zeros them out for non-admin callers viewing internal employees.
        UUID internalEmployeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalEmployeeId));
        when(billingService.generateBillingReport(any())).thenReturn(new ReportDto<>(List.of(
                new BillingDto(UUID.randomUUID(), internalEmployeeId, "INT-1", "A", "B", "ClientCo",
                        "Project", new BigDecimal("80"), 160d, 8d, new BigDecimal("12800"), "notes")), 1));
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ANALYST);

        String json = billingCallback.call("{\"startDate\":\"2025-01-01\",\"endDate\":\"2025-12-31\"}");

        assertTrue(json.contains("\"rate\":null"), "Non-admin should see masked rate. JSON: " + json);
        assertTrue(json.contains("\"total\":null"), "Non-admin should see masked total. JSON: " + json);
    }

    @Test
    @DisplayName("Non-admin seeing the margin payload through MCP gets rate, total, paid, margin masked for internal employees")
    void nonAdminMarginMasked() {
        UUID internalEmployeeId = UUID.randomUUID();
        when(employeeService.getInternalEmployeeIds()).thenReturn(Set.of(internalEmployeeId));
        when(marginService.generateMarginReport(any())).thenReturn(new ReportDto<>(List.of(
                new MarginDto(UUID.randomUUID(), "2025-03", internalEmployeeId, "INT-1", "A", "B", "ClientCo",
                        "Project", new BigDecimal("80"), 160d, 8d, new BigDecimal("12800"),
                        new BigDecimal("3000"), new BigDecimal("9800"))), 1));
        McpTestSecurityContext.authenticateWithRoles(AuthConstants.ROLE_ANALYST);

        String json = marginCallback.call("{\"startDate\":\"2025-03-01\",\"endDate\":\"2025-03-31\"}");

        assertTrue(json.contains("\"rate\":null"), "Non-admin should see masked rate. JSON: " + json);
        assertTrue(json.contains("\"total\":null"), "Non-admin should see masked total. JSON: " + json);
        assertTrue(json.contains("\"paid\":null"), "Non-admin should see masked paid. JSON: " + json);
        assertTrue(json.contains("\"margin\":null"), "Non-admin should see masked margin. JSON: " + json);
    }

    private ToolCallback pick(ToolCallback[] callbacks, String name) {
        return Arrays.stream(callbacks)
                .filter(cb -> name.equals(cb.getToolDefinition().name()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(name + " callback not advertised"));
    }
}
