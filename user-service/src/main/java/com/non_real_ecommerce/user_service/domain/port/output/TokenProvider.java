package com.non_real_ecommerce.user_service.domain.port.output;

import com.non_real_ecommerce.user_service.domain.model.User;
import com.non_real_ecommerce.user_service.domain.port.input.UserInfo;

public interface TokenProvider {
    String generateToken(User user);
    boolean validateToken(String token);
    UserInfo extractUserInfo(String token);
}
