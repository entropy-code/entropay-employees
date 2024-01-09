package com.entropyteam.entropay.employees.services;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.repositories.projections.VacationBalanceByYear;

@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;
    @InjectMocks
    private VacationService vacationService;


    @Test
    void addVacationDebitTestAvailableDays() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());

        VacationBalanceByYear availableVacation1 = Mockito.mock(VacationBalanceByYear.class);
        VacationBalanceByYear availableVacation2 = Mockito.mock(VacationBalanceByYear.class);
        Mockito.when(availableVacation1.getBalance()).thenReturn(10);
        Mockito.when(availableVacation1.getYear()).thenReturn("2022");
        Mockito.when(availableVacation2.getBalance()).thenReturn(5);
        Mockito.when(availableVacation2.getYear()).thenReturn("2023");
        List<VacationBalanceByYear> availableVacations = new ArrayList<>();
        availableVacations.add(availableVacation1);
        availableVacations.add(availableVacation2);
        Mockito.when(vacationRepository.getVacationByYear(employee.getId())).thenReturn(availableVacations);

        // Run
        vacationService.addVacationDebit(employee, 12);

        // Assert
        Mockito.verify(vacationRepository, times(2)).save(Mockito.any(Vacation.class));
    }

    @Test
    void addVacationDebitTestNotEnoughAvailableDays() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());

        VacationBalanceByYear availableVacation1 = Mockito.mock(VacationBalanceByYear.class);
        Mockito.when(availableVacation1.getBalance()).thenReturn(10);
        List<VacationBalanceByYear> availableVacations = new ArrayList<>();
        availableVacations.add(availableVacation1);
        Mockito.when(vacationRepository.getVacationByYear(employee.getId())).thenReturn(availableVacations);

        // Run
        Assertions.assertThrows(InvalidRequestParametersException.class,
                () -> vacationService.addVacationDebit(employee, 12));
    }

    @Test
    void addVacationDebitTestNotAvailableDays() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        Mockito.when(vacationRepository.getVacationByYear(employee.getId())).thenReturn(Collections.emptyList());

        // Run
        Assertions.assertThrows(InvalidRequestParametersException.class,
                () -> vacationService.addVacationDebit(employee, 12));
    }

    @Test
    void discountVacationDebitTest() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());

        Vacation vacation1 = new Vacation();
        vacation1.setDebit(10);
        vacation1.setYear("2022");
        Vacation vacation2 = new Vacation();
        vacation2.setDebit(10);
        vacation2.setYear("2023");
        List<Vacation> vacationDebits = new ArrayList<>();
        vacationDebits.add(vacation1);
        vacationDebits.add(vacation2);
        Mockito.when(
                vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(employee.getId(),
                        0)).thenReturn(vacationDebits);

        // Run
        vacationService.discountVacationDebit(employee, 15);

        // Assert
        Mockito.verify(vacationRepository, times(2)).save(Mockito.any(Vacation.class));
    }

    @Test
    void discountVacationDebitTestNotEnoughAvailableDays() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());

        Vacation vacation1 = new Vacation();
        vacation1.setDebit(10);
        Vacation vacation2 = new Vacation();
        vacation2.setDebit(10);
        List<Vacation> vacationDebits = new ArrayList<>();
        vacationDebits.add(vacation1);
        vacationDebits.add(vacation2);
        Mockito.when(
                vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(employee.getId(),
                        0)).thenReturn(vacationDebits);

        // Run
        Assertions.assertThrows(InvalidRequestParametersException.class,
                () -> vacationService.discountVacationDebit(employee, 25));
    }

    @Test
    void DiscountVacationDebitTestNotAvailableDays() {
        // Config
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        Mockito.when(
                vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(employee.getId(),
                        0)).thenReturn(Collections.emptyList());

        // Run
        Assertions.assertThrows(InvalidRequestParametersException.class,
                () -> vacationService.discountVacationDebit(employee, 12));
    }
}
