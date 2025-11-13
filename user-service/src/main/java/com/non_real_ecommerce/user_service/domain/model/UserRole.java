package com.non_real_ecommerce.user_service.domain.model;

public enum UserRole {
    ADMIN, CLIENT;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean canAccessAdminFeatures() {
        return this == ADMIN;
    }
}
