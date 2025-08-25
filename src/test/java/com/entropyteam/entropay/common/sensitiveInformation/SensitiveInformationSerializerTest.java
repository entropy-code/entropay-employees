package com.entropyteam.entropay.common.sensitiveInformation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import com.entropyteam.entropay.common.SpringContext;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class SensitiveInformationSerializerTest {

    @Mock
    private SensitiveInformationService sensitiveInformationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Field contextField;

    static {
        try {
            contextField = SpringContext.class.getDeclaredField("context");
            contextField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext mockCtx = mock(ApplicationContext.class);
        when(mockCtx.getBean(SensitiveInformationService.class)).thenReturn(sensitiveInformationService);
        contextField.set(null, mockCtx);
    }

    @AfterEach
    void tearDown() throws Exception {
        contextField.set(null, null); // reset static to avoid leaking between tests
    }

    // Simple POJO implementing EmployeeIdAware with one sensitive field
    static class SalaryView implements EmployeeIdAware {
        private final UUID employeeId;
        @SensitiveInformation
        private final Integer amount;
        private final String label;

        SalaryView(UUID employeeId, Integer amount, String label) {
            this.employeeId = employeeId;
            this.amount = amount;
            this.label = label;
        }

        @Override
        public UUID getEmployeeId() { return employeeId; }

        public Integer getAmount() { return amount; }
        public String getLabel() { return label; }
    }

    @Test
    @DisplayName("Serializer masks sensitive field when shouldMask returns true (POJO)")
    void serializerMasksField() throws Exception {
        // given
        UUID internalId = UUID.randomUUID();
        when(sensitiveInformationService.shouldMask(internalId)).thenReturn(true);
        SalaryView view = new SalaryView(internalId, 5000, "Some Label");

        // when
        String json = objectMapper.writeValueAsString(view);

        // then
        assertTrue(json.contains("\"amount\":null"), "Expected sensitive amount to be masked to null. JSON: " + json);
        assertTrue(json.contains("\"label\":\"Some Label\""), "Non-sensitive field should remain present");
    }

    @Test
    @DisplayName("Serializer keeps sensitive field when shouldMask returns false (POJO)")
    void serializerKeepsField() throws Exception {
        // given
        UUID externalId = UUID.randomUUID();
        when(sensitiveInformationService.shouldMask(externalId)).thenReturn(false);
        SalaryView view = new SalaryView(externalId, 7500, "Another Label");

        // when
        String json = objectMapper.writeValueAsString(view);

        // then
        assertFalse(json.contains("\"amount\":null"), "Did not expect sensitive amount to be masked. JSON: " + json);
        assertTrue(json.contains("\"amount\":7500"), "Expected original amount value present. JSON: " + json);
    }

    @Test
    @DisplayName("Serializer masks sensitive record components when shouldMask returns true (record)")
    void serializerMasksSensitiveRecordComponents() throws Exception {
        // given
        UUID employeeId = UUID.randomUUID();
        when(sensitiveInformationService.shouldMask(employeeId)).thenReturn(true);
        SalariesReportDto dto = new SalariesReportDto(
                UUID.randomUUID(), employeeId, "INT-1", "John", "Doe", "ClientCo",
                new BigDecimal("10000"), new BigDecimal("120"), "FULL_TIME", "USD", "PlatformX", "CountryY");

        // when
        String json = objectMapper.writeValueAsString(dto);

        // then
        assertTrue(json.contains("\"salary\":null"), "Salary should be masked. JSON: " + json);
        assertTrue(json.contains("\"rate\":null"), "Rate should be masked. JSON: " + json);
        assertTrue(json.contains("\"firstName\":\"John\""), "Non-sensitive fields should remain. JSON: " + json);
    }
}

