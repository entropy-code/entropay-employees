package com.entropyteam.entropay.employees.repositories;

import java.util.List;
import java.util.UUID;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.employees.models.Client;
import com.entropyteam.entropay.employees.models.Contract;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends BaseRepository<Client, UUID> {
    @Query(value = "SELECT DISTINCT c.* FROM client c LEFT JOIN project p ON c.id = p.client_id WHERE p.client_id IS NOT NULL AND " +
            "c.deleted = false and p.deleted = false", nativeQuery = true)
    List<Client> findAllClientsWithAProject();
}
