package com.entropy.entropay.employees.clients.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.entropy.entropay.employees.clients.models.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByIdAndStatus(UUID clientId, Boolean status);

    List<Client> findAllByStatus(Boolean status, Sort sort);


}
