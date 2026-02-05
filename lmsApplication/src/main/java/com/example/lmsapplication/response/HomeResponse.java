package com.example.lmsapplication.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class HomeResponse {

    @Getter
    @AllArgsConstructor
    public enum Abc{
        LOGIN(
                "login",
                "/login",
                "POST",
                Map.of(
                    "schema" ,Map.of("email","String","Password","String")
                )
        ),
        SIGNUP(
                "signup",
                "/signup",
                "POST",
                Map.of(
                       "schema",Map.of("name","String","email","String","Password","String","Manager Name","String")
                )
        ),
        LOGOUT(
                "logout",
                "/logout",
                "DELETE",
                Map.of(
                        "message","Session Ended"
                )
        );


        private final String key;
        private final String href;
        private final String method;
        private final Map<String,Object>actions;

    }

    private List<Abc> actions;
}
