package com.entropyteam.entropay.common;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Component responsible for mapping React Admin parameters to SQL parameters.
 * It utilizes Jackson's ObjectMapper to deserialize JSON strings into Java objects.
 */
@Component
public class ReactAdminSqlMapper {

    private final ObjectMapper objectMapper;

    public ReactAdminSqlMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Maps ReactAdminParams to ReactAdminSqlParams by deserializing JSON strings
     * into Java objects and calculating SQL query parameters.
     *
     * @param params ReactAdminParams containing the filter, range, and sort parameters as JSON strings.
     * @return ReactAdminSqlParams with deserialized and calculated SQL query parameters.
     * @throws RuntimeException if JSON processing fails.
     */
    @SuppressWarnings("unchecked")
    public ReactAdminSqlParams map(ReactAdminParams params) {
        try {
            String[] array = objectMapper.readValue(params.getSort(), String[].class);
            String sort = array[0];
            String order = array[1];

            int[] range = objectMapper.readValue(params.getRange(), int[].class);
            int limit = range[1] - range[0] + 1;
            int offset = range[0];

            Map<String, String> queryParameters = objectMapper.readValue(params.getFilter(), Map.class);

            return new ReactAdminSqlParams(queryParameters, limit, offset, sort, order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
