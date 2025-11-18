package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Children;

public interface ChildrenRepository extends BaseRepository<Children, UUID> {

    @Query("""
            SELECT c
            FROM Children c
            JOIN FETCH c.parents p
            WHERE c.deleted = false
              AND p.id = ?1""")
    List<Children> findAllByParentId(UUID parentId);
}
