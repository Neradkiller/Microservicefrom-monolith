package com.non_real_ecommerce.product_service.domain.port.output;

import com.non_real_ecommerce.product_service.domain.model.Product;
import com.non_real_ecommerce.product_service.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable);
    boolean existsById(Long id);
    void deleteById(Long id);

    Page<Product> findByStockGreaterThan(Long stock, Pageable pageable);
    Page<Product> findByStockGreaterThanAndStatus(Long stock, ProductStatus status, Pageable pageable);
    Page<Product> findByCategoryAndStatus(String category, ProductStatus status, Pageable pageable);
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    Page<Product> findByNameContainingOrDescriptionContaining(String keyword, Pageable pageable);

    long countByStockGreaterThanAndStatus(Long stock, ProductStatus status);
}
