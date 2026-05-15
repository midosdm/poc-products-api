package com.api.pocproductsapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AbstractApiException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
}
