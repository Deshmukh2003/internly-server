package com.internly.exception;

import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> badRequest(IllegalArgumentException e) { return response(HttpStatus.BAD_REQUEST, e.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e) { return response(HttpStatus.BAD_REQUEST, e.getBindingResult().getFieldErrors().stream().map(x -> x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining(", "))); }
    private ResponseEntity<?> response(HttpStatus status, String message) { return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status.value(), message)); }
    record ErrorResponse(Instant timestamp, int status, String message) {}
}
