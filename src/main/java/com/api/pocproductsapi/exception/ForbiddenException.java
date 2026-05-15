package com.api.pocproductsapi.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractApiException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "Access denied");
    }
}
