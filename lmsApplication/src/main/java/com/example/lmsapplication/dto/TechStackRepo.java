package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepo extends JpaRepository<TechStack, Integer> {

}
