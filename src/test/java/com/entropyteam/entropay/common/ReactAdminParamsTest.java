package com.entropyteam.entropay.common;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ReactAdminParamsTest {

    // Simple DTO record for testing purposes
    public static record TestDto(String name, String description, String firstName, String lastName, String startDate,
                                 String endDate) {

    }

    private ReactAdminMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReactAdminMapper(new ObjectMapper());
    }

    @Test
    public void getFilter_shouldReturnTrueForAllItems_whenNoFiltersProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{}", null, null);
        TestDto dto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(dto));
    }

    @Test
    public void getFilter_shouldFilterByField_whenFieldFilterProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"name\":\"Test\"}", null, null);
        TestDto matchingDto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");
        TestDto nonMatchingDto = new TestDto("Other", "Description", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(matchingDto));
        assertFalse(filter.test(nonMatchingDto));
    }

    @Test
    public void getFilter_shouldFilterByPartialMatch_whenFieldFilterProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"description\":\"script\"}", null, null);
        TestDto matchingDto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");
        TestDto nonMatchingDto = new TestDto("Test", "Other", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(matchingDto));
        assertFalse(filter.test(nonMatchingDto));
    }

    @Test
    public void getFilter_shouldSearchAcrossFirstNameAndLastName_whenSearchTermProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"q\":\"Jo\"}", null, null);
        TestDto matchingByFirstName = new TestDto("Test", "Description", "John", "Smith", "2023-01-01", "2023-12-31");
        TestDto matchingByLastName = new TestDto("Test", "Description", "Alice", "Jones", "2023-01-01", "2023-12-31");
        TestDto nonMatching = new TestDto("Test", "Description", "Alice", "Smith", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(matchingByFirstName));
        assertTrue(filter.test(matchingByLastName));
        assertFalse(filter.test(nonMatching));
    }

    @Test
    public void getFilter_shouldIgnoreDateRangeFields_whenDateRangeFieldsProvided() {
        // Given
        ReactAdminParams params =
                new ReactAdminParams("{\"startDate\":\"2023-01-01\",\"endDate\":\"2023-12-31\"}", null, null);
        TestDto dto = new TestDto("Test", "Description", "John", "Doe", "2022-01-01", "2022-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(dto));
    }

    @Test
    public void getFilter_shouldHandleNullValues_whenFieldHasNullValue() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"description\":\"Description\"}", null, null);
        TestDto dtoWithNullDescription = new TestDto("Test", null, "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertFalse(filter.test(dtoWithNullDescription));
    }

    @Test
    public void getFilter_shouldIgnoreNonExistentFields_whenNonExistentFieldProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"nonExistentField\":\"value\"}", null, null);
        TestDto dto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(dto));
    }

    @Test
    public void getFilter_shouldHandleEmptyFilterValues_whenEmptyFilterValueProvided() {
        // Given
        ReactAdminParams params = new ReactAdminParams("{\"name\":\"\"}", null, null);
        TestDto dto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(dto));
    }

    @Test
    public void getFilter_shouldHandleMultipleFilters_whenMultipleFiltersProvided() {
        // Given
        ReactAdminParams params =
                new ReactAdminParams("{\"name\":\"Test\",\"description\":\"Description\"}", null, null);
        TestDto matchingDto = new TestDto("Test", "Description", "John", "Doe", "2023-01-01", "2023-12-31");
        TestDto nonMatchingDto1 = new TestDto("Other", "Description", "John", "Doe", "2023-01-01", "2023-12-31");
        TestDto nonMatchingDto2 = new TestDto("Test", "Other", "John", "Doe", "2023-01-01", "2023-12-31");

        // When
        Predicate<TestDto> filter = mapper.getFilter(params, TestDto.class);

        // Then
        assertTrue(filter.test(matchingDto));
        assertFalse(filter.test(nonMatchingDto1));
        assertFalse(filter.test(nonMatchingDto2));
    }
}
