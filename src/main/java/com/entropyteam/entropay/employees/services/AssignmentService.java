package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.auth.SecureObjectService;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.AssignmentDto;
import com.entropyteam.entropay.employees.models.Assignment;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.Role;
import com.entropyteam.entropay.employees.models.Seniority;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.RoleRepository;
import com.entropyteam.entropay.employees.repositories.SeniorityRepository;

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
            ProjectRepository projectRepository, SecureObjectService secureObjectService) {
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

        return assignment;
    }
}
