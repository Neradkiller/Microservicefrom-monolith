package com.non_real_ecommerce.product_service.domain.model;

public enum ProductStatus {
    ACTIVE, INACTIVE, OUT_OF_STOCK;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
