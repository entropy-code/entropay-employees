package com.entropyteam.entropay.employees.clients.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.models.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, UUID> {

    Optional<Client> findByIdAndDeletedIsFalse(UUID clientId);
    @Query("""
            SELECT new com.entropyteam.entropay.employees.clients.dtos.ClientDto(
                c.id, c.name, c.address, c.zipCode, c.city, c.state, c.country, c.contact, c.preferredCurrency,
                c.createdAt, c.modifiedAt)
            FROM Client AS c
            WHERE c.deleted = false
            """)
    Page<ClientDto> findAllByDeletedIsFalse(Pageable pageable);

}
