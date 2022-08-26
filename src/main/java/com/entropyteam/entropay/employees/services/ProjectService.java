package com.entropyteam.entropay.employees.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.ProjectDto;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Project;
import com.entropyteam.entropay.employees.models.ProjectType;
import com.entropyteam.entropay.employees.repositories.ClientRepository;
import com.entropyteam.entropay.employees.repositories.ProjectRepository;
import com.entropyteam.entropay.employees.repositories.ProjectTypeRepository;

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
    protected BaseRepository<Project, UUID> getRepository() {
        return projectRepository;
    }

    @Override
    protected ProjectDto toDTO(Project entity) {
        return new ProjectDto(entity);
    }

    @Override
    protected Project toEntity(ProjectDto entity) {
        Client client = clientRepository.findById(entity.clientId()).orElseThrow();
        ProjectType projectType = projectTypeRepository.findById(entity.projectTypeId()).orElseThrow();

        Project project = new Project(entity);
        project.setClient(client);
        project.setProjectType(projectType);

        return project;
    }
}
