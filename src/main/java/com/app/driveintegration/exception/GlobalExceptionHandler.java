package com.app.driveintegration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidArgumentException(
        InvalidArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUnAuthorizedException(
        HttpClientErrorException.Unauthorized unauthorized) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You don't have required Authorization to Access");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleFileNotFoundException(
        HttpClientErrorException.NotFound notFound) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Resource you are trying to access is not available in the given path");
    }

    public ResponseEntity<String> handleBadRequest(HttpClientErrorException.BadRequest badRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("invalid parameters in the request ");
    }
}
