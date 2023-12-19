package by.mitrakhovich.songservice.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<?> handleNoValidException(MethodArgumentNotValidException ex) {
        final String message = "Song metadata missing validation error";
        log.error(message, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNoFindEntityException(EntityNotFoundException ex) {
        final String message = "The song metadata with the specified id does not exist";
        log.error(message, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception", ex);
        return new ResponseEntity<>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
