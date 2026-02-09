package com.example.lmsapplication.service;
import com.example.lmsapplication.enums.LeaveStatus;
import com.example.lmsapplication.enums.LeaveTypes;
import com.example.lmsapplication.exception.AuditRecordNotFoundException;
import com.example.lmsapplication.exception.EmployeeNotFoundException;
import com.example.lmsapplication.exception.LeavesNotFoundException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepo employeeRepo, AuditRepository auditRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepo = employeeRepo;
        this.auditRepository = auditRepository;
    }

    public record Response(boolean success, String reason) {

    }

   final
   LeaveRequestRepository leaveRequestRepository;
    final
    EmployeeRepo employeeRepo ;
   final
   AuditRepository auditRepository;

    public Response applyLeave(Integer employeeId, LeaveRequest leaverequest) {

        Employee emp = getEmployeeById(employeeId);

        LeaveTypes leaveType = leaverequest.getLeaveType();
        Integer casualLeave = emp.getCasualLeave();
        Integer sickLeave = emp.getSickLeave();
        Integer wfhLeave = emp.getWfhLeave();


        if (leaverequest.getStartDate().isAfter(leaverequest.getEndDate())) {
            return new Response(false, "Start date cannot be after end date");
        }

        // Checking the Overlapping leaves
        List<Leaves> overlappingLeaves =
                leaveRequestRepository.findOverlappingLeaves(
                        employeeId,
                        leaverequest.getStartDate(),
                        leaverequest.getEndDate(),
                        List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED)
                );

        if (!overlappingLeaves.isEmpty()) {
            return new Response(
                    false,
                    "You already have a leave applied for the selected date(s)"
            );

        }


        // Calculation of Leave Days
        LocalDate startDate = leaverequest.getStartDate();
        LocalDate endDate =  leaverequest.getEndDate();

// calculate days between, inclusive
        int leaveDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (leaveType == LeaveTypes.CASUAL) {

            if (casualLeave >= leaveDays) {
                casualLeave = casualLeave - leaveDays;
                emp.setCasualLeave(casualLeave);
                employeeRepo.save(emp);

                // Map request -> Leaves entity with status "Pending"
                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leaveType);
                toSave.setStatus(LeaveStatus.PENDING);
                toSave.setStartDate(leaverequest.getStartDate());
                toSave.setEndDate(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Casual Leave Applied Successfully , Remaining Casual Leaves are : " + casualLeave);

            } else {
                return new Response(false, "Your Casual Leave limit is Exceeded");
            }

        } else if (leaveType == LeaveTypes.WFH) {

            if (wfhLeave >= leaveDays) {
                wfhLeave = wfhLeave - leaveDays;
                emp.setWfhLeave(wfhLeave);
                employeeRepo.save(emp);

                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leaveType);
                toSave.setStatus(LeaveStatus.PENDING);
                toSave.setStartDate(leaverequest.getStartDate());
                toSave.setEndDate(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Work From Home Leave Applied Successfully , Remaining Work From Home Leaves are : " + wfhLeave);

            } else {
                return new Response(false, "Your Work From Home Leave limit is Exceeded");
            }

        } else if (leaveType == LeaveTypes.SICK) {

            if (sickLeave >= leaveDays) {
                sickLeave = sickLeave - leaveDays;
                emp.setSickLeave(sickLeave);
                employeeRepo.save(emp);

                Leaves toSave = new Leaves();
                toSave.setEmployee(employeeId);
                toSave.setLeaveType(leaveType);
                toSave.setStatus(LeaveStatus.PENDING);
                toSave.setStartDate(leaverequest.getStartDate());
                toSave.setEndDate(leaverequest.getEndDate());

                Leaves savedLeave = leaveRequestRepository.save(toSave);

                saveAuditForApply(savedLeave.getId(), employeeId);

                return new Response(true, "Your Sick Leave is Applied Successfully , Remaining Sick Leave Limit are : " + sickLeave);

            } else {
                return new Response(false, "Your Sick  Leave limit is Exceeded");
            }

        } else {
            return new Response(false, "Invalid Leave Type");
        }
    }

    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }



