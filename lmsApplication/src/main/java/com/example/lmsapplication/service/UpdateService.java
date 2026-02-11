package com.example.lmsapplication.service;


import com.example.lmsapplication.dto.DesignationRepo;
import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.dto.TechStackRepo;

import com.example.lmsapplication.tables.Designation;
import com.example.lmsapplication.tables.Employee;
import com.example.lmsapplication.tables.TechStack;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateService {

    DesignationRepo designationRepo;
    TechStackRepo techStackRepo;
    EmployeeRepo employeeRepo;

    public UpdateService(DesignationRepo designationRepo,TechStackRepo techStackRepo,EmployeeRepo employeeRepo){
        this.techStackRepo = techStackRepo;
        this.designationRepo = designationRepo;
        this.employeeRepo = employeeRepo;
    }

    public String updateDesignation(Employee emp1,String designation){
        Designation des = Designation.builder()
                .designationName(designation)
                .build();
        emp1.setDesignation(des);
        return "Updated  Successfully";
    }

    public String updateTechStack(Employee emp1,String techStack){
        List<TechStack>old = emp1.getSkills();
        TechStack e = TechStack.builder()
                .techStackName(techStack)
                .build();
        old.add(e);
        emp1.setSkills(old);
        return "Skills added";
    }

}
