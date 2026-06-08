package com.entropyteam.entropay.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.services.EmployeeService;
import com.entropyteam.entropay.mcp.dtos.RosterEntry;

@ExtendWith(MockitoExtension.class)
class RosterQueryServiceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private RosterQueryService service;

    @Test
    void listRosterReturnsActiveEmployeesSortedByLastName() {
        when(employeeService.findAllActive(any())).thenReturn(new PageImpl<>(List.of(
                activeEmployee("Bob", "Brown"),
                activeEmployee("Alice", "Anderson"))));

        List<RosterEntry> result = service.listRoster();

        assertEquals(2, result.size());
        assertEquals("Anderson", result.get(0).lastName());
        assertEquals("Brown", result.get(1).lastName());
    }

    private EmployeeDto activeEmployee(String firstName, String lastName) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(UUID.randomUUID());
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setActive(true);
        return dto;
    }
}
