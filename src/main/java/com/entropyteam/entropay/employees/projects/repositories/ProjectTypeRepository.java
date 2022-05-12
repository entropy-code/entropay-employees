package com.entropy.entropay.employees.projects.repositories;

import com.entropy.entropay.employees.projects.models.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {
}
