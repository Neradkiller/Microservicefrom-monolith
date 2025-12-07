package com.non_real_ecommerce.product_service.infrastructure.controller;

import com.non_real_ecommerce.product_service.domain.model.Product;
import com.non_real_ecommerce.product_service.domain.port.input.ProductService;
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
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<Page<ProductResponse>> getAvailableProducts(
            Pageable pageable) {
        log.info("Fetching available products (stock > 0)");

        Page<Product> products = productService.getAvailableProducts(pageable);

        Page<ProductResponse> response = products.map(this::toProductResponse);

        log.info("Returning {} available products", products.getContent().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock/{minStock}")
    public ResponseEntity<Page<Product>> getProductsWithStockGreaterThan(
            @PathVariable @Min(0) Long minStock,
            Pageable pageable) {

        log.info("Fetching products with stock greater than: {}", minStock);

        var products = productService.getProductsWithStockGreaterThan(minStock, pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(toResponse(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable String category,
            Pageable pageable) {
        var products = productService.getProductsByCategory(category, pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String q,
            Pageable pageable) {
        var products = productService.searchProducts(q, pageable);

        return ResponseEntity.ok(products);
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
