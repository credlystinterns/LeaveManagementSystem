package com.example.lmsapplication.service;

import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.dto.SessionRepo;
import com.example.lmsapplication.tables.Employee;
import com.example.lmsapplication.tables.Session;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchEmployee {
    SessionRepo sessionRepo;
    EmployeeRepo employeeRepo;

    public FetchEmployee(SessionRepo sessionRepo,EmployeeRepo employeeRepo){
        this.employeeRepo = employeeRepo;
        this.sessionRepo = sessionRepo;
    }


    public Employee getEmployee(String token){
        Session session =  sessionRepo.findSessionBySessionToken(token)
                .orElseThrow(() -> new RuntimeException(""));

       return  employeeRepo.findById(session.getEmployeeId())
               .orElseThrow(() -> new RuntimeException(""));
    }
}
