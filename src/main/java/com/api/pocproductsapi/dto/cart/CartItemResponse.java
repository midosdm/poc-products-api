package com.api.pocproductsapi.dto.cart;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartItemResponse {
    private Integer productId;

    private String productCode;

    private String productName;

    private BigDecimal price;

    private Long quantity;

    private BigDecimal totalPrice;
}
