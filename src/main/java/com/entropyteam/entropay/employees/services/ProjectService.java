package com.entropyteam.entropay.employees.services;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
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
            ClientRepository clientRepository, ReactAdminMapper reactAdminMapper) {
        super(Project.class, reactAdminMapper);
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
    protected Project toEntity(ProjectDto projectDto) {
        Client client = clientRepository.findById(projectDto.clientId()).orElseThrow();
        Project project = new Project(projectDto);
        project.setClient(client);
        if(projectDto.projectTypeId() != null){
            ProjectType projectType = projectTypeRepository.findById(projectDto.projectTypeId()).orElseThrow();
            project.setProjectType(projectType);
        }

        return project;
    }
}
