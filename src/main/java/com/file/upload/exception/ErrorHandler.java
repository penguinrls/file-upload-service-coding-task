package com.file.upload.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException validationException) {
        log.error(validationException.getMessage(), validationException);
        return new ResponseEntity<>(validationException.getMessage(), FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleException(FileProcessingException fileProcessingException) {
        log.error(fileProcessingException.getMessage(), fileProcessingException);
        return new ResponseEntity<>(fileProcessingException.getMessage(), BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), INTERNAL_SERVER_ERROR);
    }

}
