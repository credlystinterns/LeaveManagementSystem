package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechStackRepo extends JpaRepository<TechStack, Integer> {
    Optional<TechStack>findByTechStackName(String name);
}
