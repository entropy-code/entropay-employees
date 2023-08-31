package com.entropyteam.entropay.employees.common;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import com.entropyteam.entropay.common.Filter;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.ReactAdminParams;
import com.entropyteam.entropay.employees.models.Contract;

public class ReactAdminMapperTest {

    private ReactAdminMapper mapper;

    @BeforeEach
    public void setUp() {
        this.mapper = new ReactAdminMapper();
    }

    @Test
    public void TestMapperGetList() {
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter("{\"hoursPerMonth\":\"10\"}");
        params.setRange("[5,9]");
        params.setSort("[\"startDate\",\"ASC\"]");
        Class<Contract> entityClass = Contract.class;

        Filter filter = mapper.buildFilter(params, entityClass);
        Pageable pageable = mapper.buildPageable(params, entityClass);

        Assertions.assertEquals(0, filter.getGetByIdsFilter().size());
        Assertions.assertEquals(1, filter.getGetByFieldsFilter().size());
        Assertions.assertEquals(0, filter.getGetByRelatedFieldsFilter().size());
        Assertions.assertEquals("10", filter.getGetByFieldsFilter().get("hoursPerMonth"));

        Assertions.assertEquals(5, pageable.getOffset());
        Assertions.assertEquals(5, pageable.getPageSize());
        Sort sort = pageable.getSort();
        List<Order> orderList = sort.stream().toList();
        Assertions.assertEquals(1, orderList.size());
        Assertions.assertEquals("startDate", orderList.get(0).getProperty());
        Assertions.assertEquals(Direction.ASC, orderList.get(0).getDirection());
    }

    @Test
    public void TestMapperGetMany() {
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter(
                "{\"id\":[\"f3ef7dc9-8af4-4cde-af4e-9384b177ca13\", \"8a516eab-8813-4a30-8edb-c9a2fe251072\", "
                        + "\"7fdd602e-9bdf-4bdc-bb56-bb205c9ab19d\"]}");
        Class<Contract> entityClass = Contract.class;

        Filter filter = mapper.buildFilter(params, entityClass);
        Pageable pageable = mapper.buildPageable(params, entityClass);

        Assertions.assertEquals(1, filter.getGetByIdsFilter().size());
        Assertions.assertEquals(0, filter.getGetByFieldsFilter().size());
        Assertions.assertEquals(0, filter.getGetByRelatedFieldsFilter().size());
        Assertions.assertEquals(3, filter.getGetByIdsFilter().get("id").size());

        Assertions.assertFalse(pageable.isPaged());
    }

    @Test
    public void TestMapperGetManyReference() {
        ReactAdminParams params = new ReactAdminParams();
        params.setFilter("{\"employeeId\":\"69d70ee3-bfc6-4270-9bd8-79795ff093a6\"}");
        Class<Contract> entityClass = Contract.class;

        Filter filter = mapper.buildFilter(params, entityClass);

        Assertions.assertEquals(0, filter.getGetByIdsFilter().size());
        Assertions.assertEquals(0, filter.getGetByFieldsFilter().size());
        Assertions.assertEquals(1, filter.getGetByRelatedFieldsFilter().size());
        Assertions.assertEquals(UUID.fromString("69d70ee3-bfc6-4270-9bd8-79795ff093a6"),
                filter.getGetByRelatedFieldsFilter().get("employee"));
    }

    @Test
    public void TestMapperGetRelatedFieldSort() {
        ReactAdminParams params = new ReactAdminParams();
        params.setRange("[10,19]");
        params.setSort("[\"employeeId\",\"ASC\"]");
        Class<Contract> entityClass = Contract.class;

        Pageable pageable = mapper.buildPageable(params, entityClass);

        Assertions.assertEquals(10, pageable.getOffset());
        Assertions.assertEquals(10, pageable.getPageSize());
        Sort sort = pageable.getSort();
        List<Order> orderList = sort.stream().toList();
        Assertions.assertEquals(0, orderList.size());
    }
}
