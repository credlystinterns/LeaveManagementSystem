package com.example.lmsapplication.service;

import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.requisites.PasswordHasher;
import com.example.lmsapplication.requisites.SignUpBody;
import com.example.lmsapplication.tables.Employee;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    public record Obj( String status, String message) {}
    private final EmployeeRepo employeeRepo;
    private final PasswordHasher ps = new PasswordHasher();

    public SignUpService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Obj createEmployee(SignUpBody signUpBody) {

        if (employeeRepo.existsByEmail((signUpBody.getEmail()))) {
            return new Obj(
                    "FAILED",
                    "Employee already exists with this email"
            );
        }


        Employee manager = employeeRepo.findByEmployeeName(
                signUpBody.getManagerName()
        );

        if (manager == null) {
            return new Obj(
                    "FAILED",
                    "Manager not found"
            );
        }


        Employee employee = Employee.builder()
                .employeeName(signUpBody.getName())
                .email(signUpBody.getEmail())
                .password(ps.hash(signUpBody.getPassword()))
                .casualLeave(3)
                .sickLeave(3)
                .wfhLeave(2)
                .managerId(manager.getEmployeeId())
                .build();

        employeeRepo.save(employee);

        return new Obj(
                "SUCCESS",
                "Employee created successfully"
        );
    }
}
