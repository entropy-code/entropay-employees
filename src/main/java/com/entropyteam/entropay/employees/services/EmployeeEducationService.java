package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.EmployeeEducationDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.EmployeeEducation;
import com.entropyteam.entropay.employees.repositories.EmployeeEducationRepository;

@Service
public class EmployeeEducationService extends BaseService<EmployeeEducation, EmployeeEducationDto, UUID> {

    private final EmployeeEducationRepository employeeEducationRepository;

    @Autowired
    public EmployeeEducationService(EmployeeEducationRepository employeeEducationRepository,
            ReactAdminMapper reactAdminMapper) {
        super(EmployeeEducation.class, reactAdminMapper);
        this.employeeEducationRepository = employeeEducationRepository;
    }

    @Override
    protected BaseRepository<EmployeeEducation, UUID> getRepository() {
        return employeeEducationRepository;
    }

    @Override
    protected EmployeeEducationDto toDTO(EmployeeEducation entity) {
        return new EmployeeEducationDto(entity);
    }

    @Override
    protected EmployeeEducation toEntity(EmployeeEducationDto dto) {
        return new EmployeeEducation(dto);
    }

    public void createEducation(EmployeeEducationDto educationDto, Employee employee) {
        if (educationDto == null) {
            return;
        }
        EmployeeEducation education = new EmployeeEducation(educationDto);
        education.setEmployee(employee);
        employeeEducationRepository.save(education);
    }

    public void updateEducation(EmployeeEducationDto educationDto, Employee employee) {
        if (educationDto == null) {
            return;
        }
        
        var existingEducation = employeeEducationRepository
                .findByEmployeeIdAndDeletedFalse(employee.getId());
        
        if (existingEducation.isPresent()) {
            EmployeeEducation education = existingEducation.get();
            education.setEducationLevelId(educationDto.educationLevelId());
            education.setLevelOther(educationDto.levelOther());
            education.setInstitution(educationDto.institution());
            education.setDegree(educationDto.degree());
            employeeEducationRepository.save(education);
        } else {
            EmployeeEducation newEducation = new EmployeeEducation(educationDto);
            newEducation.setEmployee(employee);
            employeeEducationRepository.save(newEducation);
        }
    }
}
