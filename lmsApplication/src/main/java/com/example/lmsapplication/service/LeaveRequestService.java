package com.example.lmsapplication.service;
import com.example.lmsapplication.requisites.LeaveRequest;

import com.example.lmsapplication.tables.AuditDetails;
import com.example.lmsapplication.dto.AuditRepository;
import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.dto.LeaveRequestRepository;
import com.example.lmsapplication.response.AcceptResponse;
import com.example.lmsapplication.response.RejectResponse;
import com.example.lmsapplication.response.RevokeResponse;
import com.example.lmsapplication.tables.Employee;
import com.example.lmsapplication.tables.Leaves;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    public record Response(boolean success, String reason) {

    }

   @Autowired
   LeaveRequestRepository leaveRequestRepository;
    @Autowired
    EmployeeRepo employeeRepo ;
   @Autowired
   AuditRepository auditRepository;

    public Response applyLeave(Integer employeeId, LeaveRequest leaverequest) {

        Employee emp = getEmployeeById(employeeId);

        String leave_type = leaverequest.getLeaveType();
        Integer casual_leave = getCasualLeavesCount(emp);
        Integer sick_leave = getSickLeavesCount(emp);
        Integer wfh_leave = getWFHLeavesCount(emp);

        if (leaverequest.getStartDate().after(leaverequest.getEndDate())) {
            return new Response(false, "Start date cannot be after end date");
        }

        // get start and end dates from your leaveRequest
        Date start = leaverequest.getStartDate();
        Date end = leaverequest.getEndDate();

// convert to LocalDate (ignores time)
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

// calculate days between, inclusive
        int leaveDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (leave_type.equalsIgnoreCase("casual")) {

            if (casual_leave >= leaveDays) {
                casual_leave = casual_leave - leaveDays;
                emp.setCasualLeave(casual_leave);
                employeeRepo.save(emp);

                // Map request -> Leaves entity with status "Pending"
                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leave_type);
                toSave.setStatus("Pending");
                toSave.setStart_date(leaverequest.getStartDate());
                toSave.setEnd_date(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Casual Leave Applied Successfully , Remaining Casual Leaves are : " + casual_leave);

            } else {
                return new Response(false, "Your Casual Leave limit is Exceeded");
            }

        } else if (leave_type.equalsIgnoreCase("wfh")) {

            if (wfh_leave >= leaveDays) {
                wfh_leave = wfh_leave - leaveDays;
                emp.setWfhLeave(wfh_leave);
                employeeRepo.save(emp);

                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leave_type);
                toSave.setStatus("Pending");
                toSave.setStart_date(leaverequest.getStartDate());
                toSave.setEnd_date(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Work From Home Leave Applied Successfully , Remaining Work From Home Leaves are : " + wfh_leave);

            } else {
                return new Response(false, "Your Work From Home Leave limit is Exceeded");
            }

        } else if (leave_type.equalsIgnoreCase("sick")) {

            if (sick_leave >= leaveDays) {
                sick_leave = sick_leave - leaveDays;
                emp.setSickLeave(sick_leave);
                employeeRepo.save(emp);

                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leave_type);
                toSave.setStatus("Pending");
                toSave.setStart_date(leaverequest.getStartDate());
                toSave.setEnd_date(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Your Sick Leave is Applied Successfully , Remaining Sick Leave Limit are : " + sick_leave);

            } else {
                return new Response(false, "Your Sick  Leave limit is Exceeded");
            }

        } else {
            return new Response(false, "Invalid Leave Type");
        }
    }

    public Employee getEmployeeById(Integer employee_Id) {
        return employeeRepo.findById(employee_Id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Integer getCasualLeavesCount(Employee employee){
        return employee.getCasualLeave();
    }

    public Integer getSickLeavesCount(Employee employee){
        return employee.getSickLeave();
    }

    public Integer getWFHLeavesCount(Employee employee){
        return employee.getWfhLeave();
    }

// leave history
   public List<Leaves> getLeaves(Integer employee_Id){
       List<Leaves> leaves = leaveRequestRepository.findLeavesByEmployee(employee_Id);
        return leaves;
   }

   public RevokeResponse revokeLeave(Integer employee_Id){


       List<Leaves>employee_Leaves =  leaveRequestRepository.findLeavesByEmployee(employee_Id);

       if (employee_Leaves.isEmpty()) {
           throw new RuntimeException("No leaves found for employee id: " + employee_Id);
       }

       RevokeResponse response = new RevokeResponse();

       response.setKey("Revoke");
       response.setHref("/revoke");
       response.setMethod("PUT");
       response.setLeaves(employee_Leaves);

       return response;
   }

   public Response revokeCompletion(Integer leave_Id){


       Optional<Leaves> employee_Leaves = leaveRequestRepository.findById(leave_Id);

       Leaves leave = employee_Leaves.orElseThrow(() ->
               new RuntimeException("Leave not found with id: " + leave_Id)
       );



          String leave_type = leave.getLeaveType();
          String curr_status = leave.getStatus();
          if(!curr_status.equalsIgnoreCase("approved")){
              return new Response(false, "Leave Status is Not Approved Yet , You can not Revoke");
          }

       // Calculation of Leave Days

       // get start and end dates from your leaveRequest
       Date start = leave.getStart_date();
       Date end = leave.getEnd_date();

// convert to LocalDate (ignores time)
       LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
       LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

// calculate days between, inclusive
       int leaveDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;


          Integer employee_Id = leave.getEmployee();

          Employee emp = getEmployeeById(employee_Id);

       Integer casual_leave = getCasualLeavesCount(emp);
       Integer sick_leave = getSickLeavesCount(emp);
       Integer wfh_leave = getWFHLeavesCount(emp);

       if(leave_type.equalsIgnoreCase("casual")){
            casual_leave = casual_leave + leaveDays ;
           emp.setCasualLeave(casual_leave);
           leave.setStatus("Revoked");

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leave_Id, employee_Id, "Revoked");

           return new Response(true, "Revoked Successfully");
       }
       else if(leave_type.equalsIgnoreCase("wfh")){
           wfh_leave = wfh_leave +  leaveDays;
           emp.setWfhLeave(wfh_leave);
           leave.setStatus("Revoked");

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leave_Id, employee_Id, "Revoked");

           return new Response(true, "Revoked Successfully");
       }else if(leave_type.equalsIgnoreCase("sick")){
           sick_leave = sick_leave +  leaveDays;
           emp.setSickLeave(sick_leave);
           leave.setStatus("Revoked");

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leave_Id, employee_Id, "Revoked");

           return new Response(true, "Revoked Successfully");
       }


       return new Response(false , "Leave Type is Invalid");




   }

    public List<Employee> getReportees(int manager_id) {
        return employeeRepo.findEmployeesByManagerId(manager_id);
    }

    public AcceptResponse accepted(int manager_id){

        AcceptResponse acceptResponse = new AcceptResponse();
        acceptResponse.setKey("Accepted");
        acceptResponse.setHref("/accept/{leave_id}");
        List<Employee> ems = employeeRepo.findEmployeesByManagerId(manager_id);
        acceptResponse.setLeaves(ems.stream()
                .map(x -> leaveRequestRepository.findLeavesByEmployee(x.getEmployeeId()))
                .flatMap(List::stream ).collect(Collectors.toList()) );
        return acceptResponse;
    }

    public Response acceptance(int leave_id, int manager_id){
        Optional<Leaves> leaves = leaveRequestRepository.findById(leave_id);
        if(leaves.isPresent()){
            leaves.get().setStatus("Approved");
            saveAuditForUpdate(leave_id,
                    manager_id,
                    "Approved");
            return new Response(true,"Accepted");
        }
        else{
            return new Response(false,"Leave_id doesn't exist");
        }

    }

    public RejectResponse rejected(int manager_id){

        RejectResponse rejectResponse = new RejectResponse();
        rejectResponse.setKey("Rejected");
        rejectResponse.setHref("/reject/{leave_id}");
        List<Employee> ems = employeeRepo.findEmployeesByManagerId(manager_id);
        rejectResponse.setLeaves(ems.stream()
                .map(x -> leaveRequestRepository.findLeavesByEmployee(x.getEmployeeId()))
                .flatMap(List::stream ).collect(Collectors.toList()) ) ;
        return rejectResponse;
    }

    public Response rejectance(int leave_id, int manager_id){
        Optional<Leaves> leaves = leaveRequestRepository.findById(leave_id);
        if(leaves.isPresent()){
            leaves.get().setStatus("Rejected");
            saveAuditForUpdate(leave_id,
                    manager_id,
                    "Rejected");
            return new Response(true,"Rejected");
        }
        else{
            return new Response(false,"Leave_id doesn't exist");
        }

    }


    public List<Leaves> getLeaves(int employee_id){
        return leaveRequestRepository.findLeavesByEmployee(employee_id);
    }






    // Audit when leave applied
    private void saveAuditForApply(Integer leave_Id, Integer employee_Id) {

        AuditDetails audit = new AuditDetails();

        audit.setLeave(leave_Id);
        audit.setCreatedById(employee_Id);
        audit.setCreatedAt(LocalDateTime.now());

        audit.setUpdatedAction("Pending");
        audit.setUpdatedAt(LocalDateTime.now());

        auditRepository.save(audit);
    }

    // Audit when leave updated (approved/rejected/revoked)
    private void saveAuditForUpdate(Integer leave_Id,
                                    Integer updatedById,
                                    String action) {

        AuditDetails audit = auditRepository.findByLeave(leave_Id);

        if (audit == null) {
            throw new RuntimeException("Audit record not found for leave id: " + leave_Id);
        }

        audit.setUpdatedById(updatedById);
        audit.setUpdatedAction(action);
        audit.setUpdatedAt(LocalDateTime.now());

        auditRepository.save(audit);
    }




}
