package com.example.lmsapplication.tables;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "wfh_leave")
    private Integer wfhLeave;

    @Column(name = "sick_leave")
    private Integer sickLeave;

    @Column(name = "casual_leave")
    private Integer casualLeave;


}