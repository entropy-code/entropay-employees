package com.entropyteam.entropay.common.sensitiveInformation;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import com.entropyteam.entropay.common.SpringContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SensitiveInformationSerializer extends JsonSerializer<Object> {

    private final SensitiveInformationService sensitiveInformationService;

    public SensitiveInformationSerializer() {
        // Get the bean from Spring context at runtime since this serializer is not a Spring bean
        this.sensitiveInformationService =
                Objects.requireNonNull(SpringContext.getBean(SensitiveInformationService.class));
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Object parent = gen.currentValue();

        if (parent instanceof EmployeeIdAware employee) {
            UUID employeeId = employee.getEmployeeId();
            if (sensitiveInformationService.shouldMask(employeeId)) {
                value = null;
            }
        }

        serializers.defaultSerializeValue(value, gen);
    }
}
