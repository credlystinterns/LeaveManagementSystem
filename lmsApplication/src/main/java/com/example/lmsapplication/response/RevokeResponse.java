package com.example.lmsapplication.response;

import com.example.lmsapplication.dto.LeaveRequestRepository;
import com.example.lmsapplication.tables.Leaves;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevokeResponse {
   String key;
   String href;
   String method;
   List<Leaves> leaves;




}
