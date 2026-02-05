package com.example.lmsapplication.requisites;

import com.example.lmsapplication.tables.Leaves;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveHistory {
    private Integer employeeId;
    private List<Leaves> allLeaves ;
}
