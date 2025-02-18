package com.entropyteam.entropay.security.controllers;

import com.entropyteam.entropay.security.services.EmailLeakCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private final EmailLeakCheckService emailLeakCheckService;

    public SecurityController(EmailLeakCheckService emailLeakCheckService) {
        this.emailLeakCheckService = emailLeakCheckService;
    }

    @GetMapping("/trigger-email-check")
    public ResponseEntity<String> runEmailLeakCheck() {
        emailLeakCheckService.checkEmailsForLeaks();
        return ResponseEntity.ok("Leak check process started.");
    }
}
