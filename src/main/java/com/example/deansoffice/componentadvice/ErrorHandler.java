package com.example.deansoffice.componentadvice;

import com.example.deansoffice.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({WorkerNotFoundException.class, WorkDateNotFoundException.class, IntervalNotFoundException.class, UserNotFoundException.class, StudentNotFoundException.class, SpecializationNotFoundException.class})
    public ResponseEntity<Object> handleWorkerNotFoundException(Exception ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        String message = rootCause.getMessage();

        if (message.contains("major_year.year_UNIQUE")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Year already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if(message.contains("specialization_major_year.idx_major_specialization")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Year with that Specialization already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Constraint violation occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
