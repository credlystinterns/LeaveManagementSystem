package com.example.lmsapplication.controller;

import com.example.lmsapplication.requisites.LeaveRequest;
import com.example.lmsapplication.requisites.LeaveHistory;
import com.example.lmsapplication.requisites.Requests;
import com.example.lmsapplication.response.AcceptResponse;
import com.example.lmsapplication.response.MainResponse;
import com.example.lmsapplication.response.RejectResponse;
import com.example.lmsapplication.response.RevokeResponse;
import com.example.lmsapplication.service.FetchEmployee;
import com.example.lmsapplication.service.LeaveRequestService;
import com.example.lmsapplication.tables.Employee;
import com.example.lmsapplication.tables.Leaves;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/leave_requests")
public class MainController {

    private final LeaveRequestService leaveRequestService;
    private final FetchEmployee fetchEmployee;


    public MainController(LeaveRequestService leaveRequestService, FetchEmployee fetchEmployee) {
        this.leaveRequestService = leaveRequestService;
        this.fetchEmployee = fetchEmployee;

    }

    private Employee requireEmployee(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new RuntimeException("Authorization header missing");
        }
        Employee employee = fetchEmployee.getEmployee(authorization);
        if (employee == null) {
            throw new RuntimeException("Invalid or expired token");
        }
        return employee;
    }

    @GetMapping("")
    public MainResponse res(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return new MainResponse(
                employee.getEmployeeId(),
                List.of(Requests.values())
        );
    }

    @GetMapping("/view_own_leavehistory")
    public LeaveHistory getLeaveHistory(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return LeaveHistory.builder()
                .employeeId(employee.getEmployeeId())
                .allLeaves(leaveRequestService.getLeaves(employee.getEmployeeId()))
                .build();

    }

    @PostMapping("/apply_leave")
    public LeaveRequestService.Response applyLeave(@RequestHeader("Authorization") String authorization,
                               @RequestBody LeaveRequest leaveRequest) {
        Employee employee  = requireEmployee(authorization);
        return leaveRequestService.applyLeave(employee.getEmployeeId(),leaveRequest);
    }

    @GetMapping("/revoke_leave")
    public RevokeResponse revoked(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return leaveRequestService.revokeLeave(employee.getEmployeeId());
    }

    @PutMapping("/revoke_leave/{leave_id}")
    public LeaveRequestService.Response revokeLeave(@RequestHeader("Authorization") String authorization,
                                @PathVariable Integer leave_id) {
        requireEmployee(authorization);
        return leaveRequestService.revokeCompletion(leave_id);
    }

    @GetMapping("/view_history_reportees")
    public List<Leaves> getLeaveHistoryReportees(
            @RequestHeader("Authorization") String authorization) {


        Employee manager = requireEmployee(authorization);

        List<Employee> reportees =
                leaveRequestService.getReportees(manager.getEmployeeId());

        return reportees.stream()
                .map(emp -> leaveRequestService.getLeaves(emp.getEmployeeId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    @GetMapping("/accept")
    public AcceptResponse accept(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.accepted(manager.getEmployeeId());
    }

    @PutMapping("/accept/{leave_id}")
    public LeaveRequestService.Response acceptance(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable Integer leave_id) {
        Employee employee  = requireEmployee(authorization);
        return leaveRequestService.acceptance(leave_id, employee.getEmployeeId());
    }

    @GetMapping("/reject")
    public RejectResponse reject(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.rejected(manager.getEmployeeId());
    }

    @PutMapping("/reject/{leave_id}")
    public LeaveRequestService.Response rejection(@RequestHeader("Authorization") String authorization,
                                                  @PathVariable Integer leave_id) {
        Employee employee = requireEmployee(authorization);
        return leaveRequestService.rejectance(leave_id,employee.getEmployeeId());
    }
}
