package com.example.lmsapplication.requisites;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Requests {

    APPLY_LEAVE(
            "apply_leave",
            "POST",
            "/leave_requests",
            Map.of(
                    "type", Map.of("type", "string", "enum", List.of("casual", "sick", "earned")),
                    "startDate", Map.of("type", "string", "format", "date"),
                    "endDate", Map.of("type", "string", "format", "date"),
                    "reason", Map.of("type", "string")
            )
    ),
    VIEW_HISTORY(
            "view_history",
            "GET",
            "//history",
            null
    ),
    REVOKE_LEAVE(
            "revoke leave",
            "PUT",
            "/revoke_leave",
            null
    ),
    LEAVE_HISTORY_OF_REPORTEES(
            "leave history of reportees",
            "PUT",
            "/view_own_leavehistory",
            null
    ),
    ACCEPT(
            "accept request",
            "GET",
            "/accept_request",
            null
    ),
    REJECT(
            "reject request",
            "GET",
            "/reject_request",
            null
    );

    private final String key;
    private final String method;
    private final String href;
    private final Map<String,Object>actions;



}
