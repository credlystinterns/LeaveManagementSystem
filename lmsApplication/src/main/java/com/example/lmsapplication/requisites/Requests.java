package com.example.lmsapplication.requisites;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Requests {

    APPLY_LEAVE(
            "apply_leave",
            "POST",
            "/apply_leave",
            Map.of(
                    "type", Map.of("type", "string", "string", List.of("casual", "sick", "wfh")),
                    "startDate", Map.of("type", "date", "format", "YYYY-MM-DD"),
                    "endDate", Map.of("type", "date", "format", "YYYY-MM-DD")
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
            "/accept",
            null
    ),
    REJECT(
            "reject request",
            "GET",
            "/reject",
            null
    );

    private final String key;
    private final String method;
    private final String href;
    private final Map<String,Object>body;



}
