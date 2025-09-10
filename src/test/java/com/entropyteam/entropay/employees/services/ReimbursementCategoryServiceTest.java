package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.ReimbursementCategoryDto;
import com.entropyteam.entropay.employees.models.ReimbursementCategory;
import com.entropyteam.entropay.employees.repositories.ReimbursementCategoryRepository;

@ExtendWith(MockitoExtension.class)
public class ReimbursementCategoryServiceTest {

    @Mock
    private ReimbursementCategoryRepository repository;
    
    @Mock
    private ReactAdminMapper reactAdminMapper;

    @InjectMocks
    private ReimbursementCategoryService service;

    @Test
    void shouldCreateReimbursementCategory() {
        // Given
        ReimbursementCategoryDto dto = new ReimbursementCategoryDto(null, "Gym", "Fitness and gym membership", 
                BigDecimal.valueOf(20.00), 1);
        ReimbursementCategory entity = new ReimbursementCategory(dto);
        entity.setId(UUID.randomUUID());
        
        when(repository.save(any(ReimbursementCategory.class))).thenReturn(entity);

        // When
        ReimbursementCategoryDto result = service.create(dto);

        // Then
        assertNotNull(result);
        assertEquals("Gym", result.name());
        assertEquals("Fitness and gym membership", result.description());
        assertEquals(BigDecimal.valueOf(20.00), result.maximumAmount());
        assertEquals(1, result.periodInMonths());
        verify(repository).save(any(ReimbursementCategory.class));
    }

    @Test
    void shouldConvertEntityToDto() {
        // Given
        ReimbursementCategory entity = new ReimbursementCategory();
        entity.setId(UUID.randomUUID());
        entity.setName("Transportation");
        entity.setDescription("Travel expenses");
        entity.setMaximumAmount(BigDecimal.valueOf(100.00));
        entity.setPeriodInMonths(1);

        // When
        ReimbursementCategoryDto result = service.toDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals("Transportation", result.name());
        assertEquals("Travel expenses", result.description());
        assertEquals(BigDecimal.valueOf(100.00), result.maximumAmount());
        assertEquals(1, result.periodInMonths());
    }

    @Test
    void shouldConvertDtoToEntity() {
        // Given
        ReimbursementCategoryDto dto = new ReimbursementCategoryDto(UUID.randomUUID(), "Meals", "Work meal expenses",
                BigDecimal.valueOf(50.00), 1);

        // When
        ReimbursementCategory result = service.toEntity(dto);

        // Then
        assertNotNull(result);
        assertEquals("Meals", result.getName());
        assertEquals("Work meal expenses", result.getDescription());
        assertEquals(BigDecimal.valueOf(50.00), result.getMaximumAmount());
        assertEquals(1, result.getPeriodInMonths());
    }
    
    @Test
    void shouldSupportCustomPeriods() {
        // Given - Test 3 years (36 months)
        ReimbursementCategoryDto dto = new ReimbursementCategoryDto(UUID.randomUUID(), "Equipment", "Computer equipment",
                BigDecimal.valueOf(2000.00), 36);

        // When
        ReimbursementCategory result = service.toEntity(dto);

        // Then
        assertNotNull(result);
        assertEquals("Equipment", result.getName());
        assertEquals(BigDecimal.valueOf(2000.00), result.getMaximumAmount());
        assertEquals(36, result.getPeriodInMonths()); // 3 years
    }
}