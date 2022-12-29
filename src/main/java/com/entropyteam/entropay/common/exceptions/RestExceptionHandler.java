package com.entropyteam.entropay.common.exceptions;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> AuthExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<Object> InvalidRequestParametersExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> ResourceNotFoundExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error("Error occurred processing request", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
