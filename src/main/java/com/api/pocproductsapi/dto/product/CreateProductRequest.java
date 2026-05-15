package com.api.pocproductsapi.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String imageUr;
}
