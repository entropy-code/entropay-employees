package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.employees.dtos.ReimbursementDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Reimbursement;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementCategoryRepository;
import com.entropyteam.entropay.employees.repositories.ReimbursementRepository;

@ExtendWith(MockitoExtension.class)
public class ReimbursementServiceTest {

    @Mock
    private ReimbursementRepository repository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ReimbursementCategoryRepository categoryRepository;

    @InjectMocks
    private ReimbursementService service;

    @Test
    void shouldCreateReimbursement() {
        // Given
        UUID employeeId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        ReimbursementCategory category = new ReimbursementCategory();
        category.setId(categoryId);
        category.setName("Gym");

        ReimbursementDto dto = new ReimbursementDto(null, employeeId,
                categoryId, BigDecimal.valueOf(50.00), LocalDate.now(), "Monthly gym membership");

        Reimbursement entity = new Reimbursement(dto);
        entity.setEmployee(employee);
        entity.setCategory(category);
        entity.setId(UUID.randomUUID());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(repository.save(any(Reimbursement.class))).thenReturn(entity);

        // When
        ReimbursementDto result = service.create(dto);

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50.00), result.amount());
        assertEquals("Monthly gym membership", result.comment());
        verify(repository).save(any(Reimbursement.class));
    }

    @Test
    void shouldConvertEntityToDto() {
        // Given
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("Jane");
        employee.setLastName("Smith");

        ReimbursementCategory category = new ReimbursementCategory();
        category.setId(UUID.randomUUID());
        category.setName("Transportation");

        Reimbursement entity = new Reimbursement();
        entity.setId(UUID.randomUUID());
        entity.setEmployee(employee);
        entity.setCategory(category);
        entity.setAmount(BigDecimal.valueOf(25.50));
        entity.setDate(LocalDate.of(2023, 12, 15));
        entity.setComment("Bus fare");

        // When
        ReimbursementDto result = service.toDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(employee.getId(), result.employeeId());
        assertEquals(category.getId(), result.categoryId());
        assertEquals(BigDecimal.valueOf(25.50), result.amount());
        assertEquals(LocalDate.of(2023, 12, 15), result.date());
        assertEquals("Bus fare", result.comment());
    }

    @Test
    void shouldConvertDtoToEntity() {
        // Given
        UUID employeeId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setId(employeeId);

        ReimbursementCategory category = new ReimbursementCategory();
        category.setId(categoryId);

        ReimbursementDto dto = new ReimbursementDto(UUID.randomUUID(), employeeId,
                categoryId, BigDecimal.valueOf(15.00), LocalDate.now(), "Team lunch");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Reimbursement result = service.toEntity(dto);

        // Then
        assertNotNull(result);
        assertEquals(employee, result.getEmployee());
        assertEquals(category, result.getCategory());
        assertEquals(BigDecimal.valueOf(15.00), result.getAmount());
        assertEquals("Team lunch", result.getComment());
    }
}