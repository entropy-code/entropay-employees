package com.entropyteam.entropay.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/health-check")
    public ResponseEntity<String> heathCheck(){
        return ResponseEntity.ok("Entropay-employess: OK");
    }
}

