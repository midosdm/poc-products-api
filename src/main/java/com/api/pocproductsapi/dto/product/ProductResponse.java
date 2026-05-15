package com.api.pocproductsapi.dto.product;

import com.api.pocproductsapi.entity.InventoryStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private String image;
    private BigDecimal price;
    private String category;
    private Long quantity;
    private String internalReference;
    private Long shellId;
    private InventoryStatus inventoryStatus;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
