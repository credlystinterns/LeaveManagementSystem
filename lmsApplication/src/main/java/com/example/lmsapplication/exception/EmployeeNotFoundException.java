package com.example.lmsapplication.exception;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Integer employeeId) {
        super("Employee not found for id: " + employeeId);
    }
}

