package com.example.lmsapplication.dto;

import com.example.lmsapplication.tables.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LeaveRequestRepository extends JpaRepository<Leaves,Integer> {
    List<Leaves> findLeavesByEmployee_Id(Integer employee_Id);

//    List<Leaves> findLeavesByLeave_Id(Integer leaveId);
}
