package by.mitr.storageservice.controller;


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
    public ResponseEntity<?> handleValidationInputFileException(MethodArgumentNotValidException ex) {
        log.error("Validation exception", ex);
        return new ResponseEntity<>("Validation error or request body is an invalid Storage", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception", ex);
        return new ResponseEntity<>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}