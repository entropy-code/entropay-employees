package com.entropyteam.entropay.employees.clients.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.entropyteam.entropay.employees.clients.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByIdAndIsActive(UUID clientId, Boolean isActive);

    List<Client> findAllByIsActive(Boolean status, Sort sort);


}
