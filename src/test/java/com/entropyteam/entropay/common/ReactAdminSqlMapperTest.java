package com.entropyteam.entropay.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReactAdminSqlMapperTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ReactAdminSqlMapper reactAdminSqlMapper;

    @BeforeEach
    public void setUp() {
        reactAdminSqlMapper = new ReactAdminSqlMapper(OBJECT_MAPPER);
    }

    @Test
    public void map_shouldReturnCorrectSqlParams_whenValidReactAdminParamsProvided() {
        // Given
        String filter = "{\"q\":\"John\"}";
        String range = "[0,10]";
        String sort = "[\"name\",\"ASC\"]";
        ReactAdminParams params = new ReactAdminParams(filter, range, sort);

        // Expected
        ReactAdminSqlParams expected = new ReactAdminSqlParams(Map.of("q", "John"), 11, 0, "name", "ASC");

        // When
        ReactAdminSqlParams result = reactAdminSqlMapper.map(params);

        // Then
        assertEquals(expected, result);
    }
}