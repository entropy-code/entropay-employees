package com.entropyteam.entropay.employees.dtos;

import java.util.UUID;
import com.entropyteam.entropay.employees.models.Skill;

import jakarta.validation.constraints.NotNull;

public record SkillDto(UUID id,
                       @NotNull(message = "Employee ID is mandatory")
                       UUID employeeId,
                       @NotNull(message = "Technology ID is mandatory")
                       UUID technologyId,
                       @NotNull(message = "Proficiency level is mandatory")
                       String proficiencyLevel) {

    public SkillDto(Skill skill) {
        this(skill.getId(),
             skill.getEmployee().getId(),
             skill.getTechnology().getId(),
             skill.getProficiencyLevel().name());
    }

}
