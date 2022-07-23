package com.entropyteam.entropay.employees.project.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.entropyteam.entropay.employees.clients.repositories.ClientRepository;
import com.entropyteam.entropay.employees.common.CrudService;
import com.entropyteam.entropay.employees.common.Filter;
import com.entropyteam.entropay.employees.common.Range;
import com.entropyteam.entropay.employees.common.Sort;
import com.entropyteam.entropay.employees.project.dtos.ProjectDto;
import com.entropyteam.entropay.employees.project.models.Project;
import com.entropyteam.entropay.employees.project.models.ProjectType;
import com.entropyteam.entropay.employees.project.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.project.repositories.ProjectTypeRepository;

@Service
public class ProjectService implements CrudService<ProjectDto, UUID> {

    private final ProjectRepository projectRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectTypeRepository projectTypeRepository,
            ClientRepository clientRepository) {
        this.projectRepository = projectRepository;
        this.projectTypeRepository = projectTypeRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<ProjectDto> findOne(UUID id) {
        return projectRepository.findById(id)
                .map(ProjectDto::new);
    }

    @Override
    public List<ProjectDto> findAllActive(Filter filter, Sort sort, Range range) {
        return projectRepository.findAllByDeletedIsFalse()
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDto delete(UUID id) {
        projectRepository.delete(id);
        return projectRepository.findById(id)
                .map(ProjectDto::new)
                .orElseThrow();
    }

    @Override
    public ProjectDto create(ProjectDto entity) {
        Client client = clientRepository.findByIdAndDeletedIsFalse(entity.clientId()).orElseThrow();
        ProjectType projectType = projectTypeRepository.findById(entity.projectTypeId()).orElseThrow();

        Project project = new Project();
        project.setClient(client);
        project.setProjectType(projectType);
        project.setName(entity.name());
        project.setStartDate(entity.startDate());
        project.setEndDate(entity.endDate());

        Project save = projectRepository.save(project);

        return new ProjectDto(save);
    }

    @Override
    public ProjectDto update(UUID id, ProjectDto entity) {
        Project project = projectRepository.findById(id).orElseThrow();
        Client client = clientRepository.findByIdAndDeletedIsFalse(entity.clientId()).orElseThrow();
        ProjectType projectType = projectTypeRepository.findById(entity.projectTypeId()).orElseThrow();

        project.setClient(client);
        project.setProjectType(projectType);
        project.setName(entity.name());
        project.setStartDate(entity.startDate());
        project.setEndDate(entity.endDate());
        project.setNotes(entity.notes());

        Project save = projectRepository.save(project);

        return new ProjectDto(save);
    }
}
