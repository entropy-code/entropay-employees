package com.entropyteam.entropay.employees.services;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.entropyteam.entropay.employees.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignmentService extends BaseService<Assignment, AssignmentDto, UUID> {

    private final AssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final SeniorityRepository seniorityRepository;
    private final ProjectRepository projectRepository;
    private final SecureObjectService secureObjectService;


    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, EmployeeRepository employeeRepository,
                             RoleRepository roleRepository, SeniorityRepository seniorityRepository,
                             ProjectRepository projectRepository, SecureObjectService secureObjectService,
                             ReactAdminMapper reactAdminMapper) {
        super(Assignment.class, reactAdminMapper);
        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.seniorityRepository = seniorityRepository;
        this.projectRepository = projectRepository;
        this.secureObjectService = secureObjectService;
    }

    @Override
    protected BaseRepository<Assignment, UUID> getRepository() {
        return assignmentRepository;
    }

    @Transactional
    @Override
    public AssignmentDto create(AssignmentDto assignmentDto){
        Assignment entityToCreate = toEntity(assignmentDto);
        Assignment savedEntity = getRepository().save(checkActiveAssignment(entityToCreate));
        return toDTO(savedEntity);
    }


    @Override
    @Transactional
    public AssignmentDto update(UUID assignmentId,AssignmentDto assignmentDto){
        Assignment entityToUpdate = toEntity(assignmentDto);
        entityToUpdate.setId(assignmentId);
        Assignment savedEntity = getRepository().save(checkActiveAssignment(entityToUpdate));
        return toDTO(savedEntity);
    }

    @Override
    protected AssignmentDto toDTO(Assignment entity) {
        Assignment securedEntity = (Assignment) secureObjectService.secureObjectByRole(entity, getUserRole());
        return new AssignmentDto(securedEntity);
    }

    @Override
    protected Assignment toEntity(AssignmentDto entity) {
        Employee employee = employeeRepository.findById(entity.employeeId()).orElseThrow();
        Role role = roleRepository.findById(entity.roleId()).orElseThrow();
        Seniority seniority = seniorityRepository.findById(entity.seniorityId()).orElseThrow();
        Project project = projectRepository.findById(entity.projectId()).orElseThrow();

        Assignment assignment = new Assignment(entity);
        assignment.setEmployee(employee);
        assignment.setRole(role);
        assignment.setSeniority(seniority);
        assignment.setProject(project);
        assignment.setActive(true);

        return assignment;
    }
    public Assignment checkActiveAssignment(Assignment assignmentToCheck) {
        Optional<Assignment> activeAssignment = assignmentRepository.findAssignmentByEmployeeIdAndActiveIsTrueAndDeletedIsFalse(assignmentToCheck.getEmployee().getId());
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = assignmentToCheck.getStartDate();
        LocalDate endDate = assignmentToCheck.getEndDate();

        if ((endDate == null || endDate.isAfter(currentDate)) && (startDate.isBefore(currentDate) || startDate.isEqual(currentDate))) {
            assignmentToCheck.setActive(true);
            activeAssignment.ifPresent(assignment -> {
                assignment.setActive(false);
                if (assignment.getEndDate() == null) {
                    assignment.setEndDate(currentDate);
                }
                assignmentRepository.saveAndFlush(assignment);
            });
        } else {
            assignmentToCheck.setActive(false);
        }

        return assignmentToCheck;
    }

}