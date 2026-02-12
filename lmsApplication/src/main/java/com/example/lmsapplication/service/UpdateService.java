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
        Optional<Designation>  des = designationRepo.findByDesignationName(designation);

        if(des.isPresent()){
            emp1.setDesignation(des.get());
            employeeRepo.save(emp1);

            return "Updated  Successfully";

        }
        else{
            return "No such Designation.";
        }

    }

    public String updateTechStack(Employee emp1,String techStack){
        List<TechStack>old = emp1.getSkills();

        Optional<TechStack> e = techStackRepo.findByTechStackName(techStack);
        if(e.isPresent()){
            old.add(e.get());
            emp1.setSkills(old);
            employeeRepo.save(emp1);
            return "Skills added";
        }
        else{
            return "No such skill.";
        }
    }

}
