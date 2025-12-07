package com.non_real_ecommerce.product_service.domain.model;

import com.non_real_ecommerce.product_service.domain.exception.DomainException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "ProductBuilder", toBuilder = true)
public class Product {
    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Long stock;
    private final Long reservedStock;
    private final String category;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Product(Long id, String name, String description, BigDecimal price,
                    Long stock, Long reservedStock, String category, ProductStatus status,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.reservedStock = reservedStock;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new DomainException("Product name cannot be blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Product price must be positive");
        }
        if (stock == null || stock < 0) {
            throw new DomainException("Product stock cannot be negative");
        }
    }

    public boolean isAvailable() {
        return this.stock > 0 && this.status == ProductStatus.ACTIVE;
    }

    public Product reduceStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }
        if (this.stock < quantity) {
            throw new DomainException("Insufficient stock");
        }

        return this.toBuilder()
                .stock(this.stock - quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product reserveStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }
        if (this.stock < quantity) {
            throw new DomainException("Insufficient stock");
        }

        return this.toBuilder()
                .stock(this.stock - quantity)
                .reservedStock(this.reservedStock + quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product revertStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }

        return this.toBuilder()
                .stock(this.stock + quantity)
                .reservedStock(this.reservedStock - quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product releaseStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }

        return this.toBuilder()
                .reservedStock(this.reservedStock - quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product increaseStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }

        return this.toBuilder()
                .stock(this.stock + quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product deactivate() {
        return this.toBuilder()
                .status(ProductStatus.INACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product activate() {
        return this.toBuilder()
                .status(ProductStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ProductBuilder builder() {
        return new CustomProductBuilder();
    }

    public Product updateProduct(String name, String description, BigDecimal price) {
        return this.toBuilder()
                .name(name)
                .description(description)
                .price(price)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Product addStock(Long quantity) {
        if (quantity <= 0) {
            throw new DomainException("Quantity must be positive");
        }

        return this.toBuilder()
                .stock(this.stock + quantity)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private static class CustomProductBuilder extends ProductBuilder {
        @Override
        public Product build() {
            if (super.name == null || super.name.isBlank()) {
                throw new DomainException("Product name is required");
            }
            if (super.price == null) {
                throw new DomainException("Product price is required");
            }
            if (super.stock == null) {
                super.stock = 0L;
            }
            if (super.status == null) {
                super.status = ProductStatus.ACTIVE;
            }
            if (super.createdAt == null) {
                super.createdAt = LocalDateTime.now();
            }
            if (super.updatedAt == null) {
                super.updatedAt = LocalDateTime.now();
            }
            if(super.reservedStock == null){
                super.reservedStock = 0L;
            }

            return super.build();
        }
    }
}
