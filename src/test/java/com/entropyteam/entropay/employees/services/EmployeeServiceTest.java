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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    void vacationToApplyToAOldTrJrSsrEmployee() {
        //given
        Employee oldEmployee = TestUtils.anEmployee();
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

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 10);
    }

    @DisplayName("Vacations to apply to a old senior employee")
    @Test
    @Transactional
    void vacationToApplyToAOldSrEmployee() {
        //given
        Employee oldEmployee = TestUtils.anEmployee();
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

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 15);
    }

    @DisplayName("Vacations to apply to a new senior employee")
    @Test
    @Transactional
    void vacationToApplyToANewSrEmployee() {
        //given
        Employee oldEmployee = TestUtils.anEmployee();
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

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 1);
    }

    @DisplayName("Vacations to apply to a new Tr Jr Ssr employee")
    @Test
    @Transactional
    void vacationToApplyToANewTrJrSsrEmployee() {
        //given
        Employee oldEmployee = TestUtils.anEmployee();
        oldEmployee.setId(UUID.randomUUID());

        Contract activeContract = new Contract();
        activeContract.setActive(true);

        Seniority seniority = new Seniority();
        seniority.setId(UUID.randomUUID());
        seniority.setName("junior");
        seniority.setVacationDays(10);

        activeContract.setSeniority(seniority);
        activeContract.setStartDate(LocalDate.of(2023, 7, 20));
        activeContract.setEmployee(oldEmployee);

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 1);
    }

    @DisplayName("Default vacation days to employees with 2 years")
    @Test
    @Transactional
    void applyDefaultVacationDays() {
        Employee oldEmployee = TestUtils.anEmployee();
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

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(oldEmployee.getId(), currentYear)).thenReturn(false);

        //then
        int response = employeeService.applyVacationRuleToEmployee(oldEmployee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 15);

    }

    @DisplayName("Apply to employee with vacations")
    @Test
    @Transactional
    void applyToEmployeeWithVacations() {
        Employee employee = TestUtils.anEmployee();
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

        List<Contract> employeeContractList = new ArrayList<>();
        employeeContractList.add(activeContract);

        //when
        when(vacationRepository.existsVacationByEmployeeIdAndDeletedIsFalseAndYearIsLike(employee.getId(), currentYear)).thenReturn(true);

        //then
        int response = employeeService.applyVacationRuleToEmployee(employee, "2024", employeeContractList, holidaysList);

        //verify
        assertEquals(response, 0);

    }

}
