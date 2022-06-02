package com.entropyteam.entropay.employees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    BuildProperties buildProperties;

    @GetMapping("/health-check")
    public ResponseEntity<String> heathCheck(){
        return ResponseEntity.ok("Entropay-employess: OK, Version: 2, " + buildProperties.getVersion());
    }
}

