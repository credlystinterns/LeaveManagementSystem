package com.example.lmsapplication.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AcceptResponse {
    String key;
    String href;
    String method;
    List<Leaves> leaves;
}
