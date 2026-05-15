package com.api.pocproductsapi.service;

import com.api.pocproductsapi.dto.product.ProductSummaryResponse;
import com.api.pocproductsapi.dto.wishlist.WishlistResponse;
import com.api.pocproductsapi.entity.Product;
import com.api.pocproductsapi.entity.User;
import com.api.pocproductsapi.entity.Wishlist;
import com.api.pocproductsapi.repository.ProductRepository;
import com.api.pocproductsapi.repository.UserRepository;
import com.api.pocproductsapi.repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistResponse getWishlist() {
        log.info("Fetching wishlist");
        Wishlist wishlist = getOrCreateWishlist();

        List<ProductSummaryResponse> products = wishlist.getProducts().stream()
                .map(product -> new ProductSummaryResponse(
                        product.getId(),
                        product.getName(),
                        product.getCode(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getImage(),
                        product.getQuantity(),
                        product.getRating()))
                .toList();

        return new WishlistResponse(products);
    }

    public void addProduct(Integer productId) {

        log.info("Adding product with id: {}", productId);
        Wishlist wishlist = getOrCreateWishlist();

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        boolean alreadyExists = wishlist.getProducts().stream()
                .anyMatch(existingProduct -> existingProduct.getId().equals(productId));

        if (!alreadyExists) {
            log.info("Product not found in wishlist, adding it");
            wishlist.getProducts().add(product);
            wishlistRepository.save(wishlist);
        }
    }

    public void removeProduct(Integer productId) {

        log.info("Removing product with id: {}", productId);
        Wishlist wishlist = getOrCreateWishlist();

        wishlist.getProducts().removeIf(product -> product.getId().equals(productId));

        wishlistRepository.save(wishlist);
    }

    private Wishlist getOrCreateWishlist() {

        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();

        log.info("Fetching wishlist for user: {} if it already exists", email);

        return wishlistRepository.findByUserEmail(email).orElseGet(() -> {
            User user =
                    userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

            log.info("Creating new wishlist for user: {}", user.getEmail());
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user);

            return wishlistRepository.save(wishlist);
        });
    }
}
