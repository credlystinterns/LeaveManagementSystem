package com.example.lmsapplication.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LeaveTypes {
    CASUAL,
    SICK,
    WFH ;


    @JsonCreator
    public static LeaveTypes from(String value) {
        return LeaveTypes.valueOf(value.toUpperCase());
    }
}
