package com.example.caballos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> manejarAuth(AuthException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, String>> manejarRegla(ReglaNegocioException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> manejarValidacion(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Solicitud invalida"));
    }
}
