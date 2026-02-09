package com.example.lmsapplication.exception;



import com.example.lmsapplication.exception.EmployeeNotFoundException;
import com.example.lmsapplication.exception.SessionNotFoundException;
import com.example.lmsapplication.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFound(SessionNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(401, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(404, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse(500, "Something went wrong"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
