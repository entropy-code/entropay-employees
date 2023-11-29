package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
        for (VacationBalanceByYear vacation : availableVacations) {
            if (totalDays > 0) {
                Integer daysToUse = Math.min(totalDays, vacation.getBalance());
                totalDays -= daysToUse;
                Vacation vacationDebit = new Vacation();
                vacationDebit.setYear(vacation.getYear());
                vacationDebit.setCredit(0);
                vacationDebit.setDebit(daysToUse);
                vacationDebit.setEmployee(employee);
                Vacation savedEntity = vacationRepository.save(vacationDebit);
                LOGGER.info("Vacation debit created, employeeId: {}, year: {}, available vacation days before debit: {}, available vacation days after debit: {}",
                        savedEntity.getEmployee().getId(), savedEntity.getYear(), vacation.getBalance(), vacationRepository.getAvailableDays(employee.getId()));
            }
        }
    }

    public void discountVacationDebit(Employee employee, Integer totalDays) {
        List<Vacation> vacationDebits = vacationRepository.getVacationByEmployeeIdAndDeletedIsFalseAndCreditOrderByYearDesc(
                employee.getId(), 0);
        if (CollectionUtils.isEmpty(vacationDebits) || vacationDebits.stream().mapToInt(Vacation::getDebit).sum() < totalDays) {
            throw new InvalidRequestParametersException("No vacations found to discount for the employee");
        }

        vacationDebits.sort(Comparator.comparing(Vacation::getYear).reversed());
        for (Vacation vacation : vacationDebits) {
            Integer availableDays = vacationRepository.getAvailableDays(employee.getId());
            if (totalDays > 0) {
                Integer debit = vacation.getDebit();
                if (debit > totalDays) {
                    vacation.setDebit(debit - totalDays);
                    totalDays = 0;
                } else {
                    vacation.setDeleted(true);
                    totalDays -= debit;
                }
                Vacation savedEntity = vacationRepository.save(vacation);
                LOGGER.info("Vacation debit discounted, employeeId: {}, year: {}, : available days before discount: {}, available days after discount: {}",
                        savedEntity.getEmployee().getId(), savedEntity.getYear(), availableDays, vacationRepository.getAvailableDays(employee.getId()));
            }
        }
    }


    @Override
    protected Vacation toEntity(VacationDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();

        Vacation vacation = new Vacation(entity);
        vacation.setEmployee(employee);

        return vacation;
    }
}
