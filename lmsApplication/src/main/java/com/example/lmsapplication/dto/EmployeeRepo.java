package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Optional<Employee> findByEmail(String email);

    List<Employee>findEmployeesByManagerId(Integer manager_Id);

    Employee findByEmployeeName(String managerName);

    boolean existsByEmail(String email);
}
