package com.entropyteam.entropay.employees.services;

import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.OvertimeDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Overtime;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.OvertimeRepository;

@Service
public class OvertimeService extends BaseService<Overtime, OvertimeDto, UUID> {

    private final OvertimeRepository overtimeRepository;
    private final EmployeeRepository employeeRepository;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public OvertimeService(OvertimeRepository overtimeRepository,
            ReactAdminMapper reactAdminMapper, EmployeeRepository employeeRepository,
            AssignmentRepository assignmentRepository) {
        super(Overtime.class, reactAdminMapper);
        this.overtimeRepository = Objects.requireNonNull(overtimeRepository);
        this.employeeRepository = employeeRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    @Override
    public OvertimeDto create(OvertimeDto overtimeDto) {

        Overtime entityToCreate = toEntity(overtimeDto);
        Overtime savedEntity = getRepository().save(entityToCreate);
        return toDTO(savedEntity);
    }

    @Transactional
    @Override
    public OvertimeDto update(UUID id, OvertimeDto overtimeDto) {
        Overtime entityToUpdate = toEntity(overtimeDto);
        entityToUpdate.setId(id);
        Overtime savedEntity = getRepository().save(entityToUpdate);
        return toDTO(savedEntity);

    }

    @Transactional
    @Override
    public OvertimeDto delete(UUID id) {
        Overtime overtime = getRepository().findById(id).orElseThrow();
        overtime.setDeleted(true);
        return toDTO(overtime);
    }

    protected BaseRepository<Overtime, UUID> getRepository() {
        return overtimeRepository;
    }

    @Override
    protected OvertimeDto toDTO(Overtime entity) {
        return new OvertimeDto(entity);
    }

    @Override
    protected Overtime toEntity(OvertimeDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();
        Assignment assignment = assignmentRepository.findById(entity.assignmentId()).orElseThrow();
        Overtime overtime = new Overtime(entity);
        overtime.setEmployee(employee);
        overtime.setAssignment(assignment);

        return overtime;
    }

}
