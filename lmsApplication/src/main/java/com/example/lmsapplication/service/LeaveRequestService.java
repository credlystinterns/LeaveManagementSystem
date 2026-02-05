package com.example.lmsapplication.service;

import com.example.lmsapplication.audit.AuditDetails;
import com.example.lmsapplication.dto.AuditRepository;
import com.example.lmsapplication.dto.LeaveRequestRepository;
import com.example.lmsapplication.response.RevokeResponse;
import com.example.lmsapplication.tables.Leaves;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

   public Response applyLeave(Leaves leaverequest)  {
       Integer employee_Id = leaverequest.getEmployee_Id();
       Employee emp = getEmployeeById(employee_Id);

       String leave_type =  leaverequest.getLeaveType();
       Integer casual_leave = getCasualLeavesCount(emp);
       Integer sick_leave = getSickLeavesCount(emp);
       Integer wfh_leave = getWFHLeavesCount(emp);

       if (leaverequest.getStart_date().after(leaverequest.getEnd_date())) {
           return new Response(false, "Start date cannot be after end date");
       }

       // Default Status
       leaverequest.setStatus("Pending");

       // Calculation of Leave Days

       long diffInMillis = leaverequest.getEnd_date().getTime()
               - leaverequest.getStart_date().getTime();

       int leaveDays = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;




       if(leave_type.equalsIgnoreCase("casual") ){

            if(casual_leave >= leaveDays){
                casual_leave = casual_leave - leaveDays;
                emp.setCasualLeave(casual_leave);
                employeeRepo.save(emp);
           Leaves savedLeave  =    leaveRequestRepository.save(leaverequest);

                saveAuditForApply(savedLeave.getLeave_Id(), employee_Id);


                return new Response(true , "Casual Leave Applied Successfully , Remaining Casual Leaves are : "+casual_leave) ;

            }else{
                return new Response(false, "Your Casual Leave limit is Exceeded") ;
            }
       }else if(leave_type.equalsIgnoreCase("wfh")){
           if(wfh_leave >= leaveDays){
               wfh_leave  = wfh_leave -  leaveDays;
               emp.setWfhLeave(wfh_leave);
               employeeRepo.save(emp);
               Leaves savedLeave  =    leaveRequestRepository.save(leaverequest);

               saveAuditForApply(savedLeave.getLeave_Id(), employee_Id);



               return new Response(true , "Work From Home Leave Applied Successfully , Remaining Work From Home Leaves are : "+wfh_leave) ;

           }else{
               return new Response(false , "Your Work From Home Leave limit is Exceeded");
           }
       }else if(leave_type.equalsIgnoreCase("sick")){
           if(sick_leave >= leaveDays){
               sick_leave = sick_leave -   leaveDays;
               emp.setSickLeave(sick_leave);
               employeeRepo.save(emp);
               Leaves savedLeave  =  leaveRequestRepository.save(leaverequest);

               saveAuditForApply(savedLeave.getLeave_Id(), employee_Id);



               return new Response(true,"Your Sick Leave is Applied Successfully , Remaining Sick Leave Limit are : " + sick_leave) ;
           }else{
               return new Response(false , "Your Sick  Leave limit is Exceeded");
           }
       }else{
           return new Response(false ,"Invalid Leave Type");
       }


   }
    public Employee getEmployeeById(Integer employee_Id) {

        return EmployeeRepo.findById(employee_Id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
   public Integer getCasualLeavesCount(Employee employee){
       return employee.getCasualLeave() ;
   }
   public Integer getSickLeavesCount(Employee employee){
       return employee.getSickLeave();
   }
   public Integer getWFHLeavesCount(Employee employee){
       return employee.getWfhLeave();
   }


// leave history
   public List<Leaves> getLeaves(Integer employee_Id){
       List<Leaves> leaves = leaveRequestRepository.findLeavesByEmployee_Id(employee_Id);
        return leaves;
   }

   public RevokeResponse revokeLeave(Integer employee_Id){


       List<Leaves>employee_Leaves =  leaveRequestRepository.findLeavesByEmployee_Id(employee_Id);

       if (employee_Leaves.isEmpty()) {
           throw new RuntimeException("No leaves found for employee id: " + employee_Id);
       }

       RevokeResponse response = new RevokeResponse();

       response.setKey("Revoke");
       response.setHref("/revoke");
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

       long diffInMillis = leave.getEnd_date().getTime()
               - leave.getStart_date().getTime();

       int leaveDays = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;



          Integer employee_Id = leave.getEmployee_Id();

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

    // Audit when leave applied
    private void saveAuditForApply(Integer leave_Id, Integer employee_Id) {

        AuditDetails audit = new AuditDetails();

        audit.setLeave_Id(leave_Id);
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

        AuditDetails audit = auditRepository.findByLeave_Id(leave_Id);

        if (audit == null) {
            throw new RuntimeException("Audit record not found for leave id: " + leave_Id);
        }

        audit.setUpdatedById(updatedById);
        audit.setUpdatedAction(action);
        audit.setUpdatedAt(LocalDateTime.now());

        auditRepository.save(audit);
    }




}
