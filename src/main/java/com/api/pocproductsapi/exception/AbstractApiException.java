package com.api.pocproductsapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractApiException extends RuntimeException {

    private final HttpStatus status;

    protected AbstractApiException(HttpStatus status, String message) {

        super(message);

        this.status = status;
    }
}
