package com.example.lmsapplication.requisites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveHistory {
    private Integer employeeId;
    private List<Leaves> allLeaves = new ArrayList<Leaves>();
}
