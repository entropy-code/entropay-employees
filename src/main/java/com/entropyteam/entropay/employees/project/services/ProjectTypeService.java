package com.entropyteam.entropay.employees.project.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.common.CrudService;
import com.entropyteam.entropay.employees.common.Filter;
import com.entropyteam.entropay.employees.common.Range;
import com.entropyteam.entropay.employees.common.Sort;
import com.entropyteam.entropay.employees.project.dtos.ProjectTypeDto;
import com.entropyteam.entropay.employees.project.models.ProjectType;
import com.entropyteam.entropay.employees.project.repositories.ProjectTypeRepository;

@Service
public class ProjectTypeService implements CrudService<ProjectTypeDto, UUID> {

    private final ProjectTypeRepository projectTypeRepository;

    public ProjectTypeService(ProjectTypeRepository projectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
    }

    @Override
    public Optional<ProjectTypeDto> findOne(UUID id) {
        return projectTypeRepository.findById(id)
                .map(ProjectTypeDto::new);
    }

    @Override
    public List<ProjectTypeDto> findAllActive(Filter filter, Sort sort, Range range) {
        return projectTypeRepository.findAllByDeletedIsFalse()
                .stream()
                .map(ProjectTypeDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectTypeDto delete(UUID id) {
        projectTypeRepository.delete(id);
        return projectTypeRepository.findById(id)
                .map(ProjectTypeDto::new)
                .orElseThrow();
    }

    @Override
    public ProjectTypeDto create(ProjectTypeDto entity) {
        ProjectType projectType = new ProjectType();
        projectType.setName(entity.name());

        ProjectType save = projectTypeRepository.save(projectType);

        return new ProjectTypeDto(save);
    }

    @Override
    public ProjectTypeDto update(UUID id, ProjectTypeDto entity) {
        ProjectType projectType = projectTypeRepository.findById(id).orElseThrow();
        projectType.setName(entity.name());

        ProjectType save = projectTypeRepository.save(projectType);

        return new ProjectTypeDto(save);
    }
}
