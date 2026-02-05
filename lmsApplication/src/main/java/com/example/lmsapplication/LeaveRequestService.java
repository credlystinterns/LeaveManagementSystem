package com.example.lmsapplication;
import com.example.lmsapplication.response.AcceptResponse;
import com.example.lmsapplication.response.RejectResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class LeaveRequestService {
    @Autowired
    Employeerepo employeerepo;
    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    public List<Employee> getReportees(int manager_id) {
        return employeeRepo.findEmployeesByManagerId(manager_id);
    }

    public List<Employee> acceptResponse(int manager_id){

        AcceptResponse acceptResponse = new AcceptResponse();
        acceptResponse.setKey("Accepted");
        acceptResponse.setHref("/accept/{leave_id}");
        acceptResponse.setLeaves(employeeRepo.findEmployeesByManagerId(manager_id));
        return acceptResponse(manager_id);
    }

    public List<Employee> acceptance(int leave_id, int manager_id){
        Leaves leaves = leaveRequestRepository.findById(leave_id);
        leaves.setStatus("Approved");
        saveAuditForUpdate(leave_id,
                manager_id,
                "Approved");
        return new Response(true,"Accepted");
    }

    public List<Employee> rejectResponse(int manager_id){

        RejectResponse rejectResponse = new RejectResponse();
        rejectResponse.setKey("Rejected");
        rejectResponse.setHref("/reject/{leave_id}");
        rejectResponse.setLeaves(employeerepo.findEmployeesByManagerId(manager_id));
        return rejectResponse(manager_id);
    }

    public List<Employee> rejectance(int leave_id, int manager_id){
        Leaves leaves = leaveRequestRepository.findById(leave_id);
        leaves.setStatus("Rejected");
        saveAuditForUpdate(leave_id,
                manager_id,
                "Rejected");
        return new Response(true,"Rejected");
    }


    public List<Leaves> getLeaves(int employee_id){
        return leaveRequestRepository.findLeavesByEmployeeId(employee_id);
    }



}

