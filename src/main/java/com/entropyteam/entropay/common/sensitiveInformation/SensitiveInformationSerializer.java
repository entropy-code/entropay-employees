package com.entropyteam.entropay.common.sensitiveInformation;

import java.io.IOException;
import java.util.UUID;
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
            SensitiveInformationService service = SpringContext.getBean(SensitiveInformationService.class);
            if (service != null) {
                UUID employeeId = employee.getEmployeeId();
                if (service.shouldMask(employeeId)) {
                    value = null;
                }
            }
        }

        serializers.defaultSerializeValue(value, gen);
    }

    /**
     * Walks the generator's output context up to the root looking for the nearest
     * {@link EmployeeIdAware} value. Walking instead of reading {@code gen.currentValue()}
     * directly is defensive: it tolerates Jackson configurations whose collection or wrapping
     * serializers might not propagate the bean's current value down to nested fields.
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
