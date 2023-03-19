package com.example.deansoffice.componentadvice;

import com.example.deansoffice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({WorkerNotFoundException.class, WorkDateNotFoundException.class, IntervalNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleWorkerNotFoundException(Exception ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
