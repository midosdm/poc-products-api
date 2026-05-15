package com.api.pocproductsapi.exception;

import java.text.MessageFormat;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends AbstractApiException {
    public InsufficientStockException(Long quantity, String code) {
        super(
                HttpStatus.BAD_REQUEST,
                MessageFormat.format(
                        "Only ''{0}'' items are left in stock for the product with code ''{1}'': ", quantity, code));
    }
}
