package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;

import java.time.LocalDate;
import java.util.List;

public record EmployeeReportDto(String internalId,
                                String firstName,
                                String lastName,
                                String city,
                                String role,
                                List<String> profile,
                                String seniority,
                                LocalDate startDate,
                                LocalDate endDate,
                                Boolean status,
                                String clientName,
                                String projectName,
                                List<String> technologiesNames,
                                Integer usdPayment,
                                Integer arsPayment) {

    public   EmployeeReportDto(Employee employee, List<String> profile, Contract firstContract, Contract activeContract, String client, String project, List<String> technologiesName, Integer usdPayment, Integer arsPayment) {
        this(employee.getInternalId(), employee.getFirstName(), employee.getLastName(),
                employee.getCity() != null ? employee.getCity() : "",
                activeContract != null? activeContract.getRole().getName() : null,
                profile, activeContract != null? activeContract.getSeniority().getName() : null,
                firstContract != null? firstContract.getStartDate() : null,
                activeContract != null ? activeContract.getEndDate() : null,
                employee.isActive(), client, project, technologiesName, usdPayment, arsPayment);
    }
}
