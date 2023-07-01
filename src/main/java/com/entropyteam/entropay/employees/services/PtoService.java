package com.entropyteam.entropay.employees.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.PtoDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.LeaveType;
import com.entropyteam.entropay.employees.models.Pto;
import com.entropyteam.entropay.employees.models.Status;
import com.entropyteam.entropay.employees.models.Vacation;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.LeaveTypeRepository;
import com.entropyteam.entropay.employees.repositories.PtoRepository;
import com.entropyteam.entropay.employees.repositories.VacationRepository;


@Service
public class PtoService extends BaseService<Pto, PtoDto, UUID> {

    private PtoRepository ptoRepository;
    private EmployeeRepository employeeRepository;
    private LeaveTypeRepository leaveTypeRepository;
    private VacationRepository vacationRepository;

    @Autowired
    public PtoService(ReactAdminMapper mapper, PtoRepository ptoRepository, EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository, VacationRepository vacationRepository) {
        super(Pto.class, mapper);
        this.ptoRepository = ptoRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.vacationRepository = vacationRepository;
    }

    @Transactional
    @Override
    public PtoDto create(PtoDto ptoDto) {
        Pto entityToCreate = toEntity(ptoDto);
        entityToCreate.setStatus(Status.APPROVED); // For now all approved
        if (StringUtils.equalsIgnoreCase(entityToCreate.getPtoType().getName(), "Vacation")) {
            List<Vacation> availableVacations = new ArrayList<>(); // TODO read from db
            availableVacations.sort(Comparator.comparing(Vacation::getYear));
            Integer totalDays = ptoDto.days();
            for (Vacation vacation : availableVacations) {
                if (totalDays > 0) {
                    Integer daysToUse = Math.min(totalDays, vacation.getCredit());
                    totalDays -= daysToUse;
                    Vacation vacationDebit = new Vacation(vacation.getYear(), daysToUse, entityToCreate.getEmployee());
                    vacationRepository.save(vacationDebit);
                }
            }
        }
        Pto savedEntity = getRepository().save(entityToCreate);
        return toDTO(savedEntity);
    }

    @Override
    protected BaseRepository<Pto, UUID> getRepository() {
        return this.ptoRepository;
    }

    @Override
    protected PtoDto toDTO(Pto entity) {
        return new PtoDto(entity);
    }

    @Override
    protected Pto toEntity(PtoDto dto) {
        Employee employee = employeeRepository.findById(dto.employeeId()).orElse(null);
        LeaveType leaveType = leaveTypeRepository.findById(dto.leaveType()).orElse(null);
        Pto pto = new Pto(dto);
        pto.setEmployee(employee);
        pto.setPtoType(leaveType);
        return pto;
    }
}
