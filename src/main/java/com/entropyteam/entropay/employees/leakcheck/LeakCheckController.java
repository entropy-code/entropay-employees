package com.entropyteam.entropay.employees.leakcheck;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leak-check")
class LeakCheckController {

    private final EmailLeakCheckService emailLeakCheckService;

    LeakCheckController(EmailLeakCheckService emailLeakCheckService) {
        this.emailLeakCheckService = emailLeakCheckService;
    }

    /**
     * Triggers a full leak check for all active employees.
     * This is an async process that runs in the background.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/trigger")
    public ResponseEntity<String> runEmailLeakCheck() {
        emailLeakCheckService.runAsyncEmailCheck();
        return ResponseEntity.ok("Leak check process started.");
    }

    /**
     * Checks a single email address for leaks.
     * This is a synchronous operation that returns results immediately.
     * Does not save results to database.
     *
     * @param email the email address to check
     * @return leak check results
     * @throws IllegalArgumentException if email format is invalid
     */
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER_HR"})
    @GetMapping("/check-email")
    public ResponseEntity<SingleEmailLeakCheckDto> checkSingleEmail(@RequestParam String email) {
        SingleEmailLeakCheckDto result = emailLeakCheckService.checkSingleEmail(email);
        return ResponseEntity.ok(result);
    }
}
