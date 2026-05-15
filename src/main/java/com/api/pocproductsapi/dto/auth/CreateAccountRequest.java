package com.api.pocproductsapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Firstname cannot be blank")
    private String firstname;

    @Email
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(min = 6)
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
