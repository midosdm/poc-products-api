package com.api.pocproductsapi.service;

import com.api.pocproductsapi.dto.cart.AddProductToCartRequest;
import com.api.pocproductsapi.dto.cart.CartItemResponse;
import com.api.pocproductsapi.dto.cart.CartResponse;
import com.api.pocproductsapi.dto.cart.UpdateCartItemQuantityRequest;
import com.api.pocproductsapi.entity.Cart;
import com.api.pocproductsapi.entity.CartItem;
import com.api.pocproductsapi.entity.Product;
import com.api.pocproductsapi.entity.User;
import com.api.pocproductsapi.exception.InsufficientStockException;
import com.api.pocproductsapi.repository.CartRepository;
import com.api.pocproductsapi.repository.ProductRepository;
import com.api.pocproductsapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartResponse getCart() {
        log.info("Fetching cart");
        Cart cart = getOrCreateCart();

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getCode(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .toList();

        BigDecimal totalPrice =
                items.stream().map(CartItemResponse::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, totalPrice);
    }

    public void addProduct(Integer productId, AddProductToCartRequest request) {

        log.info("Adding product with id: {} to cart", productId);
        Cart cart = getOrCreateCart();

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Long availableQuantity = product.getQuantity();

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            log.info("Product already exists in cart, updating quantity");
            validateStock((existingItem.getQuantity() + request.getQuantity()), availableQuantity, product.getCode());
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());

        } else {
            log.info("Product not found in cart, adding new item");
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            validateStock(request.getQuantity(), availableQuantity, product.getCode());
            item.setQuantity(request.getQuantity());

            cart.getItems().add(item);
        }

        cartRepository.save(cart);
    }

    public void removeProduct(Integer productId) {
        log.info("Removing product with id: {} from cart", productId);
        Cart cart = getOrCreateCart();
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {

        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();

        log.info("Fetching cart for user: {} if it already exists", email);

        return cartRepository.findByUserEmail(email).orElseGet(() -> {
            User user =
                    userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

            log.info("Creating new cart for user: {}", email);

            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    public void updateProductQuantity(Integer productId, UpdateCartItemQuantityRequest request) {
        Cart cart = getOrCreateCart();

        log.info("Updating product quantity in cart for product with id: {}", productId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found in cart"));

        Long availableQuantity = cartItem.getProduct().getQuantity();

        if (request.getQuantity() == 0) {
            log.info("Quantity is 0, removing product with id: {} from cart", productId);
            cart.getItems().remove(cartItem);

        } else {
            validateStock(
                    request.getQuantity(),
                    availableQuantity,
                    cartItem.getProduct().getCode());
            cartItem.setQuantity(request.getQuantity());
        }

        cartRepository.save(cart);
    }

    private void validateStock(Long requested, Long available, String productCode) {
        if (requested > available) {
            throw new InsufficientStockException(available, productCode);
        }
    }
}
