package com.entropyteam.entropay.common;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReactAdminMapper {

    /**
     * Doc https://marmelab.com/react-admin/DataProviders.html#admin-dataprovider
     */

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public final static String ID_FIELD = "id";
    public static final String SEARCH_TERM_KEY = "q";
    public static final String YEAR_SEARCH_TERM_KEY = "year";
    public static final String DATE_FROM_TERM_KEY = "dateFrom";
    public static final String DATE_TO_TERM_KEY = "dateTo";

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
                Map<String, Object> requestFilter = MAPPER.readValue(params.getFilter(), Map.class);
                for (Map.Entry<String, Object> filter : requestFilter.entrySet()) {
                    if (StringUtils.equalsIgnoreCase(filter.getKey(), ID_FIELD)) {
                        // getMany filter
                        List<String> ids = (List<String>) filter.getValue();
                        getByIdsFilter.put(filter.getKey(),
                                ids.stream().map(UUID::fromString).collect(Collectors.toList()));
                    } else if (isEntityField(entityClass, filter.getKey()) || StringUtils.equalsIgnoreCase(filter.getKey(), SEARCH_TERM_KEY)) {
                        // getList filter
                        getByFieldsFilter.put(filter.getKey(), filter.getValue());
                    } else if (StringUtils.equalsIgnoreCase(filter.getKey(), YEAR_SEARCH_TERM_KEY)) {
                        String year = filter.getValue().toString();
                        LocalDate dateFrom = LocalDate.parse(year+"-01-01");
                        LocalDate dateTo = LocalDate.parse(year+"-12-31");
                        getByDateFieldsFilter.put("dateFrom",dateFrom);
                        getByDateFieldsFilter.put("dateTo",dateTo);
                    } else if (StringUtils.equalsIgnoreCase(filter.getKey(), DATE_FROM_TERM_KEY) || StringUtils.equalsIgnoreCase(filter.getKey(), DATE_TO_TERM_KEY) ) {
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

            List<Integer> rangeList = MAPPER.readValue(params.getRange(), List.class);
            Integer start = rangeList.get(0);
            Integer size = (rangeList.get(1) + 1) - start;
            Integer page = start.equals(0) ? start : Integer.divideUnsigned(start, size);

            List<String> sortList = MAPPER.readValue(params.getSort(), List.class);
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
                Map<String, Object> requestFilter = MAPPER.readValue(params.getFilter(), Map.class);
                for (Map.Entry<String, Object> filter : requestFilter.entrySet()) {
                    if (isEntityField(report, filter.getKey())) {
                        getByFieldsFilter.put(filter.getKey(), filter.getValue());
                    }
                }
            }
            return new Filter(getByFieldsFilter);
        }
        catch (JsonProcessingException e) {
            throw new InvalidRequestParametersException("Bad param on filters", e);
        }
    }

    private boolean isEntityField(Class entityClass, String field) {
        return Arrays.stream(entityClass.getDeclaredFields()).anyMatch(f -> StringUtils.equals(f.getName(), field));
    }
}
