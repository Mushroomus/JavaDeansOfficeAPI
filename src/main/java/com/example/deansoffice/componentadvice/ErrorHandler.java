package com.example.deansoffice.componentadvice;

import com.example.deansoffice.exception.*;
import com.example.deansoffice.model.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> handleGenericException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Something went wrong"));
    }

    @ExceptionHandler({RecordNotFoundException.class})
    public ResponseEntity<Object> handleRecordNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<Object> handleInternalServerErrorException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({AccessForbiddenException.class})
    public ResponseEntity<Object> handleAccessForbiddenException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        assert rootCause != null;
        String message = rootCause.getMessage();

        if (message.contains("major_year.year_UNIQUE")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Year already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if(message.contains("specialization_major_year.idx_major_specialization")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Year with that Specialization already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if(message.contains("specialization.name_course_unique")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Name and Course already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if(message.contains("login.username_UNIQUE")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Login exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if(message.contains("student_major_details.unique_student_specialization_major_year")) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Student already has that major details");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "Constraint violation occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
