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
        with pi as (
            select pi.employee_id, string_agg(platform, ', ') as platform
            from payment_information pi
            where pi.deleted is not true
            group by pi.employee_id
        ),
        last_contract as (
            select c1.* from contract c1
            where c1.deleted is not true
              and c1.end_date = (
                select max(c2.end_date) from contract c2
                where c2.employee_id = c1.employee_id and c2.deleted is not true
              )
        )
        select * from (
            -- Active Employees
            select
                coalesce(ps.id, e.id) as id,
                e.id as employeeId,
                e.internal_id as internalId,
                first_name as firstName,
                last_name as lastName,
                c.name as clientName,
                ps.salary,
                ps.modality,
                ps.currency,
                pi.platform,
                country.name as country,
                e.active as active
            from employee e
                inner join country on e.country_id = country.id
                left join assignment a on e.id = a.employee_id
                    and a.active = true
                    and a.engagement_type in ('FULL_TIME', 'PART_TIME')
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

            union

            -- Inactive Employees whose last contract ended this month and year
            select
                coalesce(ps2.id, e2.id) as id,
                e2.id as employeeId,
                e2.internal_id as internalId,
                e2.first_name as firstName,
                e2.last_name as lastName,
                c2.name as clientName,
                ps2.salary,
                ps2.modality,
                ps2.currency,
                pi.platform,
                country2.name as country,
                e2.active as active
            from employee e2
                inner join country country2 on e2.country_id = country2.id
                left join last_contract lc on lc.employee_id = e2.id
                left join assignment a2 on e2.id = a2.employee_id
                    and a2.active = true
                    and a2.engagement_type in ('FULL_TIME', 'PART_TIME')
                left join project p2 on p2.id = a2.project_id
                left join client c2 on c2.id = p2.client_id
                left join payment_settlement ps2 on ps2.contract_id = lc.id
                left join pi on pi.employee_id = e2.id
            where
                (e2.first_name ilike :q or e2.last_name ilike :q)
                and e2.active is false
                and e2.deleted is not true
                and lc.end_date is not null
                and extract(month from lc.end_date) = extract(month from current_date)
                and extract(year from lc.end_date) = extract(year from current_date)
        ) as combined
    """;

    @Override
    public List<SalariesReportDto> getSalariesReport(ReactAdminSqlParams params) {

        Query nativeQuery = entityManager.createNativeQuery(SALARIES_QUERY, SalariesReportDto.class);

        Map<String, String> filterMap = params.queryParameters();
        nativeQuery.setParameter("q", filterMap.containsKey("q") ? "%" + filterMap.get("q") + "%" : "%%");

        return nativeQuery.getResultList();
    }

    @Override
    public Integer getSalariesCount(ReactAdminSqlParams params) {
        Map<String, String> filterMap = params.queryParameters();

        return entityManager.createNativeQuery(SALARIES_QUERY)
                .setParameter("q", filterMap.containsKey("q") ? "%" + filterMap.get("q") + "%" : "%%")
                .getResultList().size();
    }


}
