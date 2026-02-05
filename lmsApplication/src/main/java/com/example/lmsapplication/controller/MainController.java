package com.example.lmsapplication.controller;

import com.example.lmsapplication.requisites.LeaveHistory;
import com.example.lmsapplication.requisites.Requests;
import com.example.lmsapplication.response.MainResponse;
import com.example.lmsapplication.service.FetchEmployee;
import com.example.lmsapplication.service.MainService;
import com.example.lmsapplication.tables.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave_requests")
public class MainController {

    private final LeaveRequestService leaveRequestService;
    private final FetchEmployee fetchEmployee;
    private final MainService mainService;

    public MainController(LeaveRequestService leaveRequestService, FetchEmployee fetchEmployee,MainService mainService) {
        this.leaveRequestService = leaveRequestService;
        this.fetchEmployee = fetchEmployee;
        this.mainService = mainService;
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

    @GetMapping
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
    public Response applyLeave(@RequestHeader("Authorization") String authorization,
                               @RequestBody Leaves leaveRequest) {
        requireEmployee(authorization);
        return leaveRequestService.applyLeave(leaveRequest);
    }

    @GetMapping("/revoke_leave")
    public RevokeResponse revoked(@RequestHeader("Authorization") String authorization) {
        Employee employee = requireEmployee(authorization);
        return leaveRequestService.revokeLeave(employee.getEmployeeId());
    }

    @PutMapping("/revoke_leave/{leave_id}")
    public Response revokeLeave(@RequestHeader("Authorization") String authorization,
                                @PathVariable Integer leave_id) {
        requireEmployee(authorization);
        return leaveRequestService.revokeCompletion(leave_id);
    }

    @GetMapping("/view_history_reportees")
    public List<Leaves> getLeaveHistoryReportees(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        List<Employee> reportees = leaveRequestService.getReportees(manager.getEmployeeId());
        return reportees.stream()
                .map(x -> leaveRequestService.getLeaves(x.getEmployeeId()))
                .toList();
    }

    @GetMapping("/accept")
    public AcceptResponse accept(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.acceptRequest(manager.getEmployeeId());
    }

    @PutMapping("/accept/{leave_id}")
    public Response acceptance(@RequestHeader("Authorization") String authorization,
                               @PathVariable Integer leave_id) {
        requireEmployee(authorization);
        return leaveRequestService.acceptance(leave_id);
    }

    @GetMapping("/reject")
    public RejectResponse reject(@RequestHeader("Authorization") String authorization) {
        Employee manager = requireEmployee(authorization);
        return leaveRequestService.reject(manager.getEmployeeId());
    }

    @PutMapping("/reject/{leave_id}")
    public Response rejection(@RequestHeader("Authorization") String authorization,
                              @PathVariable Integer leave_id) {
        requireEmployee(authorization);
        return leaveRequestService.rejected(leave_id);
    }
}
