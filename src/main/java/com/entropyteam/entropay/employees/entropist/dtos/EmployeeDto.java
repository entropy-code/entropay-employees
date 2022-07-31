package com.entropyteam.entropay.employees.entropist.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.entropyteam.entropay.employees.entropist.models.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

public record EmployeeDto(UUID id,
                          String firstName,
                          String lastName,
                          String email,
                          String phone,
                          String address,
                          String city,
                          String state,
                          String zip,
                          String country,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt,
                          boolean deleted) {

    public EmployeeDto(Employee employee) {
        this(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPhone(),
                employee.getAddress(), employee.getCity(), employee.getState(), employee.getZip(), employee.getCountry(),
                employee.getBirthDate(), employee.getCreatedAt(), employee.getModifiedAt(), employee.isDeleted());
    }
}
