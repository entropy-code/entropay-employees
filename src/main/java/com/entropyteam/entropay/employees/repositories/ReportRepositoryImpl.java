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
            select pi.employee_id,
            string_agg(platform, ', ') as platform
            from payment_information pi
            where pi.deleted is not true
            group by pi.employee_id
        ),
        last_contract as (
            select *
            from (
                select c.*,
                   row_number() over (
                        partition by c.employee_id
                        order by c.end_date desc nulls last
                   ) as rn
                from contract c
                where c.deleted is not true
            ) t
            where rn = 1
        )

        select
            coalesce(ps.id, e.id) as id,
            e.id as employeeId,
            e.internal_id as internalId,
            e.first_name as firstName,
            e.last_name as lastName,
            cl.name as clientName,
            ps.salary,
            ps.modality,
            ps.currency,
            pi.platform,
            country.name as country,
            e.active as active
        from employee e
            inner join country on e.country_id = country.id

            left join assignment a
                on e.id = a.employee_id
                and a.active = true
                and a.engagement_type in ('FULL_TIME', 'PART_TIME')
                and a.deleted is not true

            left join project p on p.id = a.project_id
            left join client cl on cl.id = p.client_id

            left join contract co
                on e.id = co.employee_id
                and co.active = true
                and co.deleted is not true

            left join last_contract lc
                on lc.employee_id = e.id

            left join payment_settlement ps
                on ps.contract_id = coalesce(co.id, lc.id)
                and ps.deleted is not true

            left join pi on pi.employee_id = e.id

        where
            (e.first_name ilike :q or e.last_name ilike :q)
            and e.deleted is not true
            and (
                -- All active employees
                e.active is true

                -- Inactive employees whose last contract ended this month
                or
                (
                    e.active is false
                    and lc.end_date is not null
                    and lc.end_date >= date_trunc('month', current_date)
                    and lc.end_date < date_trunc('month', current_date) + interval '1 month'
                )
            )
        ;
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
