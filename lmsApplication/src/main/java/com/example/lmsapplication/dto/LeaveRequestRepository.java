package com.example.lmsapplication.dto;

import com.example.lmsapplication.enums.LeaveStatus;
import com.example.lmsapplication.tables.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<Leaves,Integer> {

    @Query("""
    SELECT leave FROM Leaves leave
    WHERE leave.employee = :employeeId
      AND leave.startDate <= :endDate
      AND leave.endDate >= :startDate
""")

    List<Leaves> findOverlappingLeaves(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate ,
            @Param("status") List<LeaveStatus> statuses
    );

    List<Leaves> findLeavesByEmployee(Integer employeeId);



}
