package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.Map;
import com.entropyteam.entropay.common.ReactAdminSqlParams;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class ReportRepositoryImpl implements ReportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String SALARIES_QUERY = """
            with pi as (select pi.employee_id, string_agg(platform, ', ') as platform
                            from payment_information pi
                            where pi.deleted is not true
                            group by pi.employee_id)
                select
                    coalesce(ps.id, e.id) as rowId,
                    e.id as employeeId,
                    e.internal_id as internalId,
                    first_name as firstName,
                    last_name as lastName,
                    c.name as clientName,
                    ps.salary,
                    a.billable_rate as rate,
                    ps.modality,
                    ps.currency,
                    pi.platform,
                    country.name as country
                from employee e
                     inner join country on e.country_id = country.id
                     left join assignment a on e.id = a.employee_id and a.active = true
                     left join project p on p.id = a.project_id
                     left join client c on c.id = p.client_id
                     left join contract co on e.id = co.employee_id and co.active = true
                     left join payment_settlement ps on ps.contract_id = co.id
                     left join pi on pi.employee_id = e.id
                where
                  (e.first_name ilike :q or e.last_name ilike :q)
                  and e.active is true
                  and e.deleted is not true
                  and a.deleted is not true
                  and co.deleted is not true
            """;

    @Override
    public List<SalariesReportDto> getSalariesReport(ReactAdminSqlParams params) {

        String query = SALARIES_QUERY
                + " order by " + params.sort() + " " + params.order()
                + " limit " + params.limit() + " offset " + params.offset();

        Query nativeQuery = entityManager.createNativeQuery(query, SalariesReportDto.class);

        Map<String, String> filterMap = params.queryParameters();
        nativeQuery.setParameter("q", filterMap.containsKey("q") ? "%" + filterMap.get("q") + "%" : "%%");

        return nativeQuery.getResultList();
    }

    @Override
    public Integer getSalariesCount() {
        return entityManager.createNativeQuery(SALARIES_QUERY)
                .setParameter("q", "%%")
                .getResultList().size();
    }


}
