package com.example.lmsapplication.controller;

import com.example.lmsapplication.requisites.ChangeRequest;
import com.example.lmsapplication.requisites.LeaveRequest;
import com.example.lmsapplication.requisites.LeaveHistory;
import com.example.lmsapplication.requisites.Requests;
import com.example.lmsapplication.response.AcceptResponse;
import com.example.lmsapplication.response.MainResponse;
import com.example.lmsapplication.response.RejectResponse;
import com.example.lmsapplication.response.RevokeResponse;
import com.example.lmsapplication.service.FetchEmployee;
import com.example.lmsapplication.service.LeaveRequestService;
import com.example.lmsapplication.service.UpdateService;
import com.example.lmsapplication.tables.Employee;
import com.example.lmsapplication.tables.Leaves;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveRequests")
public class MainController {

    private final LeaveRequestService leaveRequestService;
    private final FetchEmployee fetchEmployee;

    private final UpdateService updateService;

    public MainController(LeaveRequestService leaveRequestService, FetchEmployee fetchEmployee,UpdateService updateService) {
        this.leaveRequestService = leaveRequestService;
        this.fetchEmployee = fetchEmployee;
        this.updateService = updateService;

    }

    private Employee requireEmployee(String authorization) {
        return fetchEmployee.getEmployee(authorization);
    }

    @GetMapping("")
    public MainResponse res(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return new MainResponse(
                employee.getEmployeeId(),
                List.of(Requests.values())
        );
    }

    @GetMapping("/history")
    public LeaveHistory getLeaveHistory(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return LeaveHistory.builder()
                .employeeId(employee.getEmployeeId())
                .allLeaves(leaveRequestService.getLeaves(employee.getEmployeeId()))
                .build();

    }

    @PostMapping("/apply")
    public LeaveRequestService.Response applyLeave(@RequestHeader("Authorization") String authorization,
                               @RequestBody @Valid LeaveRequest leaveRequest) {
        Employee employee  = requireEmployee(authorization);
        return leaveRequestService.applyLeave(employee.getEmployeeId(),leaveRequest);
    }

    @GetMapping("/revoke")
    public RevokeResponse revoked(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return leaveRequestService.revokeLeave(employee.getEmployeeId());
    }

    @PutMapping("/revoke/{leaveId}")
    public LeaveRequestService.Response revokeLeave(@RequestHeader("Authorization") String authorization,
                                @PathVariable Integer leaveId) {
        requireEmployee(authorization);
        return leaveRequestService.revokeCompletion(leaveId);
    }

    @GetMapping("/reporteeHistory")
    public List<Leaves> getLeaveHistoryReportees(
            @RequestHeader("Authorization") String authorization) {


        Employee manager = requireEmployee(authorization);

        List<Employee> reportees =
                leaveRequestService.getReportees(manager.getEmployeeId());

        return reportees.stream()
                .map(emp -> leaveRequestService.getLeaves(emp.getEmployeeId()))
                .flatMap(List::stream)
                .toList();
    }


    @GetMapping("/accept")
    public AcceptResponse accept(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.accepted(manager.getEmployeeId());
    }

    @PutMapping("/accept/{leaveId}")
    public LeaveRequestService.Response acceptance(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable Integer leaveId) {
        Employee employee  = requireEmployee(authorization);
        return leaveRequestService.acceptance(leaveId, employee.getEmployeeId());
    }

    @GetMapping("/reject")
    public RejectResponse reject(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.rejected(manager.getEmployeeId());
    }

    @PutMapping("/reject/{leaveId}")
    public LeaveRequestService.Response rejection(@RequestHeader("Authorization") String authorization,
                                                  @PathVariable Integer leaveId) {
        Employee employee = requireEmployee(authorization);
        return leaveRequestService.rejectance(leaveId,employee.getEmployeeId());
    }

    @PostMapping("/designation")
    public ResponseEntity<String> updateDesignation(@RequestHeader("Authorization")String token, @RequestBody ChangeRequest changeDepartmentRequest){
        Employee employee = requireEmployee(token);
        return ResponseEntity.ok(updateService.updateDesignation(employee,changeDepartmentRequest.getName()));
    }

    @PostMapping("/techstack")
    public ResponseEntity<String> updateTechStack(@RequestHeader("Authorization")String token,@RequestBody ChangeRequest changeTechStackRequest){
        Employee employee = requireEmployee(token);
        return ResponseEntity.ok(updateService.updateTechStack(employee,changeTechStackRequest.getName()));
    }
}
