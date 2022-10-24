package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.models.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

public record EmployeeDto(UUID id,
                          String internalId,
                          String firstName,
                          String lastName,
                          String personalEmail,
                          String phone,
                          String address,
                          String city,
                          String state,
                          String zip,
                          String country,
                          String personalNumber,
                          String taxId,
                          String emergencyContactFullName,
                          String emergencyContactPhone,
                          List<UUID> profile,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt,
                          boolean deleted) {

    public EmployeeDto(Employee employee) {
        this(employee.getId(), employee.getInternalId(), employee.getFirstName(),
                employee.getLastName(), employee.getPersonalEmail(), employee.getPhone(), employee.getAddress(),
                employee.getCity(), employee.getState(), employee.getZip(), employee.getCountry(),employee.getPersonalNumber(),employee.getTaxId(),employee.getEmergencyContactFullName(),
                employee.getEmergencyContactPhone(),employee.getRolesList().stream().map(BaseEntity::getId).collect(Collectors.toList()), employee.getBirthDate(), employee.getCreatedAt(), employee.getModifiedAt(),
                employee.isDeleted()
                );
    }
}
