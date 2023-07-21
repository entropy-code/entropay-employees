package com.entropyteam.entropay.employees.services;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
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
                Vacation vacationDebit = new Vacation(vacation.getYear(), daysToUse, employee);
                vacationRepository.save(vacationDebit);
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
    }


    @Override
    protected Vacation toEntity(VacationDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();

        Vacation vacation = new Vacation(entity);
        vacation.setEmployee(employee);

        return vacation;
    }
}
