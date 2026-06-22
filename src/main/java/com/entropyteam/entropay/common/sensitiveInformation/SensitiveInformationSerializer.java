package com.entropyteam.entropay.common.sensitiveInformation;

import java.io.IOException;
import com.entropyteam.entropay.common.SpringContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SensitiveInformationSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        EmployeeIdAware employee = findEmployeeIdAwareAncestor(gen);

        if (employee != null) {
            // Fail closed: finding an EmployeeIdAware ancestor means we are inside a sensitive
            // context. If the masking service cannot be resolved (e.g. SpringContext not yet
            // initialized), mask rather than leak — the raw value must never reach the wire when
            // we are unable to evaluate the masking decision.
            SensitiveInformationService service = SpringContext.getBean(SensitiveInformationService.class);
            if (service == null || service.shouldMask(employee.getEmployeeId())) {
                value = null;
            }
        }

        serializers.defaultSerializeValue(value, gen);
    }

    /**
     * Walks the generator's output context up to the root looking for the nearest
     * {@link EmployeeIdAware} value. Walking instead of reading {@code gen.currentValue()}
     * directly is defensive: it tolerates Jackson configurations whose collection or wrapping
     * serializers might not propagate the bean's current value down to nested fields.
     *
     * <p><strong>Assumption:</strong> the nearest {@link EmployeeIdAware} ancestor is the owner
     * of the sensitive field — i.e. its {@code employeeId} is the correct masking subject. This
     * holds for today's flat report DTOs (each sensitive field lives directly on an
     * {@code EmployeeIdAware} record). It would break if a future DTO nests an
     * {@code EmployeeIdAware} inside another {@code EmployeeIdAware} with a different
     * {@code employeeId}, or places a {@code @SensitiveInformation} field on a non-{@code
     * EmployeeIdAware} type wrapped by an unrelated {@code EmployeeIdAware} parent: the masking
     * decision would then be evaluated against the wrong employee. Any such nesting must add a
     * masking test asserting the field is attributed to its real owner (see
     * {@code McpToolSerializationMaskingTest}).</p>
     */
    private static EmployeeIdAware findEmployeeIdAwareAncestor(JsonGenerator gen) {
        JsonStreamContext context = gen.getOutputContext();
        while (context != null) {
            Object current = context.getCurrentValue();
            if (current instanceof EmployeeIdAware employee) {
                return employee;
            }
            context = context.getParent();
        }
        return null;
    }
}
