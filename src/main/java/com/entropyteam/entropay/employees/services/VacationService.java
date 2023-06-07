package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.VacationDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
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

    @Override
    protected Vacation toEntity(VacationDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();

        Vacation vacation = new Vacation();
        vacation.setEmployee(employee);

        return vacation;
    }
}
