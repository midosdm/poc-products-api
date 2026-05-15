package com.api.pocproductsapi.exception;

import java.text.MessageFormat;
import org.springframework.http.HttpStatus;

public class ProductCodeExistsException extends AbstractApiException {
    public ProductCodeExistsException(String code) {
        super(HttpStatus.BAD_REQUEST, MessageFormat.format("A product with code ''{0}'' already exists", code));
    }
}
