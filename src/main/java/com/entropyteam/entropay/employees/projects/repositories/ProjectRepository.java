package com.entropyteam.entropay.employees.projects.repositories;

import java.util.UUID;
import com.entropyteam.entropay.employees.projects.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
