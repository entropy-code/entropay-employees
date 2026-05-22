package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.mcp.dtos.EmployeeDetail;

@ExtendWith(MockitoExtension.class)
class PayrollMcpToolsTest {

    @Mock
    private PayrollQueryService payrollQueryService;

    @InjectMocks
    private PayrollMcpTools tools;

    @Test
    void getEmployeeRejectsInvalidUuid() {
        assertThrows(IllegalArgumentException.class, () -> tools.getEmployee("not-a-uuid"));
    }

    @Test
    void listEmployeeReimbursementsRejectsInvalidDate() {
        UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class,
                () -> tools.listEmployeeReimbursements(id.toString(), "31-12-2026", null));
    }

    @Test
    void getEmployeeDelegatesWithParsedUuid() {
        // Given
        UUID id = UUID.randomUUID();
        EmployeeDetail detail = new EmployeeDetail(id, "E-1", "Ada", "Lovelace", null, null, null, null,
                null, null, null, null, null, null, true, null, null, null, null, null, null);
        when(payrollQueryService.getEmployee(id)).thenReturn(detail);

        // When / Then
        assertEquals(detail, tools.getEmployee(id.toString()));
    }
}
