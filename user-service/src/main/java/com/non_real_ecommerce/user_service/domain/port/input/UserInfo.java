package com.non_real_ecommerce.user_service.domain.port.input;

import com.non_real_ecommerce.user_service.domain.model.UserRole;

public record UserInfo(Long userId, String email, UserRole role) {
}
