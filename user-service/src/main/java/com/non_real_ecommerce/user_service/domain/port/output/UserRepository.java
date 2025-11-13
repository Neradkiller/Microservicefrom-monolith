package com.non_real_ecommerce.user_service.domain.port.output;

import com.non_real_ecommerce.user_service.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsByEmail(String email);
    void deleteById(Long id);
}
