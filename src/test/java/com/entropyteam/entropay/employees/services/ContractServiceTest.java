package com.entropyteam.entropay.employees.services;

import static com.entropyteam.entropay.employees.testUtils.TestUtils.aCompany;
import static com.entropyteam.entropay.employees.testUtils.TestUtils.aRole;
import static com.entropyteam.entropay.employees.testUtils.TestUtils.aSeniority;
import static com.entropyteam.entropay.employees.testUtils.TestUtils.anEndReason;
import static com.entropyteam.entropay.employees.testUtils.TestUtils.buildContract;
import static com.entropyteam.entropay.employees.testUtils.TestUtils.buildEmployee;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ContractDto;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.repositories.CompanyRepository;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.EndReasonRepository;
import com.entropyteam.entropay.employees.repositories.PaymentSettlementRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import com.entropyteam.entropay.employees.testUtils.SecurityContextFactory;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    private final UUID ACTIVE_CONTRACT_ID = UUID.fromString("6616a253-a57c-4bb2-817e-32215ab71eee");
    private final UUID EXISTENT_CONTRACT_ID = UUID.fromString("5516a253-a57c-4bb2-817e-32215ab71fff");
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private SeniorityRepository seniorityRepository;
    @Mock
    private EndReasonRepository endReasonRepository;
    @Mock
    private PaymentSettlementService paymentSettlementService;

    @Mock
    private PaymentSettlementRepository paymentSettlementRepository;
    @Mock
    private SecureObjectService secureObjectService;
    @Captor
    private ArgumentCaptor<Contract> contractCaptor;
    @InjectMocks
    @Spy
    private ContractService contractService;
    private Contract existentContract;
    private Contract activeContract;

    @BeforeEach
    public void setUp() {
        existentContract = buildContract();
        existentContract.setId(EXISTENT_CONTRACT_ID);
        activeContract = buildContract();
        activeContract.setId(ACTIVE_CONTRACT_ID);
        SecurityContextFactory.createSecurityContext();
    }

    @DisplayName("When employee has existent active contract deactivate it, create new active contract and save")
    @Test
    public void testCreateWhenEmployeeHasExistentAnActiveContract() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // when
        when(employeeRepository.findById(any())).thenReturn(Optional.of(buildEmployee()));
        when(companyRepository.findById(any())).thenReturn(Optional.of(aCompany()));
        when(roleRepository.findById(any())).thenReturn(Optional.of(aRole()));
        when(endReasonRepository.findById(any())).thenReturn(Optional.of(anEndReason()));
        when(seniorityRepository.findById(any())).thenReturn(Optional.of(aSeniority()));
        when(contractRepository.save(any())).thenReturn(existentContract);
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(any())).thenReturn(
                Optional.of(existentContract));
        when(paymentSettlementService.createPaymentsSettlement(any(), any())).thenReturn(null);
        when(paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(any())).thenReturn(
                Collections.emptyList());
        when(secureObjectService.secureObjectByRole(any(), any())).thenCallRealMethod();

        // then
        ContractDto response = contractService.create(new ContractDto(existentContract));

        assertEquals(new ContractDto(existentContract), response);

        verify(employeeRepository, times(1)).findById(eq(requestedContract.employeeId()));
        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(any());
        verify((BaseService) contractService, times(1)).create(eq(requestedContract.withActive(true)));

        assertFalse(contractCaptor.getValue().isActive());
    }

    @DisplayName("When employee doesn't have active contracts then just create and save requested")
    @Test
    public void testCreateWhenEmployeeDoesNotHaveActiveContracts() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // when
        when(employeeRepository.findById(any())).thenReturn(Optional.of(buildEmployee()));
        when(companyRepository.findById(any())).thenReturn(Optional.of(aCompany()));
        when(roleRepository.findById(any())).thenReturn(Optional.of(aRole()));
        when(endReasonRepository.findById(any())).thenReturn(Optional.of(anEndReason()));
        when(seniorityRepository.findById(any())).thenReturn(Optional.of(aSeniority()));
        when(contractRepository.save(any())).thenReturn(existentContract);
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(any())).thenReturn(Optional.empty());
        when(paymentSettlementService.createPaymentsSettlement(any(), any())).thenReturn(null);
        when(paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(any())).thenReturn(
                Collections.emptyList());
        when(secureObjectService.secureObjectByRole(any(), any())).thenCallRealMethod();

        // then
        ContractDto response = contractService.create(new ContractDto(existentContract));

        assertEquals(new ContractDto(existentContract), response);

        verify(employeeRepository, times(1)).findById(eq(requestedContract.employeeId()));
        verify(contractRepository, never()).saveAndFlush(any());
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(any());
        verify((BaseService) contractService, times(1)).create(eq(requestedContract.withActive(true)));
    }

    @DisplayName("Test create when ContractRepository throws exception")
    @Test
    public void testCreateWhenContractRepositoryThrowsException() {
        // given
        ContractDto requestedContract = new ContractDto(existentContract);

        // then
        assertThrows(RuntimeException.class, () ->
                        contractService.create(new ContractDto(existentContract)),
                "RuntimeException was expected");

        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when setActive is true and there is an existing active contract, should "
            + "deactivate existing and activate current.")
    @Test
    public void testModifyStatusWhenSetActiveIsTrueAndExistentContractIsActive() {
        // given
        boolean setActive = true;
        existentContract.setActive(false);

        Contract activated = buildContract();
        activated.setId(EXISTENT_CONTRACT_ID);
        activated.setActive(true);
        ContractDto expected = new ContractDto(activated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(any())).thenReturn(Optional.of(activeContract));
        when(contractRepository.save(any())).thenReturn(activated);
        when(paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(any())).thenReturn(
                Collections.emptyList());
        when(secureObjectService.secureObjectByRole(any(), any())).thenCallRealMethod();

        // then
        ContractDto actual = contractService.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

        Assertions.assertEquals(expected, actual);
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(
                existentContract.getEmployee().getId());
        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());
        verify(contractRepository, times(1)).save(contractCaptor.capture());
        verifyNoMoreInteractions(contractRepository);

        List<Contract> capturedValues = contractCaptor.getAllValues();
        assertTrue(capturedValues.stream().anyMatch(
                contract -> existentContract.getId().equals(contract.getId()) && contract.isActive() == setActive));
        assertTrue(capturedValues.stream()
                .anyMatch(contract -> activeContract.getId().equals(contract.getId()) && !contract.isActive()));
    }

    @DisplayName("Test modifyStatus when setActive is true and there is not active contracts, should activate current.")
    @Test
    public void testModifyStatusWhenSetActiveIsTrue() {
        // given
        boolean setActive = true;
        existentContract.setActive(false);

        Contract activated = buildContract();
        activated.setId(EXISTENT_CONTRACT_ID);
        activated.setActive(true);
        ContractDto expected = new ContractDto(activated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.save(any())).thenReturn(activated);
        when(paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(any())).thenReturn(
                Collections.emptyList());
        when(secureObjectService.secureObjectByRole(any(), any())).thenCallRealMethod();

        // then
        ContractDto actual = contractService.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

        assertEquals(expected, actual);
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verify(contractRepository, times(1)).findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(
                existentContract.getEmployee().getId());
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
                        contractService.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NO_CONTENT.value(), thrown.getStatusCode().value());
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
                        contractService.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NO_CONTENT.value(), thrown.getStatusCode().value());
        verify(contractRepository, times(1)).findById(eq(existentContract.getId()));
        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("Test modifyStatus when setActive is false should deactivate contract.")
    @Test
    public void testModifyStatusWhenSetActiveIsFalse() {
        // given
        boolean setActive = false;
        existentContract.setActive(true);

        Contract deactivated = buildContract();
        deactivated.setId(EXISTENT_CONTRACT_ID);
        deactivated.setActive(false);
        ContractDto expected = new ContractDto(deactivated);

        // when
        when(contractRepository.findById(any())).thenReturn(Optional.of(existentContract));
        when(contractRepository.save(any())).thenReturn(deactivated);
        when(paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(any())).thenReturn(
                Collections.emptyList());
        when(secureObjectService.secureObjectByRole(any(), any())).thenCallRealMethod();

        // then
        ContractDto actual = contractService.modifyStatus(EXISTENT_CONTRACT_ID, setActive);

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
                        contractService.modifyStatus(existentContract.getId(), setActive),
                "ResponseStatusException was expected");

        assertEquals(HttpStatus.NOT_FOUND.value(), thrown.getStatusCode().value());
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
                        contractService.modifyStatus(existentContract.getId(), setActive),
                "RuntimeException was expected");
        verifyNoMoreInteractions(contractRepository);
    }

    @DisplayName("when a new valid contract is created it is active")
    @Test
    public void testCreateNewActiveContract() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now().plusDays(-2));
        newActiveContract.setEndDate(LocalDate.now().plusDays(7));
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);
        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);
        assertTrue(response.isActive());
    }

    @DisplayName("when a new contract has end date before now it is inactive")
    @Test
    public void testCreateNewInactiveContract() {
        //given
        Contract newInactiveContract = buildContract();
        newInactiveContract.setActive(false);
        newInactiveContract.setStartDate(LocalDate.now().plusDays(-5));
        newInactiveContract.setEndDate(LocalDate.now().plusDays(-2));
        Employee employee = buildEmployee();
        newInactiveContract.setEmployee(employee);

        //then
        Contract response = contractService.setContractStatus(newInactiveContract);
        assertFalse(response.isActive());
    }

    @DisplayName("when a new contract starts tomorrow it is inactive")
    @Test
    public void testCreateStratsTomorrowContract() {
        //given
        Contract newInactiveContract = buildContract();
        newInactiveContract.setActive(false);
        newInactiveContract.setStartDate(LocalDate.now().plusDays(2));
        newInactiveContract.setEndDate(LocalDate.now().plusDays(7));
        Employee employee = buildEmployee();
        newInactiveContract.setEmployee(employee);

        //then
        Contract response = contractService.setContractStatus(newInactiveContract);
        assertFalse(response.isActive());
    }

    @DisplayName("when a new contract has null end date it is active")
    @Test
    public void testContractWithNullEndDateIsActive() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now().plusDays(-2));
        newActiveContract.setEndDate(null);
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);
        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);
        assertTrue(response.isActive());
    }

    @DisplayName("when a new contract start today it is active")
    @Test
    public void testContractStartsTodayIsActive() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now());
        newActiveContract.setEndDate(LocalDate.now().plusDays(7));
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);
        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);
        assertTrue(response.isActive());
    }

    @DisplayName("when a new contract start today and has null end date it is active")
    @Test
    public void testContractStartsTodayWithNullEndDateIsActive() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now());
        newActiveContract.setEndDate(null);
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);
        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);
        assertTrue(response.isActive());
    }

    @DisplayName("when a new contract finish today it is active")
    @Test
    public void testContractFinishTodayIsActive() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now().plusDays(-7));
        newActiveContract.setEndDate(LocalDate.now());
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);
        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);
        assertTrue(response.isActive());
    }


    @DisplayName("when a new contract has start date after now and end date after start date is inactive")
    @Test
    public void testContractWithStartDateAfterNowAndEndDateAfterEndDate() {
        //given
        Contract newInactiveContract = buildContract();
        newInactiveContract.setActive(false);
        newInactiveContract.setStartDate(LocalDate.now().plusDays(2));
        newInactiveContract.setEndDate(LocalDate.now().plusDays(7));
        Employee employee = buildEmployee();
        newInactiveContract.setEmployee(employee);

        //then
        Contract response = contractService.setContractStatus(newInactiveContract);
        assertFalse(response.isActive());
    }

    @DisplayName("when a new active contract is created the last active contract is set inactive")
    @Test
    public void testInactiveLastActiveContractWhenNewActiveContractIsCreated() {
        //given
        Contract newActiveContract = buildContract();
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now().plusDays(-2));
        newActiveContract.setEndDate(LocalDate.now().plusDays(7));
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);

        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);

        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());

        assertFalse(contractCaptor.getValue().isActive());
        assertTrue(response.isActive());
    }

    @DisplayName("when a new inactive contract is created the last active contract stays active")
    @Test
    public void testLastActiveContractStaysActiveWhenNewInactiveContractIsCreated() {
        //given
        ContractDto contractToCreate = new ContractDto(existentContract);
        Contract newInactiveContract = new Contract(contractToCreate);
        newInactiveContract.setActive(false);
        newInactiveContract.setStartDate(LocalDate.now().plusDays(-7));
        newInactiveContract.setEndDate(LocalDate.now().plusDays(-2));
        Employee employee = buildEmployee();
        newInactiveContract.setEmployee(employee);

        //then
        Contract response = contractService.setContractStatus(newInactiveContract);

        verify(contractRepository, times(0)).saveAndFlush(contractCaptor.capture());
        assertFalse(response.isActive());
    }

    @DisplayName("when a new contract with null end date is created the last active contract is set inactive")
    @Test
    public void testLastActiveContractSetInactiveWhenNewContractWithNullEndDateIsCreated() {
        //given
        ContractDto contractToCreate = new ContractDto(existentContract);
        Contract newActiveContract = new Contract(contractToCreate);
        newActiveContract.setActive(false);
        newActiveContract.setStartDate(LocalDate.now().plusDays(-7));
        newActiveContract.setEndDate(null);
        Employee employee = buildEmployee();
        newActiveContract.setEmployee(employee);

        //when
        when(contractRepository.findContractByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(employee.getId())).thenReturn(
                Optional.of(existentContract));
        //then
        Contract response = contractService.setContractStatus(newActiveContract);

        verify(contractRepository, times(1)).saveAndFlush(contractCaptor.capture());

        assertFalse(contractCaptor.getValue().isActive());
        assertTrue(response.isActive());
    }

}