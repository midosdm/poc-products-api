package com.api.pocproductsapi.service;

import com.api.pocproductsapi.dto.product.CreateProductRequest;
import com.api.pocproductsapi.dto.product.ProductResponse;
import com.api.pocproductsapi.dto.product.ProductSummaryResponse;
import com.api.pocproductsapi.entity.Product;
import com.api.pocproductsapi.entity.User;
import com.api.pocproductsapi.exception.ForbiddenException;
import com.api.pocproductsapi.exception.ProductCodeExistsException;
import com.api.pocproductsapi.repository.ProductRepository;
import com.api.pocproductsapi.repository.UserRepository;
import com.api.pocproductsapi.service.auth.AuthorizationService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    public Page<ProductSummaryResponse> getProducts(String search, String category, Pageable pageable) {
        log.info("Fetching products with search: {}, category: {}", search, category);
        return repository.listProducts(search == null ? "" : search, category == null ? "" : category, pageable);
    }

    public ProductResponse getById(Integer id) {

        log.info("Fetching product with id: {}", id);
        Product product = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request, String email) {
        User user = getUser(email);

        log.info("Checking user's authority");
        if (!authorizationService.isAdmin(user)) {
            log.error("User {} is not an admin", email);
            throw new ForbiddenException();
        }

        if (repository.existsByCode(request.getCode())) {
            throw new ProductCodeExistsException("Product with code already exists");
        }

        log.info("Creating product with name: {}", request.getName());
        Product product = Product.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .price(request.getPrice())
                .image(request.getImageUr())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(product);

        return toResponse(product);
    }

    private User getUser(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email).orElseThrow();
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getDescription(),
                product.getImage(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity(),
                product.getInternalReference(),
                product.getShellId(),
                product.getInventoryStatus(),
                product.getRating(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    public void deleteProduct(Integer productId, String email) {

        log.info("Checking user's authority");
        User user = getUser(email);
        if (!authorizationService.isAdmin(user)) {
            log.error("User {} is not an admin", email);
            throw new ForbiddenException();
        }

        log.info("Deleting product with id: '{}' ", productId);

        repository.deleteById(productId);
    }

    private ProductSummaryResponse toSummaryResponse(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getName(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getImage(),
                product.getQuantity(),
                product.getRating());
    }
}
