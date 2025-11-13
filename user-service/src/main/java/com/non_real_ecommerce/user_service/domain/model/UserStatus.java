package com.non_real_ecommerce.user_service.domain.model;

public enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED;

    public boolean canLogin() {
        return this == ACTIVE;
    }
}
