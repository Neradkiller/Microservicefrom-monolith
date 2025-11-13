package com.non_real_ecommerce.user_service.domain.port.output;

public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
