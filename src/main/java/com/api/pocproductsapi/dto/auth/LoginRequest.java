package com.api.pocproductsapi.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
