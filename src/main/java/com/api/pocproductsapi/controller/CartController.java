package com.api.pocproductsapi.controller;

import com.api.pocproductsapi.dto.cart.AddProductToCartRequest;
import com.api.pocproductsapi.dto.cart.CartResponse;
import com.api.pocproductsapi.dto.cart.UpdateCartItemQuantityRequest;
import com.api.pocproductsapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Carts API")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get current user cart")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved")})
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add product to cart")
    public void addProduct(@PathVariable Integer productId, @Valid @RequestBody AddProductToCartRequest request) {
        cartService.addProduct(productId, request);
    }

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove product from cart")
    public void removeProduct(@PathVariable Integer productId) {

        cartService.removeProduct(productId);
    }

    @PutMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update cart product quantity")
    public void updateProductQuantity(
            @PathVariable Integer productId, @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        cartService.updateProductQuantity(productId, request);
    }
}
