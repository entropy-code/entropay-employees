package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import com.entropyteam.entropay.common.ReactAdminSqlParams;
import com.entropyteam.entropay.employees.dtos.SalariesReportDto;

public interface ReportRepository {

    List<SalariesReportDto> getSalariesReport(ReactAdminSqlParams params);

    Integer getSalariesCount(ReactAdminSqlParams params);
}
