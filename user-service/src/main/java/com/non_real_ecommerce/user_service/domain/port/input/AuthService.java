package com.non_real_ecommerce.user_service.domain.port.input;

import com.non_real_ecommerce.user_service.domain.model.User;
import com.non_real_ecommerce.user_service.domain.model.UserRole;

public interface AuthService {
    User register(String email, String name, String password, UserRole role);
    AuthenticationResult login(String email, String password);
    boolean validateToken(String token);
    UserInfo extractUserInfo(String token);
}
