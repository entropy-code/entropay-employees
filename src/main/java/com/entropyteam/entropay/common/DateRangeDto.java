package com.entropyteam.entropay.common;

import java.time.LocalDate;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DateRangeDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    public DateRangeDto(ReactAdminSqlParams params) {
        try {
            this.startDate = LocalDate.parse(params.queryParameters().get("startDate"));
            this.endDate = LocalDate.parse(params.queryParameters().get("endDate"));
        } catch (Exception e) {
            throw new InvalidRequestParametersException("Invalid date range parameters.");
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
