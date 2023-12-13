package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.testUtils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    private final String currentYear = String.valueOf(LocalDate.now().getYear() + 1);
    private final List<Holiday> holidaysList = new ArrayList<>();

    @Mock
    private VacationRepository vacationRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @DisplayName("Vacations to apply to a old trainee/junior/ssr lv2 employee")
    @Test
    void vacationToApplyToAOldTrJrSsrEmployee() {
        //given
        Employee oldEmployee = TestUtils.buildEmployee();
        oldEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setVacationDays(10);
        seniority.setName("Jr");

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2023, 5, 5));
        activeContract.setEmployee(oldEmployee);

        LocalDate currentDate = LocalDate.of(2023,10,1);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 10);
    }

    @DisplayName("Vacations to apply to a old senior employee")
    @Test
    void vacationToApplyToAOldSrEmployee() {
        //given
        Employee oldEmployee = TestUtils.buildEmployee();
        oldEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setVacationDays(15);
        seniority.setName("Senior");

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2023, 5, 5));
        activeContract.setEmployee(oldEmployee);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        LocalDate currentDate = LocalDate.of(2023,10,1);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 15);
    }

    @DisplayName("Vacations to apply to a new senior employee")
    @Test
    void vacationToApplyToANewSrEmployee() {
        //given
        Employee oldEmployee = TestUtils.buildEmployee();
        oldEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("Senior 1");
        seniority.setVacationDays(15);

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2023, 7, 20));
        activeContract.setEmployee(oldEmployee);

        LocalDate currentDate = LocalDate.of(2024,1,1);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 9);
    }

    @DisplayName("Vacations to apply to a new Tr Jr Ssr employee")
    @Test
    void vacationToApplyToANewTrJrSsrEmployee() {
        //given
        Employee newEmployee = TestUtils.buildEmployee();
        newEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("junior");
        seniority.setVacationDays(10);

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2023, 7, 20));
        activeContract.setEmployee(newEmployee);

        LocalDate currentDate = LocalDate.of(2024,1,1);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(newEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(newEmployee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 6);
    }

    @DisplayName("Default vacation days to employees with 2 years")
    @Test
    void applyDefaultVacationDays() {
        Employee oldEmployee = TestUtils.buildEmployee();
        oldEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("junior");
        seniority.setVacationDays(10);

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2021, 7, 1));
        activeContract.setEmployee(oldEmployee);

        LocalDate currentDate = LocalDate.of(2023,10,1);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 15);

    }

    @DisplayName("Apply to employee with vacations")
    @Test
    void applyToEmployeeWithVacations() {
        Employee employee = TestUtils.buildEmployee();
        employee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("junior");
        seniority.setVacationDays(10);

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2021, 7, 1));
        activeContract.setEmployee(employee);

        LocalDate currentDate = LocalDate.of(2023,10,1);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(employee.getId(), currentYear)).thenReturn(true);

        //then
        int response = employeeService.applyVacationRuleToEmployee(employee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 0);

    }

    @DisplayName("Vacations to apply to a employee without contract")
    @Test
    void vacationToApplyToAEmployeeWithoutContract() {
        Employee employee = TestUtils.buildEmployee();
        employee.setId(UUID.randomUUID());

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("junior");
        seniority.setVacationDays(10);

        LocalDate currentDate = LocalDate.of(2023,10,1);
        List<Contract> employeeContractList = new ArrayList<>();

        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(employee.getId(), currentYear)).thenReturn(true);

        //then
        int response = employeeService.applyVacationRuleToEmployee(employee, "2024", employeeContractList, currentDate, holidaysList);

        //verify
        assertEquals(response, 0);

    }

}
