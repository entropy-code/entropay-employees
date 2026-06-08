package com.entropyteam.entropay.employees.common;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.Range;
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
import com.entropyteam.entropay.common.ReactAdminSqlParams;
import com.entropyteam.entropay.employees.dtos.ReportDto;
import com.entropyteam.entropay.employees.models.Contract;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReactAdminMapperTest {

    private ReactAdminMapper mapper;

    @BeforeEach
    public void setUp() {
        this.mapper = new ReactAdminMapper(new ObjectMapper());
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

    // --- Regression: programmatic callers (e.g. the MCP report tools) build a ReactAdminParams
    // without sort/range — React Admin always sends them, but those callers do not. Before the
    // null-guards, map()/paginate()/getRange()/getComparator() handed the null straight to
    // Jackson's readValue, which throws IllegalArgumentException: argument "content" is null —
    // the exact error the four report tools returned in QA. ---

    private record Row(UUID id, String name) {
    }

    @Test
    public void mapToleratesAllNullParams() {
        // The get_salaries_report tool calls map() with new ReactAdminParams() (filter/range/sort null).
        ReactAdminSqlParams sqlParams = mapper.map(new ReactAdminParams());

        Assertions.assertTrue(sqlParams.queryParameters().isEmpty());
        Assertions.assertEquals(0, sqlParams.offset());
        Assertions.assertNull(sqlParams.sort());
        Assertions.assertNull(sqlParams.order());
    }

    @Test
    public void mapToleratesFilterOnlyParams() {
        // The billing/margin/turnover tools set only the date filter, leaving sort/range null.
        ReactAdminParams params =
                new ReactAdminParams("{\"startDate\":\"2026-01-01\",\"endDate\":\"2026-05-31\"}", null, null);

        ReactAdminSqlParams sqlParams = mapper.map(params);

        Assertions.assertEquals("2026-01-01", sqlParams.queryParameters().get("startDate"));
        Assertions.assertEquals("2026-05-31", sqlParams.queryParameters().get("endDate"));
    }

    @Test
    public void paginateToleratesNullSortRangeAndFilter() {
        // salaries/billing/margin then flow through paginate(), which also parsed sort/range/filter.
        List<Row> rows = List.of(new Row(UUID.randomUUID(), "a"), new Row(UUID.randomUUID(), "b"));

        ReportDto<Row> result = mapper.paginate(new ReactAdminParams(), rows, Row.class);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.data().size());
    }

    @Test
    public void paginateWithNullParamsOnEmptyInputYieldsEmptyStructureNotNull() {
        // "report sin filas" must yield an empty structure, never null.
        ReportDto<Row> result = mapper.paginate(new ReactAdminParams(), List.of(), Row.class);

        Assertions.assertEquals(0, result.size());
        Assertions.assertNotNull(result.data());
        Assertions.assertTrue(result.data().isEmpty());
    }

    @Test
    public void getRangeWithNullRangeReturnsFullPaginateSafeRange() {
        Range<Integer> range = mapper.getRange(new ReactAdminParams());

        Assertions.assertEquals(0, range.getMinimum());
        // Stays below Integer.MAX_VALUE so paginate()'s getMaximum() + 1 does not overflow.
        Assertions.assertEquals(Integer.MAX_VALUE - 1, range.getMaximum());
    }
}
