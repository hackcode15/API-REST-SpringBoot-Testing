package com.app.pruebaApi.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.pruebaApi.model.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Person
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> hadlePersonaNotFoundException(PersonNotFoundException ex) {

        /*
        ApiResponse<String> error = new ApiResponse<>(
            "Error, status" + HttpStatus.NOT_FOUND.value(),
            "Person not found",
            ex.getMessage()
        );
         */

        ApiResponse<String> error = new ApiResponse<>(
                "error",
                ex.getMessage(),
                null);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    // excepciones genericas comunes
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "error",
                "Validation failed",
                errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(
                "error",
                "An unexpected error occurred",
                null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
