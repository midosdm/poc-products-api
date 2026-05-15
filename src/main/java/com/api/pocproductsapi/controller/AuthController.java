package com.api.pocproductsapi.controller;

import com.api.pocproductsapi.CommonControllerAdvice;
import com.api.pocproductsapi.dto.auth.CreateAccountRequest;
import com.api.pocproductsapi.dto.auth.LoginRequest;
import com.api.pocproductsapi.dto.auth.LoginResponse;
import com.api.pocproductsapi.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user", description = "Registers a new user")
    @PostMapping("/account")
    public void register(@RequestBody @Valid CreateAccountRequest request) {
        authService.register(request);
    }

    @Operation(summary = "User Login", description = "Logs in user with the given email and password")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonControllerAdvice.ErrorResponse.class))
                        }),
            })
    @PostMapping("/token")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
