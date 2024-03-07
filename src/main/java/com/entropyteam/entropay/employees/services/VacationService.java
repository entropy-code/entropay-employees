package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.UUID;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.common.exceptions.InvalidRequestParametersException;
import com.entropyteam.entropay.employees.dtos.VacationDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import com.entropyteam.entropay.employees.repositories.projections.VacationBalanceByYear;

@Service
public class VacationService extends BaseService<Vacation, VacationDto, UUID> {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int DAYS_THRESHOLD = 5;
    private static final int START_OF_POLICY = 2023;
    private static final String EXPIRED = "EXPIRED";
    private final VacationRepository vacationRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public VacationService(VacationRepository vacationRepository, ReactAdminMapper reactAdminMapper,
                           EmployeeRepository employeeRepository) {
        super(Vacation.class, reactAdminMapper);
        this.vacationRepository = vacationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected BaseRepository<Vacation, UUID> getRepository() {
        return vacationRepository;
    }

    @Override
    protected VacationDto toDTO(Vacation entity) {
        return new VacationDto(entity);
    }

    public void addVacationDebit(Employee employee, Integer totalDays) {
        List<VacationBalanceByYear> availableVacations = vacationRepository.getVacationByYear(employee.getId());
        if (CollectionUtils.isEmpty(availableVacations)
                || availableVacations.stream().mapToInt(VacationBalanceByYear::getBalance).sum() < totalDays) {
            throw new InvalidRequestParametersException("Not enough vacations days available for the employee");
        }
        availableVacations.sort(Comparator.comparing(VacationBalanceByYear::getYear));
        LOGGER.info("Adding vacation debit, employeeId: {}, credit before adding new vacation: {}",
                employee.getId(), availableVacations.stream().mapToInt(VacationBalanceByYear::getBalance).sum());
        for (VacationBalanceByYear vacation : availableVacations) {
            if (totalDays > 0) {
                Integer daysToUse = Math.min(totalDays, vacation.getBalance());
                totalDays -= daysToUse;
                Vacation vacationDebit = new Vacation();
                vacationDebit.setYear(vacation.getYear());
                vacationDebit.setCredit(0);
                vacationDebit.setDebit(daysToUse);
                vacationDebit.setEmployee(employee);
                vacationRepository.save(vacationDebit);
            }
        }
        LOGGER.info("Vacation debits added, employeeId: {}, credit after adding new vacation: {}",
                employee.getId(),
                vacationRepository.getVacationByYear(employee.getId()).stream().mapToInt(VacationBalanceByYear::getBalance).sum());
    }

    public void discountVacationDebit(Employee employee, Integer totalDays) {
        List<Vacation> vacationDebits = vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(
                employee.getId(), 0);
        if (CollectionUtils.isEmpty(vacationDebits) || vacationDebits.stream().mapToInt(Vacation::getDebit).sum() < totalDays) {
            throw new InvalidRequestParametersException("No vacations found to discount for the employee");
        }
        LOGGER.info("Discounting vacation debit, employeeId: {}, debit before discount: {}",
                employee.getId(), vacationDebits.stream().mapToInt(Vacation::getDebit).sum());
        vacationDebits.sort(Comparator.comparing(Vacation::getYear).reversed());
        for (Vacation vacation : vacationDebits) {
            if (totalDays > 0) {
                Integer debit = vacation.getDebit();
                if (debit > totalDays) {
                    vacation.setDebit(debit - totalDays);
                    totalDays = 0;
                } else {
                    vacation.setDeleted(true);
                    totalDays -= debit;
                }
                vacationRepository.save(vacation);
            }
        }
        LOGGER.info("Vacation debits discounted, employeeId: {}, debit after discount: {}",
                employee.getId(), vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(
                        employee.getId(), 0).stream().mapToInt(Vacation::getDebit).sum());
    }

    public List<Vacation> applyExpiredVacationsPolicyToEmployee(Employee employee, Integer vacationYear) {
        String vacationYearToExpire = String.valueOf(vacationYear - 1);
        List<Vacation> expiredVacations = new ArrayList<>();
        List<VacationBalanceByYear> vacations = vacationRepository.getVacationBalanceBetween(employee.getId(), START_OF_POLICY, vacationYear - 1);

        //if the most recent vacation has more than the threshold, it should always be expired
        //and then if that's the case all the other vacations with more than 0 should expire. It's impossible for the older vacations to have more days than the recent one

        for (VacationBalanceByYear balance : vacations) {
            if (balance.getYear().equals(vacationYearToExpire)) {
                if (balance.getBalance() > DAYS_THRESHOLD) {
                    expiredVacations.add(expireVacations(balance.getBalance() - DAYS_THRESHOLD, balance.getYear(), employee));
                }
            }
            else {
                if (balance.getBalance() > 0) {
                    expiredVacations.add(expireVacations(balance.getBalance(), balance.getYear(), employee));
                }
            }
        }
        return expiredVacations;
    }

    private Vacation expireVacations(int daysToExpire, String year, Employee employee){
        Vacation vacationToExpire = new Vacation();
        vacationToExpire.setYear(year);
        vacationToExpire.setCredit(0);
        vacationToExpire.setDebit(daysToExpire);
        vacationToExpire.setEmployee(employee);
        vacationToExpire.setDetails(EXPIRED);
        return vacationRepository.save(vacationToExpire);
    }

    @Override
    protected Vacation toEntity(VacationDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();

        Vacation vacation = new Vacation(entity);
        vacation.setEmployee(employee);

        return vacation;
    }

}
