package com.api.pocproductsapi.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCartItemQuantityRequest {
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater or equal to 0")
    private Long quantity;
}
