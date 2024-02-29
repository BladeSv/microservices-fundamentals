package by.mitrakhovich.resourceservice.controller;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public ResponseEntity<?> handleValidationInputFileException(ConstraintViolationException ex) {
        log.error("Validation exception", ex);
        String message = ex.getConstraintViolations().stream().findFirst().get().getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception", ex);
        return new ResponseEntity<>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
