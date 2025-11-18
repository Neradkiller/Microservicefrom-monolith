package com.non_real_ecommerce.product_service.domain.port.output;

import com.non_real_ecommerce.product_service.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    List<Product> findByStockGreaterThan(Long stock);
}
