package com.entropyteam.entropay.employees.clients.controllers;

import java.util.List;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.entropyteam.entropay.employees.clients.dtos.ClientDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropyteam.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropyteam.entropay.employees.clients.services.ClientService;
import com.entropyteam.entropay.employees.common.exceptions.InvalidRequestParametersException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> findClients() {
        log.info("GET findClients");
        int page = 1;
        int size = 10;
        Direction sort = Direction.ASC;
        String sortBy = "name";

        if (page < 1) {
            throw new InvalidRequestParametersException("\"page\" must be greater than 0.");
        }

        List<ClientDto> clients = clientService.listActiveClients(sort, page, size, sortBy).get().toList();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(clients.size())).body(clients);
    }

    @GetMapping(value = "/{clientId}")
    public ResponseEntity<ClientDto> findClientById(@PathVariable String clientId) {
        log.info("GET findClientById: {}", clientId);
        return ResponseEntity.ok(clientService.findClientById(clientId));
    }

    @PostMapping
    public ResponseEntity<ClientSaveResponseDto> createClient(@RequestBody
    ClientSaveRequestDto requestBody) {
        log.info("POST createClient");
        log.trace("Request Body: {}", requestBody);
        return ResponseEntity.ok(clientService.createClient(requestBody));
    }

    @PutMapping(value = "/{clientId}")
    public ResponseEntity<ClientSaveResponseDto> updateClientById(@PathVariable String clientId, @RequestBody
            ClientSaveRequestDto requestBody) {
        log.info("PUT updateClientById: {}.", clientId);
        log.trace("Request Body: {}", requestBody);
        return ResponseEntity.ok(clientService.updateClient(clientId, requestBody));
    }

    @DeleteMapping(value = "/{clientId}")
    public ResponseEntity<BodyBuilder> deleteClientById(@PathVariable String clientId) {
        log.info("DELETE deleteClientById: {}.", clientId);
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }
}