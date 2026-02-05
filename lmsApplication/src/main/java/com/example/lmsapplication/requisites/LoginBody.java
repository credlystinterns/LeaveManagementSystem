package com.example.lmsapplication.requisites;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginBody {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8 , message = "Password should be atleast of Length 8.")
    @NotBlank
    private String password;
}
