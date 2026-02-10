package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepo extends JpaRepository<Designation,Integer> {


}
