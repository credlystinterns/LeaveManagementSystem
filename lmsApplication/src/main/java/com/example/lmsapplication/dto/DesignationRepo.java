package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface DesignationRepo extends JpaRepository<Designation, Integer> {


//    @Query(value = "SELECT * FROM designation WHERE designation_name = :name", nativeQuery = true)
//    Optional<Designation> findNativeByName(@Param("name") String name);

    Optional<Designation>findByDesignationName(String name);
}

