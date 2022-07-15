package com.entropyteam.entropay.employees.clients.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.models.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByIdAndIsActiveTrue(UUID clientId);
    @Query("""
            SELECT new com.entropyteam.entropay.employees.clients.dtos.ClientDto(
                c.id, c.name, c.address, c.zipCode, c.city, c.state, c.country, c.contact, c.preferredCurrency,
                c.createdOn, c.modifiedOn)
            FROM Client AS c
            """)
    Page<ClientDto> findAllByIsActiveTrue(Pageable pageable);

}
