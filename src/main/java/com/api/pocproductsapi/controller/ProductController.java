package com.api.pocproductsapi.controller;

import com.api.pocproductsapi.CommonControllerAdvice;
import com.api.pocproductsapi.dto.product.CreateProductRequest;
import com.api.pocproductsapi.dto.product.ProductResponse;
import com.api.pocproductsapi.dto.product.ProductSummaryResponse;
import com.api.pocproductsapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products API")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(
            summary = "List products with search value",
            description =
                    "Returns the list of all products summaries matching the search value and according to the page number")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved")})
    @GetMapping
    public Page<ProductSummaryResponse> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return service.getProducts(search, category, pageable);
    }

    @Operation(summary = "Get product information", description = "Retrieves all information of a product")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "The requested product does not exist",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonControllerAdvice.ErrorResponse.class))
                        }),
            })
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // TODO: ADD AN UPDATE PRODUCT ENDPOINT
    //    @PutMapping("/{id}")
    //    public ProductResponse updateProduct(@PathVariable Integer id) {
    //        return service.updateProduct(id);
    //    }

    @Operation(summary = "Create new product", description = "Creates a new product")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Successfully created"),
                @ApiResponse(
                        responseCode = "400",
                        description = "There is a validation error",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonControllerAdvice.ErrorResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "409",
                        description = "A product with this code already exists",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonControllerAdvice.ErrorResponse.class))
                        }),
            })
    @PostMapping
    public ProductResponse createProduct(
            @RequestBody @Valid CreateProductRequest request, Authentication authentication) {
        return service.create(request, authentication.getName());
    }

    @Operation(summary = "Delete Product", description = "Deletes the product with the given id")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Successfully deleted"),
                @ApiResponse(
                        responseCode = "404",
                        description = "The requested product does not exist",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonControllerAdvice.ErrorResponse.class))
                        }),
            })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(
            final @Parameter(
                            name = "id",
                            description = "The ID of the user account to delete",
                            examples = {@ExampleObject(value = "1")}) @PathVariable Integer id,
            Authentication authentication) {
        service.deleteProduct(id, authentication.getName());
    }
}
