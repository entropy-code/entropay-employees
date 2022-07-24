package com.entropyteam.entropay.employees.clients.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.entropyteam.entropay.employees.clients.models.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByIdAndDeletedIsFalse(UUID clientId);

    List<Client> findAllByDeletedIsFalse();

}
