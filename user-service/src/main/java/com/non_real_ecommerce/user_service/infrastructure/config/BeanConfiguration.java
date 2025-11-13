package com.non_real_ecommerce.user_service.infrastructure.config;

import com.non_real_ecommerce.user_service.application.service.AuthServiceImpl;
import com.non_real_ecommerce.user_service.application.service.UserServiceImpl;
import com.non_real_ecommerce.user_service.domain.port.input.AuthService;
import com.non_real_ecommerce.user_service.domain.port.input.UserService;
import com.non_real_ecommerce.user_service.domain.port.output.PasswordEncoder;
import com.non_real_ecommerce.user_service.domain.port.output.TokenProvider;
import com.non_real_ecommerce.user_service.domain.port.output.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AuthService authService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   TokenProvider tokenProvider) {
        return new AuthServiceImpl(userRepository, passwordEncoder, tokenProvider);
    }
}
