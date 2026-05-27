package com.entropyteam.entropay.employees.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.calendar.CalendarService;
import com.entropyteam.entropay.employees.dtos.EmployeeDto;
import com.entropyteam.entropay.employees.models.Country;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.CountryRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmployeeInternalIdAssignmentTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PaymentInformationService paymentInformationService;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ReactAdminMapper reactAdminMapper;
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CalendarService calendarService;
    @Mock
    private EmployeeEducationService employeeEducationService;
    @Mock
    private EmployeeInternalIdGenerator internalIdGenerator;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDto buildCreateDto(String clientSuppliedInternalId) {
        EmployeeDto dto = new EmployeeDto();
        dto.setInternalId(clientSuppliedInternalId);
        dto.setFirstName("Satoshi");
        dto.setLastName("Nakamoto");
        dto.setPersonalEmail("satoshin@gmx.com");
        dto.setBirthDate(LocalDate.of(1975, 4, 5));
        dto.setCountryId(UUID.randomUUID());
        dto.setProfile(List.of());
        dto.setPaymentInformation(Collections.emptyList());
        return dto;
    }

    private Country aCountry(UUID id) {
        Country country = new Country();
        country.setId(id);
        country.setName("Japan");
        return country;
    }

    private Employee saved(String internalId, boolean active) {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setInternalId(internalId);
        employee.setFirstName("Satoshi");
        employee.setLastName("Nakamoto");
        employee.setActive(active);
        employee.setBirthDate(LocalDate.of(1975, 4, 5));
        employee.setCountry(aCountry(UUID.randomUUID()));
        employee.setRoles(new HashSet<>());
        employee.setAssignments(new HashSet<>());
        employee.setContracts(new HashSet<>());
        employee.setPaymentsInformation(new HashSet<>());
        return employee;
    }

    @DisplayName("Create ignores client-supplied internalId and uses the generated one")
    @Test
    void createIgnoresClientInternalIdAndUsesGenerated() {
        EmployeeDto dto = buildCreateDto("E999");
        when(roleRepository.findAllByDeletedIsFalseAndIdIn(any())).thenReturn(new HashSet<>());
        when(countryRepository.findById(any())).thenReturn(Optional.of(aCountry(dto.getCountryId())));
        when(internalIdGenerator.next()).thenReturn("E124");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee passed = invocation.getArgument(0);
            passed.setId(UUID.randomUUID());
            if (passed.getPaymentsInformation() == null) {
                passed.setPaymentsInformation(new HashSet<>());
            }
            if (passed.getAssignments() == null) {
                passed.setAssignments(new HashSet<>());
            }
            if (passed.getContracts() == null) {
                passed.setContracts(new HashSet<>());
            }
            return passed;
        });

        EmployeeDto result = employeeService.create(dto);

        assertEquals("E124", result.getInternalId());

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertEquals("E124", captor.getValue().getInternalId());
        verify(internalIdGenerator, times(1)).next();
    }

    @DisplayName("Two consecutive create() calls consume two consecutive sequence values")
    @Test
    void consecutiveCreatesConsumeSequenceTwice() {
        when(roleRepository.findAllByDeletedIsFalseAndIdIn(any())).thenReturn(new HashSet<>());
        when(countryRepository.findById(any())).thenAnswer(inv -> Optional.of(aCountry(inv.getArgument(0))));
        when(internalIdGenerator.next()).thenReturn("E124", "E125");
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee passed = invocation.getArgument(0);
            passed.setId(UUID.randomUUID());
            if (passed.getPaymentsInformation() == null) {
                passed.setPaymentsInformation(new HashSet<>());
            }
            if (passed.getAssignments() == null) {
                passed.setAssignments(new HashSet<>());
            }
            if (passed.getContracts() == null) {
                passed.setContracts(new HashSet<>());
            }
            return passed;
        });

        EmployeeDto firstResult = employeeService.create(buildCreateDto(null));
        EmployeeDto secondResult = employeeService.create(buildCreateDto(null));

        assertEquals("E124", firstResult.getInternalId());
        assertEquals("E125", secondResult.getInternalId());
        verify(internalIdGenerator, times(2)).next();
    }

    @DisplayName("Update preserves the persisted internalId and never consumes the sequence")
    @Test
    void updatePreservesPersistedInternalId() {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto dto = buildCreateDto("E000-tampered");
        dto.setActive(true);

        Employee persisted = saved("E007", true);
        persisted.setId(employeeId);

        when(roleRepository.findAllByDeletedIsFalseAndIdIn(any())).thenReturn(new HashSet<>());
        when(countryRepository.findById(any())).thenReturn(Optional.of(aCountry(dto.getCountryId())));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(persisted));
        when(employeeRepository.getById(employeeId)).thenReturn(persisted);
        when(contractRepository.findAllByEmployeeIdAndDeletedIsFalse(any())).thenReturn(Collections.emptyList());
        when(assignmentRepository.findAssignmentByEmployee_IdAndDeletedIsFalse(any()))
                .thenReturn(Collections.emptyList());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeDto result = employeeService.update(employeeId, dto);

        assertEquals("E007", result.getInternalId());

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertEquals("E007", captor.getValue().getInternalId());
        verify(internalIdGenerator, never()).next();
    }

    @DisplayName("peekNextInternalId delegates to the generator without consuming the sequence")
    @Test
    void peekNextInternalIdDelegates() {
        when(internalIdGenerator.peek()).thenReturn("E124");

        assertEquals("E124", employeeService.peekNextInternalId());

        verify(internalIdGenerator).peek();
        verify(internalIdGenerator, never()).next();
    }
}
