package com.example.lmsapplication.response;


import com.example.lmsapplication.requisites.Requests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainResponse {
    private Integer employeeId;
    private List<Requests> availableRequests;
}
