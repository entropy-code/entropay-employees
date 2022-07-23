package com.entropyteam.entropay.employees.project.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.entropyteam.entropay.employees.project.models.Project;

public interface ProjectRepository extends PagingAndSortingRepository<Project, UUID> {

    List<Project> findAllByDeletedIsFalse();

    @Modifying
    @Query("""
            update Project p
            set p.deleted = true, p.modifiedAt = current_timestamp
            where p.id = :id
            """)
    void delete(@Param("id") UUID id);

}
