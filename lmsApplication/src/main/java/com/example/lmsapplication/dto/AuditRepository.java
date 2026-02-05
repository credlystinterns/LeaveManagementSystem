package com.example.lmsapplication.dto;

import com.example.lmsapplication.audit.AuditDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditDetails, Long> {
    AuditDetails findByLeave_Id(Integer leave_Id);
}
