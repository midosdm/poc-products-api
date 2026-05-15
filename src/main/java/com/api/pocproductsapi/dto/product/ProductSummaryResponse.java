package com.api.pocproductsapi.dto.product;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSummaryResponse {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private Long quantity;
    private Integer rating;
}
