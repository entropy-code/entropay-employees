package com.entropyteam.entropay.employees.project.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.entropyteam.entropay.employees.project.models.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, UUID> {

    List<Project> findAllByDeletedIsFalse();

}
