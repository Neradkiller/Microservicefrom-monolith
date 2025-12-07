package com.non_real_ecommerce.product_service.infrastructure.controller;

import com.non_real_ecommerce.product_service.domain.model.Product;
import com.non_real_ecommerce.product_service.domain.port.input.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@Slf4j
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductResponse> response = products
                .map(this::toProductResponse);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        var product = productService.createProduct(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.category()
        );

        var response = toResponse(product);
        return ResponseEntity.created(URI.create("/api/products/" + product.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        var product = productService.updateProduct(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.category()
        );

        return ResponseEntity.ok(toResponse(product));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request) {

        var product = productService.updateStock(id, request.stock());
        return ResponseEntity.ok(toResponse(product));
    }

    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<ProductResponse> reduceStock(
            @PathVariable Long id,
            @Valid @RequestBody ReduceStockRequest request) {

        var product = productService.reduceStock(id, request.quantity());
        return ResponseEntity.ok(toResponse(product));
    }



    @PostMapping("/{id}/increase-stock")
    public ResponseEntity<ProductResponse> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody IncreaseStockRequest request) {

        var product = productService.increaseStock(id, request.quantity());
        return ResponseEntity.ok(toResponse(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getStatus().name(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.isAvailable()
        );
    }

    public record CreateProductRequest(
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin("0.01") BigDecimal price,
            @Min(0) Long stock,
            String category
    ) {}

    public record UpdateProductRequest(
            @NotBlank String name,
            String description,
            @NotNull @DecimalMin("0.01") BigDecimal price,
            String category
    ) {}

    public record UpdateStockRequest(@Min(0) Long stock) {}
    public record ReduceStockRequest(@Min(1) Long quantity) {}
    public record IncreaseStockRequest(@Min(1) Long quantity) {}

    public record ProductResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Long stock,
            String category,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean available
    ) {}

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getStatus().name(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.isAvailable()
        );
    }
}
