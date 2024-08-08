package com.entropyteam.entropay.employees.dtos;

import com.entropyteam.entropay.employees.models.Children;
import com.entropyteam.entropay.employees.models.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ChildrenDto(UUID id,
                          @NotNull(message = "Employee Id is mandatory")
                          UUID employeeId,
                          @NotNull(message = "First Name is mandatory")
                          String firstName,
                          @NotNull(message = "Last Name is mandatory")
                          String lastName,
                          @NotNull(message = "Gender is mandatory")
                          Gender gender,
                          @JsonFormat(pattern = "yyyy-MM-dd")
                          LocalDate birthDate,
                          boolean deleted,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                          LocalDateTime modifiedAt
) {

    public ChildrenDto(Children children) {
        this(children.getId(), children.getEmployee().getId(), children.getFirstName(), children.getLastName(), children.getGender(), children.getBirthDate(),
                 children.isDeleted(), children.getCreatedAt(), children.getModifiedAt());
    }
}
