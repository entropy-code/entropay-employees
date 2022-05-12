package com.entropy.entropay.employees.clients.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import com.entropy.entropay.employees.clients.dtos.ClientDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveRequestDto;
import com.entropy.entropay.employees.clients.dtos.ClientSaveResponseDto;
import com.entropy.entropay.employees.clients.services.ClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/client",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(value = "/")
    public ResponseEntity<List<ClientDto>> findClients() {
        log.info("GET findClients");
        return ResponseEntity.ok(clientService.listClients());
    }

    @GetMapping(value = "/{clientId}")
    public ResponseEntity<ClientDto> findClientById(@PathVariable String clientId) {
        log.info("GET findClientById: {}", clientId);
        return ResponseEntity.ok(clientService.findById(clientId));
    }

    @PostMapping
    public ResponseEntity<ClientSaveResponseDto> createClient(@RequestBody
    ClientSaveRequestDto requestBody) {
        log.info("POST createClient");
        return ResponseEntity.ok(clientService.createClient(requestBody));
    }

    @PutMapping(value = "/{clientId}")
    public ResponseEntity<ClientSaveResponseDto> updateClientById(@PathVariable String clientId, @RequestBody
            ClientSaveRequestDto requestBody) {
        log.info("PUT updateClientById: {}.", clientId);
        return ResponseEntity.ok(clientService.updateClient(clientId, requestBody));
    }

    @DeleteMapping(value = "/{clientId}")
    public BodyBuilder deleteClientById(@PathVariable String clientId) {
        log.info("DELETE deleteClientById: {}.", clientId);
        clientService.deleteClient(clientId);
        return ResponseEntity.status(HttpStatus.OK);
    }
}