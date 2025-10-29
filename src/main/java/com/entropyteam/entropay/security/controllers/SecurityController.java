package com.entropyteam.entropay.security.controllers;

import com.entropyteam.entropay.security.services.EmailLeakCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private final EmailLeakCheckService emailLeakCheckService;

    public SecurityController(EmailLeakCheckService emailLeakCheckService) {
        this.emailLeakCheckService = emailLeakCheckService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/trigger-email-check")
    public ResponseEntity<String> runEmailLeakCheck() {
        emailLeakCheckService.runAsyncEmailCheck();
        return ResponseEntity.ok("Leak check process started.");
    }
}
