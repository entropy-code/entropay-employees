package com.entropyteam.entropay.common.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.entropyteam.entropay.common.exceptions.dtos.ErrorResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponseDto> authExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<ErrorResponseDto> invalidRequestParametersExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> resourceNotFoundExceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        LOGGER.error(ex.getMessage());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .toList();
        ErrorResponseDto response = new ErrorResponseDto(String.join(", ", errors));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> noSuchElementExceptionHandler(HttpServletRequest request,  NoSuchElementException e) {
        LOGGER.error(e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> exceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error("Error occurred processing request", e);
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
