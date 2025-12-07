package com.non_real_ecommerce.product_service.infrastructure.persistence.repository;


import com.non_real_ecommerce.product_service.domain.model.ProductStatus;
import com.non_real_ecommerce.product_service.infrastructure.persistence.entity.ProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByStockGreaterThan(Long stock, Pageable pageable);
    Page<ProductEntity> findByStockGreaterThanAndStatus(Long stock, ProductStatus status, Pageable pageable);
    Page<ProductEntity> findByCategoryAndStatus(String category, ProductStatus status, Pageable pageable);
    Page<ProductEntity> findByStatus(ProductStatus status, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductEntity> findByNameContainingOrDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);
    long countByStockGreaterThanAndStatus(Long stock, ProductStatus status);
}
