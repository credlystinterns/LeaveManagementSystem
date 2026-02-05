package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,Integer> {
    Optional<Session> findSessionByEmployeeId(Integer employeeId);

    long deleteSessionBySessionToken(String token);
    Optional<Session> findSessionBySessionToken(String sessionToken);

    void deleteByEmployeeId(Integer employeeId);
}
