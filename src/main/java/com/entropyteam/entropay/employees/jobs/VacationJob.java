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
import com.entropyteam.entropay.employees.services.VacationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class VacationJob {
    private static final Logger LOGGER = LogManager.getLogger();

    private final EmployeeRepository employeeRepository;
    private final ContractRepository contractRepository;
    private final HolidayRepository holidayRepository;
    private final EmployeeService employeeService;
    private final VacationService vacationService;
    private final VacationRepository vacationRepository;
    private final AwsService awsService;
    private final AwsCredentialsProperties awsCredentialsProperties;

    @Autowired
    public VacationJob(EmployeeRepository employeeRepository, ContractRepository contractRepository, HolidayRepository holidayRepository,
                       EmployeeService employeeService, VacationRepository vacationRepository, VacationService vacationService,
                       AwsService awsService, AwsCredentialsProperties awsCredentialsProperties) {
        this.employeeRepository = employeeRepository;
        this.contractRepository = contractRepository;
        this.holidayRepository = holidayRepository;
        this.employeeService = employeeService;
        this.vacationService = vacationService;
        this.vacationRepository = vacationRepository;
        this.awsService = awsService;
        this.awsCredentialsProperties = awsCredentialsProperties;
    }

    //Job to execute in October and January
    @Scheduled(cron = "0 0 9 1 1,10 ?")
    @Transactional
    public void setEmployeeVacations() throws IOException {
        LOGGER.info("Starting employees vacation set job");
        Map<String, Integer> creditsSummary = findEmployeeVacations();
        String fileName = LocalDate.now() + "_EmployeesVacationsSummary.csv";
        InputStream isCredits = BuilderUtils.convertMapToCSVInputStream(creditsSummary);
        awsService.uploadFile(awsCredentialsProperties.getBucketName(), fileName, isCredits);
    }

    //job to run on October 10 minutes after the other job
    @Scheduled(cron = "0 10 9 1 10 ?")
    @Transactional
    public void setVacationsAsExpired() throws IOException {
        LOGGER.info("Starting employees expiring vacations job");
        List<Vacation> expiredVacations = expireEmployeeVacations();
        String expiredVacationsFileName = LocalDate.now() + "_EmployeesExpiredVacationsSummary.csv";
        InputStream isExpiredVacations = BuilderUtils.convertVacationToCSVInputStream(expiredVacations);
        awsService.uploadFile(awsCredentialsProperties.getBucketName(), expiredVacationsFileName, isExpiredVacations);
    }


    private Map<String, Integer> findEmployeeVacations() {
        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
        Map<String, Integer> summary = new HashMap<>();
        if (employees.isEmpty()) {
            return summary;
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        String vacationYearToAdd = currentDate.getMonthValue() == 10 ? String.valueOf(currentYear) : String.valueOf(currentYear - 1);

        List<Holiday> holidaysInPeriod = holidayRepository.findAllByDeletedIsFalse();
        List<Contract> contractsList = contractRepository.findAllByDeletedIsFalse();

        for (Employee employee : employees) {

            List<Contract> employeeContracts = contractsList.stream().filter( c -> c.getEmployee().getId() == employee.getId()).toList();
            int vacationsCreditToAdd = employeeService.applyVacationRuleToEmployee(employee, vacationYearToAdd, employeeContracts, currentDate, holidaysInPeriod);

            if (vacationsCreditToAdd > 0) {
                Vacation vacationToAdd = new Vacation();
                vacationToAdd.setYear(vacationYearToAdd);
                vacationToAdd.setCredit(vacationsCreditToAdd);
                vacationToAdd.setDebit(0);
                vacationToAdd.setEmployee(employee);
                vacationRepository.save(vacationToAdd);
                summary.put(employee.getFirstName() + " " + employee.getLastName(),vacationsCreditToAdd);
            } else {
                summary.put(employee.getFirstName() + " " + employee.getLastName(),0);
            }

        }
        return summary;
    }

    public List<Vacation> expireEmployeeVacations() {
        List<Employee> employees = employeeRepository.findAllByDeletedIsFalseAndActiveIsTrue();
        List<Vacation> summary = new ArrayList<>();
        if (CollectionUtils.isEmpty(employees)) {
            return summary;
        }
        else {
            summary = employees.stream().flatMap(employee -> vacationService.applyExpiredVacationsPolicyToEmployee(employee, LocalDate.now().getYear())
                            .stream()).collect(Collectors.toList());
        }
        return summary;
    }
}
