package com.api.pocproductsapi.dto.wishlist;

import com.api.pocproductsapi.dto.product.ProductSummaryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WishlistResponse {
    private List<ProductSummaryResponse> products;
}
