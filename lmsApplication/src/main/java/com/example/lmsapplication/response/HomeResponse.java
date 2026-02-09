package com.example.lmsapplication.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor

public class HomeResponse {

    @Getter
    @AllArgsConstructor
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Abc{
        LOGIN(
                "login",
                "/login",
                "POST",
                Map.of(
                    "schema" ,Map.of("email","string","password","string")
                )
        ),
        SIGNUP(
                "signup",
                "/signup",
                "POST",
                Map.of(
                       "schema",Map.of("name","string","email","string","password","string","managerName","string")
                )
        ),
        LOGOUT(
                "logout",
                "/logout",
                "DELETE",
                Map.of(
                        "message","Session will be ended."
                )
        );


        private final String key;
        private final String href;
        private final String method;
        private final Map<String,Object>body;

    }

    private List<Abc> actions;
}
