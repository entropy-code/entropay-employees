package com.entropyteam.entropay.common;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Doc https://marmelab.com/react-admin/DataProviders.html#admin-dataprovider
 */
@Service
public class ReactAdminMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactAdminMapper.class);
    private static final String YEAR_SEARCH_TERM_KEY = "year";
    private final static String ID_FIELD = "id";
    public static final String SEARCH_TERM_KEY = "q";
    public static final String DATE_FROM_TERM_KEY = "dateFrom";
    public static final String DATE_TO_TERM_KEY = "dateTo";

    private final ObjectMapper mapper;

    public ReactAdminMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * The filter param could be:
     * getMany:            GET http://my.api.url/posts?filter={"ids":[123,456,789]}
     * or
     * getManyReference:   GET http://my.api.url/posts?filter={"author.id":345} => send target as <related
     * entity>.<field> E.x. employee.id
     * or
     * getList: 	        GET http://my.api.url/posts?sort=["title","ASC"]&range=[0, 24]&filter={"title":"bar"}
     */
    public Filter buildFilter(ReactAdminParams params, Class entityClass) {
        try {
            Map<String, List<UUID>> getByIdsFilter = new HashMap<>();
            Map<String, Object> getByFieldsFilter = new HashMap<>();
            Map<String, UUID> getByRelatedFieldsFilter = new HashMap<>();
            Map<String, LocalDate> getByDateFieldsFilter = new HashMap<>();

            if (params.getFilter() != null) {
                Map<String, Object> requestFilter = mapper.readValue(params.getFilter(), Map.class);
                for (Map.Entry<String, Object> filter : requestFilter.entrySet()) {
                    if (StringUtils.equalsIgnoreCase(filter.getKey(), ID_FIELD)) {
                        // getMany filter
                        List<String> ids = (List<String>) filter.getValue();
                        getByIdsFilter.put(filter.getKey(),
                                ids.stream().map(UUID::fromString).collect(Collectors.toList()));
                    } else if (isEntityField(entityClass, filter.getKey()) || StringUtils.equalsIgnoreCase(
                            filter.getKey(), SEARCH_TERM_KEY)) {
                        // getList filter
                        getByFieldsFilter.put(filter.getKey(), filter.getValue());
                    } else if (StringUtils.equalsIgnoreCase(filter.getKey(), YEAR_SEARCH_TERM_KEY)) {
                        String year = filter.getValue().toString();
                        LocalDate dateFrom = LocalDate.parse(year + "-01-01");
                        LocalDate dateTo = LocalDate.parse(year + "-12-31");
                        getByDateFieldsFilter.put("dateFrom", dateFrom);
                        getByDateFieldsFilter.put("dateTo", dateTo);
                    } else if (StringUtils.equalsIgnoreCase(filter.getKey(), DATE_FROM_TERM_KEY)
                               || StringUtils.equalsIgnoreCase(filter.getKey(), DATE_TO_TERM_KEY)) {
                        LocalDate date = LocalDate.parse(filter.getValue().toString());
                        getByDateFieldsFilter.put(filter.getKey(), date);
                    } else {
                        // getManyReference filter
                        String relatedEntity = StringUtils.removeEnd(filter.getKey(), "Id");
                        getByRelatedFieldsFilter.put(relatedEntity, UUID.fromString((String) filter.getValue()));
                    }
                }
            }
            return new Filter(getByIdsFilter, getByFieldsFilter, getByRelatedFieldsFilter, getByDateFieldsFilter);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestParametersException("Bad param on filters");
        }
    }

    /**
     * This method references the table in https://marmelab.com/react-admin/DataProviders.html#admin-dataprovider
     * The Range param is:
     * getList:            GET http://my.api.url/posts?sort=["title","ASC"]&range=[0, 24]&filter={"title":"bar"}
     * 0 = first value, 24 = last value => size = 25
     */
    public Pageable buildPageable(ReactAdminParams params, Class entityClass) {
        try {
            if (params.getRange() == null || params.getSort() == null) {
                return Pageable.unpaged();
            }

            List<Integer> rangeList = mapper.readValue(params.getRange(), List.class);
            Integer start = rangeList.get(0);
            Integer size = (rangeList.get(1) + 1) - start;
            Integer page = start.equals(0) ? start : Integer.divideUnsigned(start, size);

            List<String> sortList = mapper.readValue(params.getSort(), List.class);
            String sortField = sortList.get(0);
            if (!isEntityField(entityClass, sortField)) {
                // TODO: filter by related entities id
                return PageRequest.of(page, size);
            }

            return PageRequest.of(page, size, Sort.by(Direction.valueOf(sortList.get(1)), sortField));


        } catch (JsonProcessingException e) {
            throw new InvalidRequestParametersException("Bad param on range");
        }
    }

    public Filter buildReportFilter(ReactAdminParams params, Class report) {
        try {
            Map<String, Object> getByFieldsFilter = new HashMap<>();
            if (params.getFilter() != null) {
                Map<String, Object> requestFilter = mapper.readValue(params.getFilter(), Map.class);
                for (Map.Entry<String, Object> filter : requestFilter.entrySet()) {
                    if (isEntityField(report, filter.getKey())) {
                        getByFieldsFilter.put(filter.getKey(), filter.getValue());
                    }
                }
            }
            return new Filter(getByFieldsFilter);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestParametersException("Bad param on filters", e);
        }
    }

    private boolean isEntityField(Class entityClass, String field) {
        return Arrays.stream(entityClass.getDeclaredFields()).anyMatch(f -> StringUtils.equals(f.getName(), field));
    }

    public <T> Comparator<T> getComparator(ReactAdminParams params, Class<T> clazz) {

        return (object1, object2) -> {
            try {
                List<String> sortList = mapper.readValue(params.getSort(), List.class);
                String sort = sortList.get(0);
                String order = sortList.get(1);

                var field = clazz.getDeclaredField(sort);
                field.setAccessible(true);
                var fieldValue1 = field.get(object1);
                var fieldValue2 = field.get(object2);
                if (fieldValue1 instanceof Comparable && fieldValue2 instanceof Comparable) {
                    int comparison = ((Comparable) fieldValue1).compareTo(fieldValue2);
                    return "desc".equalsIgnoreCase(order) ? -comparison : comparison;
                }
                return 0;
            } catch (NoSuchFieldException | IllegalAccessException | JsonProcessingException e) {
                throw new RuntimeException("Error sorting billing report by field: " + params.getSort(), e);
            }
        };
    }

    /**
     * Creates a predicate that filters objects based on attribute values.
     * Uses reflection to access fields via getter methods.
     * Special handling is implemented for:
     * - Date range fields (startDate, endDate) - excluded from filtering
     * - Search field (q) - searches across firstName and lastName
     *
     * @param <T> The type of object to filter
     * @param params ReactAdminParams containing filter information
     * @param clazz The class of the object being filtered, needed for reflection
     * @return A predicate that tests if an object matches all filter criteria
     */

    public <T> Predicate<T> getFilter(ReactAdminParams params, Class<T> clazz) {
        Map<String, String> filters = parseFilters(params);

        return dto -> {
            // If no filters are provided, return true (include all items)
            if (filters == null || filters.isEmpty()) {
                return true;
            }

            // Check if all filter conditions are satisfied
            return filters.entrySet()
                    .stream()
                    .filter(entry -> !isDateRangeField(entry.getKey())) // Filter out startDate and endDate
                    .allMatch(entry -> {
                        String fieldName = entry.getKey();
                        String filterValue = entry.getValue();

                        // Skip empty filter values
                        if (StringUtils.isEmpty(filterValue)) {
                            return true;
                        }

                        // Special handling for 'q' (search) parameter
                        if (SEARCH_TERM_KEY.equals(fieldName)) {
                            return handleSearchQuery(dto, filterValue, clazz);
                        }

                        try {
                            // Get the method that corresponds to the field name
                            java.lang.reflect.Method method = clazz.getMethod(fieldName);
                            Object fieldValue = method.invoke(dto);

                            // Handle null values
                            if (fieldValue == null) {
                                return false;
                            }

                            String stringValue = fieldValue.toString();
                            return StringUtils.containsIgnoreCase(stringValue, filterValue);
                        } catch (NoSuchMethodException | IllegalAccessException |
                                 java.lang.reflect.InvocationTargetException e) {
                            // If the field doesn't exist or can't be accessed, ignore this filter
                            LOGGER.warn("Error filtering by field {}: {}", fieldName, e.getMessage());
                            return true;
                        }
                    });
        };
    }

    private Map<String, String> parseFilters(ReactAdminParams params) {
        Map<String, String> filters;
        try {
            filters = mapper.readValue(params.getFilter(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return filters;
    }

    /**
     * Determines if a field is a special date range field that should be filtered differently.
     *
     * @param fieldName the name of the field
     * @return true if the field is a date range field, false otherwise
     */
    private static boolean isDateRangeField(String fieldName) {
        return "startDate".equals(fieldName) || "endDate".equals(fieldName);
    }

    /**
     * Handles the special case of searching by 'q' parameter across firstName and lastName fields
     *
     * @param <T> The type of object to filter
     * @param dto The object to check
     * @param searchValue The search value to look for
     * @param clazz The class of the object being filtered
     * @return true if either firstName or lastName contains the search value, false otherwise
     */
    private static <T> boolean handleSearchQuery(T dto, String searchValue, Class<T> clazz) {
        try {
            // Check firstName
            java.lang.reflect.Method firstNameMethod = clazz.getMethod("firstName");
            Object firstNameValue = firstNameMethod.invoke(dto);

            // Check lastName
            java.lang.reflect.Method lastNameMethod = clazz.getMethod("lastName");
            Object lastNameValue = lastNameMethod.invoke(dto);

            // If either field contains the search value, return true
            return (firstNameValue != null && StringUtils.containsIgnoreCase(firstNameValue.toString(), searchValue)) ||
                   (lastNameValue != null && StringUtils.containsIgnoreCase(lastNameValue.toString(), searchValue));

        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            LOGGER.warn("Error searching firstName/lastName with query 'q': {}", e.getMessage());
            return false;
        }
    }


    public Range<Integer> getRange(ReactAdminParams params) {
        try {
            List<Integer> rangeList = mapper.readValue(params.getRange(), List.class);
            Integer start = rangeList.get(0);
            Integer end = rangeList.get(1);
            return Range.of(start, end);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing range: " + params.getRange(), e);
        }

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
            String[] array = mapper.readValue(params.getSort(), String[].class);
            String sort = array[0];
            String order = array[1];

            int[] range = mapper.readValue(params.getRange(), int[].class);
            int limit = range[1] - range[0] + 1;
            int offset = range[0];

            Map<String, String> queryParameters = mapper.readValue(params.getFilter(), Map.class);

            return new ReactAdminSqlParams(queryParameters, limit, offset, sort, order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> ReportDto<T> paginate(ReactAdminParams params, List<T> items, Class<T> clazz) {
        List<T> data = items.stream()
                .filter(getFilter(params, clazz))
                .sorted(getComparator(params, clazz))
                .toList();

        Range<Integer> range = getRange(params);
        int minimum = range.getMinimum();
        int maximum = Math.min(range.getMaximum() + 1, data.size());

        return new ReportDto<>(data.subList(minimum, maximum), data.size());
    }
}
