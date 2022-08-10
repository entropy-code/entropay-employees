package com.entropyteam.entropay.employees.project.services;

import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.clients.models.Client;
import com.entropyteam.entropay.employees.clients.repositories.ClientRepository;
import com.entropyteam.entropay.employees.project.dtos.ProjectDto;
import com.entropyteam.entropay.employees.project.models.Project;
import com.entropyteam.entropay.employees.project.models.ProjectType;
import com.entropyteam.entropay.employees.project.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.project.repositories.ProjectTypeRepository;

@Service
public class ProjectService extends BaseService<Project, ProjectDto, UUID> {

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
    @Transactional
    public ProjectDto create(ProjectDto entity) {
        Client client = clientRepository.findById(entity.clientId()).orElseThrow();
        ProjectType projectType = projectTypeRepository.findById(entity.projectTypeId()).orElseThrow();

        Project project = new Project(entity);
        project.setClient(client);
        project.setProjectType(projectType);

        Project save = projectRepository.save(project);

        return new ProjectDto(save);
    }

    @Override
    @Transactional
    public ProjectDto update(UUID id, ProjectDto entity) {
        Client client = clientRepository.findById(entity.clientId()).orElseThrow();
        ProjectType projectType = projectTypeRepository.findById(entity.projectTypeId()).orElseThrow();

        Project project = new Project(entity);
        project.setId(id);
        project.setClient(client);
        project.setProjectType(projectType);

        Project save = projectRepository.save(project);

        return new ProjectDto(save);
    }

    @Override
    protected BaseRepository<Project, UUID> getRepository() {
        return projectRepository;
    }

    @Override
    protected ProjectDto toDTO(Project entity) {
        return new ProjectDto(entity);
    }

    @Override
    protected Project toEntity(ProjectDto entity) {
        return new Project(entity);
    }
}
