package com.example.lmsapplication.response;

import com.example.lmsapplication.requisites.AvailableAction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse{
    private String key;
    private String href;
    private String token;
    s

    private List<AvailableAction> availableActionList;
}

