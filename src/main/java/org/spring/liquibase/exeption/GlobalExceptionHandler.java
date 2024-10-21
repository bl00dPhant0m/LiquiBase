package org.spring.liquibase.exeption;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> phoneNotPresentException(PropertyValueException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        String collect = Arrays.stream(stackTrace).map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));

        log.error(e.getMessage());
        log.error(collect);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}