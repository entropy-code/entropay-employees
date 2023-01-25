package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.ContractType;
import com.entropyteam.entropay.employees.models.Currency;
import com.entropyteam.entropay.employees.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.entropyteam.entropay.employees.testUtils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {
    private final UUID ACTIVE_CONTRACT_ID = UUID.fromString("6616a253-a57c-4bb2-817e-32215ab71eee");
    private final UUID EXISTENT_CONTRACT_ID = UUID.fromString("5516a253-a57c-4bb2-817e-32215ab71fff");
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    ContractRepository contractRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    SeniorityRepository seniorityRepository;
    @Captor
    ArgumentCaptor<Contract> contractCaptor;
    @InjectMocks
    @Spy
    ContractService underTest;
    private Contract existentContract;
    private Contract activeContract;

    @BeforeEach
    public void setUp() {
        existentContract = aContract();
        existentContract.setId(EXISTENT_CONTRACT_ID);
        existentContract.setContractType(ContractType.CONTRACTOR);
        activeContract = aContract();
        activeContract.setId(ACTIVE_CONTRACT_ID);
        activeContract.setContractType(ContractType.CONTRACTOR);
    }

    @DisplayName("When employee has existent active contract deactivate it, create new active contract and save")
    @Test
    public void testCreateWhenEmployeeHasExistentAnActiveContract() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // when
        when(employeeRepository.findById(any())).thenReturn(Optional.of(anEmployee()));
        when(companyRepository.findById(any())).thenReturn(Optional.of(aCompany()));
        when(roleRepository.findById(any())).thenReturn(Optional.of(aRole()));
        when(seniorityRepository.findById(any())).thenReturn(Optional.of(aSeniority()));
        when(contractRepository.save(any())).thenReturn(existentContract);
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrue(any())).thenReturn(Optional.of(existentContract));

        // then
        ContractDto response = underTest.create(new ContractDto(existentContract));

        assertEquals(new ContractDto(existentContract), response);

        verify(employeeRepository, times(1)).findById(eq(requestedContract.employeeId()));
        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrue(any());
        verify((BaseService) underTest, times(1)).create(eq(requestedContract.withActive(true)));

        assertFalse(contractCaptor.getValue().isActive());
    }

    @DisplayName("When employee doesn't have active contracts then just create and save requested")
    @Test
    public void testCreateWhenEmployeeDoesNotHaveActiveContracts() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // when
        when(employeeRepository.findById(any())).thenReturn(Optional.of(anEmployee()));
        when(companyRepository.findById(any())).thenReturn(Optional.of(aCompany()));
        when(roleRepository.findById(any())).thenReturn(Optional.of(aRole()));
        when(seniorityRepository.findById(any())).thenReturn(Optional.of(aSeniority()));
        when(contractRepository.save(any())).thenReturn(existentContract);
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrue(any())).thenReturn(Optional.empty());

        // then
        ContractDto response = underTest.create(new ContractDto(existentContract));

        assertEquals(new ContractDto(existentContract), response);

        verify(employeeRepository, times(1)).findById(eq(requestedContract.employeeId()));
        verify(contractRepository, never()).saveAndFlush(any());
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrue(any());
        verify((BaseService) underTest, times(1)).create(eq(requestedContract.withActive(true)));
    }

    @DisplayName("Test create when ContractRepository throws exception")
    @Test
    public void testCreateWhenContractRepositoryThrowsException() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrue(any())).thenThrow(new RuntimeException("Test exception thrown!!"));

        // then
        assertThrows(RuntimeException.class, () ->
                        underTest.create(new ContractDto(existentContract)),
                "RuntimeException was expected");

        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when setActive is true and there is an existing active contract, should deactivate existing and activate current.")
    @Test
    public void testModifyStatusWhenSetActiveIsTrueAndExistentContractIsActive() {
        // given
        boolean setActive = true;
        existentContract.setActive(false);

        Contract activated = aContract();
        activated.setId(EXISTENT_CONTRACT_ID);
        activated.setActive(true);
        activated.setContractType(ContractType.CONTRACTOR);
        ContractDto expected = new ContractDto(activated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrue(any())).thenReturn(Optional.of(activeContract));
        when(contractRepository.save(any())).thenReturn(activated);

        // then
        ContractDto actual = underTest.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

        Assertions.assertEquals(expected, actual);
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrue(existentContract.getEmployee().getId());
        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());
        verify(contractRepository, times(1)).save(contractCaptor.capture());
        verifyNoMoreInteractions(contractRepository);

        List<Contract> capturedValues = contractCaptor.getAllValues();
        assertTrue(capturedValues.stream().anyMatch(contract -> existentContract.getId().equals(contract.getId()) && contract.isActive() == setActive));
        assertTrue(capturedValues.stream().anyMatch(contract -> activeContract.getId().equals(contract.getId()) && !contract.isActive()));
    }

    @DisplayName("Test modifyStatus when setActive is true and there is not active contracts, should activate current.")
    @Test
    public void testModifyStatusWhenSetActiveIsTrue() {
        // given
        boolean setActive = true;
        existentContract.setActive(false);

        Contract activated = aContract();
        activated.setId(EXISTENT_CONTRACT_ID);
        activated.setActive(true);
        activated.setContractType(ContractType.CONTRACTOR);
        ContractDto expected = new ContractDto(activated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.save(any())).thenReturn(activated);

        // then
        ContractDto actual = underTest.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

        assertEquals(expected, actual);
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrue(existentContract.getEmployee().getId());
        verify(contractRepository, times(1)).save(contractCaptor.capture());
        verifyNoMoreInteractions(contractRepository);

        List<Contract> capturedValues = contractCaptor.getAllValues();
        assertTrue(capturedValues.stream().anyMatch(contract -> existentContract.getId().equals(contract.getId())
                && contract.isActive() == setActive));
    }

    @DisplayName("Test modifyStatus when setActive is true and contract is already active, should throw exception")
    @Test
    public void testModifyStatusWhenSetActiveIsTrueAndContractIsAlreadyActive() {
        // given
        boolean setActive = true;
        existentContract.setActive(true);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));

        // then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                        underTest.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NO_CONTENT, thrown.getStatus());
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when setActive is false and contract is already inactive, should throw exception")
    @Test
    public void testModifyStatusWhenSetActiveIsFalseAndContractIsAlreadyInactive() {
        // given
        boolean setActive = false;
        existentContract.setActive(false);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));

        // then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                        underTest.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NO_CONTENT, thrown.getStatus());
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when setActive is false should deactivate contract.")
    @Test
    public void testModifyStatusWhenSetActiveIsFalse() {
        // given
        boolean setActive = false;
        existentContract.setActive(true);

        Contract deactivated = aContract();
        deactivated.setId(EXISTENT_CONTRACT_ID);
        deactivated.setActive(false);
        deactivated.setContractType(ContractType.CONTRACTOR);
        ContractDto expected = new ContractDto(deactivated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.save(any())).thenReturn(deactivated);

        // then
        ContractDto actual = underTest.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

        Assertions.assertEquals(expected, actual);
        assertFalse(actual.active());
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verify(contractRepository, times(1)).save(contractCaptor.capture());
        verifyNoMoreInteractions(contractRepository);

        List<Contract> capturedValues = contractCaptor.getAllValues();
        assertTrue(capturedValues.stream().anyMatch(contract -> existentContract.getId().equals(contract.getId())
                && contract.isActive() == setActive));
    }

    @DisplayName("Test modifyStatus when contract does not exists")
    @Test
    public void testModifyStatusWhenContractDoesntExists() {
        // given
        boolean setActive = false;

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.empty());

        // then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                        underTest.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when ContractRepository throws exception")
    @Test
    public void testModifyStatusWhenContractRepositoryThrowsException() {
        // given
        boolean setActive = false;

        // when
        when(contractRepository.findById(any())).thenThrow(new RuntimeException("Test exception thrown!!"));

        // then
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                        underTest.modifyStatus(existentContract.getId(), setActive),
                "RuntimeException was expected");
        verifyNoMoreInteractions(contractRepository);
    }
}