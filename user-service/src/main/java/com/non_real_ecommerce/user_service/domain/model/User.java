package com.non_real_ecommerce.user_service.domain.model;

import com.non_real_ecommerce.user_service.domain.exception.DomainException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "UserBuilder", toBuilder = true)
public class User {
    private final Long id;
    private final String email;
    private final String name;
    private final String passwordHash;
    private final UserRole role;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(Long id, String email, String name, String passwordHash,
                 UserRole role, UserStatus status, LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    private void validate() {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new DomainException("Invalid email format");
        }
        if (name == null || name.isBlank() || name.length() < 2) {
            throw new DomainException("Name must be at least 2 characters long");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DomainException("Password hash cannot be blank");
        }
    }

    // Métodos de negocio usando Builder para inmutabilidad
    public User deactivate() {
        return this.toBuilder()
                .status(UserStatus.INACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public User activate() {
        return this.toBuilder()
                .status(UserStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public User updateName(String newName) {
        return this.toBuilder()
                .name(newName)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public boolean hasRole(UserRole requiredRole) {
        return this.role == requiredRole;
    }

    public boolean canAccessAdminFeatures() {
        return this.role.canAccessAdminFeatures();
    }

    public boolean canLogin() {
        return this.status.canLogin();
    }


    public static UserBuilder builder() {
        return new CustomUserBuilder();
    }

    private static class CustomUserBuilder extends UserBuilder {
        @Override
        public User build() {

            if (super.email == null || super.email.isBlank()) {
                throw new DomainException("Email cannot be null or blank");
            }

            if (super.role == null) {
                super.role = UserRole.CLIENT;
            }
            if (super.status == null) {
                super.status = UserStatus.ACTIVE;
            }
            if (super.createdAt == null) {
                super.createdAt = LocalDateTime.now();
            }
            if (super.updatedAt == null) {
                super.updatedAt = LocalDateTime.now();
            }

            return super.build();
        }
    }
}
