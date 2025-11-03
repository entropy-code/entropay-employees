package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.SkillDto;
import com.entropyteam.entropay.employees.models.Skill;
import com.entropyteam.entropay.employees.repositories.EmployeeRepository;
import com.entropyteam.entropay.employees.repositories.SkillRepository;
import com.entropyteam.entropay.employees.repositories.TechnologyRepository;

@Service
public class SkillService extends BaseService<Skill, SkillDto, UUID> {

    private final SkillRepository skillRepository;
    private final EmployeeRepository employeeRepository;
    private final TechnologyRepository technologyRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository, ReactAdminMapper reactAdminMapper,
            EmployeeRepository employeeRepository, TechnologyRepository technologyRepository) {
        super(Skill.class, reactAdminMapper);
        this.skillRepository = skillRepository;
        this.employeeRepository = employeeRepository;
        this.technologyRepository = technologyRepository;
    }

    @Override
    protected BaseRepository<Skill, UUID> getRepository() {
        return skillRepository;
    }

    @Override
    protected SkillDto toDTO(Skill entity) {
        return new SkillDto(entity);
    }

    @Override
    protected Skill toEntity(SkillDto entity) {
        Skill skill = new Skill(entity);
        skill.setEmployee(employeeRepository.findById(entity.employeeId()).orElseThrow(
                () -> new IllegalArgumentException("Employee with id " + entity.employeeId() + " not found")));
        skill.setTechnology(technologyRepository.findById(entity.technologyId()).orElseThrow(
                () -> new IllegalArgumentException("Technology with id " + entity.technologyId() + " not found")));
        return skill;
    }
}
