package com.example.lmsapplication.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse{
    private String key;
    private String href;
    private String token;

    public record AvailableAction(
            String key,
            String href,
            String method
    ){}

    private List<AvailableAction> availableActionList;
}

