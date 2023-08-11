package com.entropyteam.entropay.employees.jobs;

import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class VacationJob {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final VacationRepository vacationRepository;
    private final HolidayRepository holidayRepository;
    private final EmployeeService employeeService;

    @Autowired
    public VacationJob(EmployeeRepository employeeRepository, ContractRepository contractRepository, VacationRepository vacationRepository, HolidayRepository holidayRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.vacationRepository = vacationRepository;
        this.holidayRepository = holidayRepository;
        this.employeeService = employeeService;
    }

    //Job to execute in October and January
    @Scheduled(cron = "0 0 8 1 1,8 ?")
    @Transactional
    public void addVacationForEmployees() {
        List<String> summary = findEmployeeVacations();
        summary.forEach(System.out::println);
    }

    private List<String> findEmployeeVacations() {

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        String vacationYearToAdd = (currentDate.getMonthValue() == Month.JANUARY.getValue()) ? String.valueOf(currentYear) : String.valueOf(currentYear + 1);

        //find employees according to the month of accreditation
        List<Employee> employees = LocalDate.now().getMonth().getValue() == 10 ?
                employeeRepository.findEmployeeWhereStartDateBeforeJuly() : employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
        List<String> summary = new ArrayList<>();
        if (employees.isEmpty()) {
            summary.add("No employees found to add vacations");
            return summary;
        }

        for (Employee employee : employees) {
            List<Contract> employeeContracts = contractRepository.findAllByEmployeeIdAndDeletedIsFalse(employee.getId());
            List<Holiday> holidaysInPeriod = holidayRepository.findAllByDeletedIsFalse();
            int vacationsCreditToAdd = employeeService.applyVacationRuleToEmployee(employee, vacationYearToAdd, employeeContracts, holidaysInPeriod);
            if (vacationsCreditToAdd > 0) {
                summary.add(vacationsCreditToAdd + " days will be add to employee " + employee.getFirstName() + " " + employee.getLastName() + " for year " + vacationYearToAdd);
                Vacation vacationToAdd = new Vacation();
                vacationToAdd.setYear(vacationYearToAdd);
                vacationToAdd.setCredit(vacationsCreditToAdd);
                vacationToAdd.setEmployee(employee);
                vacationRepository.save(vacationToAdd);
            } else {
                summary.add("Can't add vacations to employee " + employee.getFirstName() + " " + employee.getLastName() + " for year " + vacationYearToAdd);
            }
        }
        return summary;
    }


}
