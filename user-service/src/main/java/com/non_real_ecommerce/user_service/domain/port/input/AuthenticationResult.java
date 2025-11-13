package com.non_real_ecommerce.user_service.domain.port.input;

import com.non_real_ecommerce.user_service.domain.model.User;

public record AuthenticationResult(String token, User user) {}
