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
public class SignUpBody {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8 , message = "It should be of length 8.")
    @NotBlank
    private String password;

    @NotBlank
    private String managerName;
}
