package com.entropyteam.entropay.employees.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
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
import com.entropyteam.entropay.common.sensitiveInformation.EmployeeIdAware;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformation;
import com.entropyteam.entropay.common.sensitiveInformation.SensitiveInformationService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Guards that {@code EmployeeDto} keeps masking the employee's compensation. The {@code rate}
 * and {@code salary} fields expose employee compensation and must be hidden from non-ADMIN
 * callers viewing internal employees, exactly like {@code AssignmentDto.billableRate} and
 * {@code SalariesReportDto.salary}.
 *
 * <p>Two layers of protection are asserted:
 * <ol>
 *   <li><b>Structural</b> — the fields carry {@link SensitiveInformation} and the DTO implements
 *       {@link EmployeeIdAware}. The annotation is a no-op without the interface: the serializer
 *       walks the JSON output context for the nearest {@code EmployeeIdAware} ancestor to resolve
 *       the masking subject, and an un-{@code EmployeeIdAware} DTO never gets masked.</li>
 *   <li><b>Behavioural</b> — serializing a real {@code EmployeeDto} through the same Jackson
 *       pipeline the REST API uses masks rate and salary when the service says to, and leaves
 *       them intact when it does not.</li>
 * </ol>
 */
@ExtendWith(MockitoExtension.class)
class EmployeeDtoSensitiveInformationTest {

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

    private Object previousContext;

    @BeforeEach
    void setUp() throws Exception {
        ApplicationContext mockCtx = mock(ApplicationContext.class);
        // lenient: the reflection-only tests never serialize, so they never resolve the bean.
        lenient().when(mockCtx.getBean(SensitiveInformationService.class)).thenReturn(sensitiveInformationService);
        previousContext = contextField.get(null);
        contextField.set(null, mockCtx);
    }

    @AfterEach
    void tearDown() throws Exception {
        contextField.set(null, previousContext);
    }

    @Test
    @DisplayName("rate and salary fields are annotated with @SensitiveInformation")
    void rateAndSalaryAreAnnotated() throws NoSuchFieldException {
        Field rate = EmployeeDto.class.getDeclaredField("rate");
        Field salary = EmployeeDto.class.getDeclaredField("salary");

        assertTrue(rate.isAnnotationPresent(SensitiveInformation.class),
                "EmployeeDto.rate must be annotated with @SensitiveInformation");
        assertTrue(salary.isAnnotationPresent(SensitiveInformation.class),
                "EmployeeDto.salary must be annotated with @SensitiveInformation");
    }

    @Test
    @DisplayName("EmployeeDto implements EmployeeIdAware so the annotation actually masks")
    void implementsEmployeeIdAware() {
        assertTrue(EmployeeIdAware.class.isAssignableFrom(EmployeeDto.class),
                "EmployeeDto must implement EmployeeIdAware; without it @SensitiveInformation never masks");
    }

    @Test
    @DisplayName("Serializer masks rate and salary when shouldMask returns true")
    void masksCompensationWhenShouldMask() throws Exception {
        // given
        UUID employeeId = UUID.randomUUID();
        when(sensitiveInformationService.shouldMask(employeeId)).thenReturn(true);
        EmployeeDto dto = dtoWith(employeeId, new BigDecimal("75.50"), new BigDecimal("5000"));

        // when
        String json = objectMapper.writeValueAsString(dto);

        // then
        assertTrue(json.contains("\"rate\":null"), "Expected rate to be masked. JSON: " + json);
        assertTrue(json.contains("\"salary\":null"), "Expected salary to be masked. JSON: " + json);
        assertTrue(json.contains("\"firstName\":\"Jane\""), "Non-sensitive fields should remain. JSON: " + json);
    }

    @Test
    @DisplayName("Serializer keeps rate and salary when shouldMask returns false")
    void keepsCompensationWhenNotMasked() throws Exception {
        // given
        UUID employeeId = UUID.randomUUID();
        when(sensitiveInformationService.shouldMask(employeeId)).thenReturn(false);
        EmployeeDto dto = dtoWith(employeeId, new BigDecimal("75.50"), new BigDecimal("5000"));

        // when
        String json = objectMapper.writeValueAsString(dto);

        // then
        assertFalse(json.contains("\"rate\":null"), "Did not expect rate to be masked. JSON: " + json);
        assertFalse(json.contains("\"salary\":null"), "Did not expect salary to be masked. JSON: " + json);
        assertTrue(json.contains("\"rate\":75.50"), "Expected raw rate present. JSON: " + json);
        assertTrue(json.contains("\"salary\":5000"), "Expected raw salary present. JSON: " + json);
    }

    private static EmployeeDto dtoWith(UUID employeeId, BigDecimal rate, BigDecimal salary) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employeeId); // EmployeeDto.id is the employee's own id, the masking subject
        dto.setFirstName("Jane");
        dto.setRate(rate);
        dto.setSalary(salary);
        return dto;
    }
}
