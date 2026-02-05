package com.example.lmsapplication.service;

import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.tables.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {
    EmployeeRepo employeeRepo;

    public MainService(EmployeeRepo employeeRepo){
        this.employeeRepo = employeeRepo;
    }
    public List<Employee> getReportees(Integer managerId){
        return employeeRepo.findEmployeesByManagerId(managerId);
    }
}
