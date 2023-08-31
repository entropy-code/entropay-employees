package com.entropyteam.entropay.employees.jobs;

import com.entropyteam.entropay.common.BuilderUtils;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Holiday;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.HolidayRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.services.AwsCredentialsProperties;
import com.entropyteam.entropay.employees.services.AwsService;
import com.entropyteam.entropay.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VacationJob {

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final HolidayRepository holidayRepository;
    private final EmployeeService employeeService;
    private final VacationRepository vacationRepository;
    private final AwsService awsService;
    private final AwsCredentialsProperties awsCredentialsProperties;

    @Autowired
    public VacationJob(EmployeeRepository employeeRepository, ContractRepository contractRepository, HolidayRepository holidayRepository, EmployeeService employeeService, VacationRepository vacationRepository, AwsService awsService, AwsCredentialsProperties awsCredentialsProperties) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.holidayRepository = holidayRepository;
        this.employeeService = employeeService;
        this.vacationRepository = vacationRepository;
        this.awsService = awsService;
        this.awsCredentialsProperties = awsCredentialsProperties;
    }

    //Job to execute in October and January
    @Scheduled(cron = "0 0 8 1 1,10 ?")
    @Transactional
    public void setEmployeeVacations() throws IOException {
        Map<String, Integer> summary = findEmployeeVacations();
        String fileName = LocalDate.now() + "_EmployeesVacationsSummary.csv";
        InputStream is = BuilderUtils.convertMapToCSVInputStream(summary);
        awsService.uploadFile(awsCredentialsProperties.getBucketName(), fileName,is);
    }

    private Map<String, Integer> findEmployeeVacations() {
        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
        Map<String, Integer> summary = new HashMap<>();
        if (employees.isEmpty()) {
            return summary;
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        String vacationYearToAdd = currentDate.getMonthValue() == 10 ? String.valueOf(currentYear + 1) : String.valueOf(currentYear);

        List<Holiday> holidaysInPeriod = holidayRepository.findAllByDeletedIsFalse();
        List<Contract> contractsList = contractRepository.findAllByDeletedIsFalse();

        for (Employee employee : employees) {
            List<Contract> employeeContracts = contractsList.stream().filter( c -> c.getEmployee().getId() == employee.getId()).toList();
            int vacationsCreditToAdd = employeeService.applyVacationRuleToEmployee(employee, vacationYearToAdd, employeeContracts, currentDate, holidaysInPeriod);

            if (vacationsCreditToAdd > 0) {
                Vacation vacationToAdd = new Vacation();
                vacationToAdd.setYear(vacationYearToAdd);
                vacationToAdd.setCredit(vacationsCreditToAdd);
                vacationToAdd.setEmployee(employee);
                vacationRepository.save(vacationToAdd);
                summary.put(employee.getFirstName() + " " + employee.getLastName(),vacationsCreditToAdd);
            } else {
                summary.put(employee.getFirstName() + " " + employee.getLastName(),0);
            }
        }
        return summary;
    }


}