// leave history
   public List<Leaves> getLeaves(Integer employeeId){
     return leaveRequestRepository.findLeavesByEmployee(employeeId);

   }

   public RevokeResponse revokeLeave(Integer employeeId){


       List<Leaves>employeeLeaves =  leaveRequestRepository.findLeavesByEmployee(employeeId);

       if (employeeLeaves.isEmpty()) {
           throw new LeavesNotFoundException("No leaves found for employee id: " + employeeId);
       }

       RevokeResponse response = new RevokeResponse();

       response.setKey("Revoke");
       response.setHref("/revoke");
       response.setMethod("PUT");
       response.setLeaves(employeeLeaves);

       return response;
   }

   public Response revokeCompletion(Integer leaveId){


       Optional<Leaves> employeeLeaves = leaveRequestRepository.findById(leaveId);

       Leaves leave = employeeLeaves.orElseThrow(() ->
               new LeavesNotFoundException("Leave not found with id: " + leaveId)
       );



          LeaveTypes leaveType = leave.getLeaveType();
          LeaveStatus currStatus = leave.getStatus();
          if(currStatus != LeaveStatus.APPROVED ){
              return new Response(false, "Leave Status is Not Approved Yet , You can not Revoke");
          }

       // Calculation of Leave Days


       LocalDate startDate = leave.getStartDate();
       LocalDate endDate = leave.getEndDate();


// calculate days between, inclusive
       int leaveDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;


          Integer employeeId = leave.getEmployee();

          Employee emp = getEmployeeById(employeeId);

       Integer casualLeave = emp.getCasualLeave();
       Integer sickLeave = emp.getSickLeave();
       Integer wfhLeave = emp.getWfhLeave();

       if(leaveType == LeaveTypes.CASUAL){
            casualLeave = casualLeave + leaveDays ;
           emp.setCasualLeave(casualLeave);
           leave.setStatus(LeaveStatus.REVOKED);

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leaveId, employeeId, "Revoked");

           return new Response(true, "Revoked Successfully");
       }
       else if(leaveType == LeaveTypes.WFH){
           wfhLeave = wfhLeave +  leaveDays;
           emp.setWfhLeave(wfhLeave);
           leave.setStatus(LeaveStatus.REVOKED);

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leaveId, employeeId, "Revoked");

           return new Response(true, "Revoked Successfully");
       }else if(leaveType == LeaveTypes.SICK){
           sickLeave = sickLeave +  leaveDays;
           emp.setSickLeave(sickLeave);
           leave.setStatus(LeaveStatus.REVOKED);

           employeeRepo.save(emp);
           leaveRequestRepository.save(leave);

           saveAuditForUpdate(leaveId, employeeId, "Revoked");

           return new Response(true, "Revoked Successfully");
       }


       return new Response(false , "Leave Type is Invalid");




   }

    public List<Employee> getReportees(int managerId) {
        return employeeRepo.findEmployeesByManagerId(managerId);
    }

    public AcceptResponse accepted(int managerId){

        AcceptResponse acceptResponse = new AcceptResponse();
        acceptResponse.setKey("Accepted");
        acceptResponse.setHref("/accept/{leave_id}");
        List<Employee> ems = employeeRepo.findEmployeesByManagerId(managerId);
        acceptResponse.setLeaves(ems.stream()
                .map(x -> leaveRequestRepository.findLeavesByEmployee(x.getEmployeeId()))
                .flatMap(List::stream ).toList() );
        return acceptResponse;
    }

    public Response acceptance(int leaveId, int managerId){
        Optional<Leaves> leaves = leaveRequestRepository.findById(leaveId);

        if(leaves.isPresent()){

            Leaves leave =   leaves.get();

            // employee can not accept its own leave

            if(leave.getEmployee().equals(managerId)){
                return new Response(false,"You can not approve your own leave") ;
            }

              // Approve
            leaves.get().setStatus(LeaveStatus.APPROVED);
            saveAuditForUpdate(leaveId,
                    managerId,
                    "Approved");
            return new Response(true,"Accepted");
        }
        else{
            return new Response(false,"Leave_id doesn't exist");
        }

    }

    public RejectResponse rejected(int managerId){

        RejectResponse rejectResponse = new RejectResponse();
        rejectResponse.setKey("Rejected");
        rejectResponse.setHref("/reject/{leave_id}");
        List<Employee> ems = employeeRepo.findEmployeesByManagerId(managerId);
        rejectResponse.setLeaves(ems.stream()
                .map(x -> leaveRequestRepository.findLeavesByEmployee(x.getEmployeeId()))
                .flatMap(List::stream ).toList() ) ;
        return rejectResponse;
    }

    public Response rejectance(int leaveId, int managerId) {

        Optional<Leaves> leaveOpt = leaveRequestRepository.findById(leaveId);

        if (leaveOpt.isPresent()) {

            Leaves leave = leaveOpt.get();
            leave.setStatus(LeaveStatus.REJECTED);

            Employee employee = employeeRepo.findById(leave.getEmployee())
                    .orElseThrow(() -> new EmployeeNotFoundException(leave.getEmployee()));

            LocalDate startDate = leave.getStartDate();
           LocalDate endDate = leave.getEndDate();


            int leaveDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

            LeaveTypes leaveType  = leave.getLeaveType();

            if (leaveType == LeaveTypes.SICK) {
                employee.setSickLeave(employee.getSickLeave() + leaveDays);
            }
            else if (leaveType == LeaveTypes.CASUAL) {
                employee.setCasualLeave(employee.getCasualLeave() + leaveDays);
            }
            else if (leaveType == LeaveTypes.WFH) {
                employee.setWfhLeave(employee.getWfhLeave() + leaveDays);
            }

            employeeRepo.save(employee);
            leaveRequestRepository.save(leave);

            saveAuditForUpdate(leaveId, managerId, "Rejected");

            return new Response(true, "Rejected");
        }

        return new Response(false, "Leave_id doesn't exist");
    }









    // Audit when leave applied
    private void saveAuditForApply(Integer leaveId, Integer employeeId) {

        AuditDetails audit = new AuditDetails();

        audit.setLeave(leaveId);
        audit.setCreatedById(employeeId);


        auditRepository.save(audit); // using @Prepersist
    }

    // Audit when leave updated (approved/rejected/revoked)
    private void saveAuditForUpdate(Integer leaveId,
                                    Integer updatedById,
                                    String action) {

        AuditDetails audit = auditRepository.findByLeave(leaveId);

        if (audit == null) {
            throw new AuditRecordNotFoundException("Audit record not found for leave id: " + leaveId);
        }

        audit.setUpdatedById(updatedById);
        audit.setUpdatedAction(action);
        // using @PreUpdate for updating the timestamp

        auditRepository.save(audit);
    }




}
