package com.entropyteam.entropay.employees.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.entropyteam.entropay.common.BaseEntity;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.fasterxml.jackson.annotation.JsonFormat;

public record EmployeeDto(UUID id,
                          @NotNull(message = "Internal ID is mandatory") String internalId,
                          @NotNull(message = "First Name is mandatory") String firstName,
                          @NotNull(message = "Last Name is mandatory") String lastName,
                          @Email @NotNull(message = "Email is mandatory") String personalEmail,
                          String phoneNumber,
                          String mobileNumber,
                          String address,
                          String city,
                          String state,
                          String zip,
                          String country,
                          @NotNull(message = "Personal Number is mandatory") String personalNumber,
                          String taxId,
                          String emergencyContactFullName,
                          String emergencyContactPhone,
                          List<UUID> profile,
                          String notes,
                          String healthInsurance,
                          List<PaymentInformationDto> paymentInformation,
                          List<UUID> technologies,
                          @Email String labourEmail,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime modifiedAt,
                          boolean deleted,
                          String project,
                          String client,
                          String role,
                          UUID lastAssignmentId,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                          @NotNull(message = "Active state is mandatory") boolean active,
                          Integer availableDays,
                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate nearestPto,
                          String timeSinceStart) {

    public EmployeeDto(Employee employee, List<PaymentInformation> paymentInformationList, Assignment lastAssignment,
            Contract firstContract, Integer availableDays, Contract activeContract, LocalDate nearestPto, String timeSinceStart) {
        this(employee.getId(), employee.getInternalId(), employee.getFirstName(), employee.getLastName(),
                employee.getPersonalEmail(), employee.getPhoneNumber(), employee.getMobileNumber(),
                employee.getAddress(), employee.getCity(), employee.getState(), employee.getZip(),
                employee.getCountry(), employee.getPersonalNumber(), employee.getTaxId(),
                employee.getEmergencyContactFullName(), employee.getEmergencyContactPhone(),
                employee.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toList()), employee.getNotes(),
                employee.getHealthInsurance(), paymentInformationList.stream().map(PaymentInformationDto::new).toList(),
                employee.getTechnologies().stream().map(BaseEntity::getId).collect(Collectors.toList()),
                employee.getLabourEmail(), employee.getBirthDate(), employee.getCreatedAt(), employee.getModifiedAt(),
                employee.isDeleted(),
                lastAssignment != null ? lastAssignment.getProject().getName() : "-",
                lastAssignment != null ? lastAssignment.getProject().getClient().getName() : "-",
                lastAssignment != null ? lastAssignment.getRole().getName() : "-",
                lastAssignment != null ? lastAssignment.getId() : null,
                firstContract != null ? firstContract.getStartDate() : null,
                activeContract != null ? activeContract.getEndDate() : null,
                employee.isActive(),
                availableDays != null ? availableDays : 0,
                nearestPto, timeSinceStart);
    }
}
