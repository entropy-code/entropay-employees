package com.entropyteam.entropay.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Map<String, String> getByFieldsFilter = new HashMap<>();
            Map<String, UUID> getByRelatedFieldsFilter = new HashMap<>();
            if (params.getFilter() != null) {
                Map<String, Object> requestFilter = MAPPER.readValue(params.getFilter(), Map.class);
                for (Map.Entry<String, Object> filter : requestFilter.entrySet()) {
                    if (StringUtils.equalsIgnoreCase(filter.getKey(), ID_FIELD)) {
                        // getMany filter
                        List<String> ids = (List<String>) filter.getValue();
                        getByIdsFilter.put(filter.getKey(),
                                ids.stream().map(UUID::fromString).collect(Collectors.toList()));
                    } else if (isEntityField(entityClass, filter.getKey())) {
                        // getList filter
                        getByFieldsFilter.put(filter.getKey(), (String) filter.getValue());
                    } else {
                        // getManyReference filter
                        String relatedEntity = StringUtils.removeEnd(filter.getKey(), "Id");
                        getByRelatedFieldsFilter.put(relatedEntity, UUID.fromString((String) filter.getValue()));

                    }
                }
            }
            return new Filter(getByIdsFilter, getByFieldsFilter, getByRelatedFieldsFilter);
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
            if (!isEntityField(entityClass, sortField)){
                // TODO: filter by related entities id
                return PageRequest.of(page, size);
            }

            return PageRequest.of(page, size, Sort.by(Direction.valueOf(sortList.get(1)), sortField));


        } catch (JsonProcessingException e) {
            throw new InvalidRequestParametersException("Bad param on range");
        }
    }

    private boolean isEntityField(Class entityClass, String field) {
        return Arrays.stream(entityClass.getDeclaredFields()).anyMatch(f -> StringUtils.equals(f.getName(), field));
    }
}
