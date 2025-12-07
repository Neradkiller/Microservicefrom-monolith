package com.non_real_ecommerce.product_service.infrastructure.config;


import com.non_real_ecommerce.product_service.application.service.ProductServiceImpl;
import com.non_real_ecommerce.product_service.domain.port.input.ProductService;
import com.non_real_ecommerce.product_service.domain.port.output.ProductRepository;
import com.non_real_ecommerce.product_service.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public ProductService productService(ProductRepository productRepository, ProductMapper productMapper) {
        return new ProductServiceImpl(productRepository, productMapper);
    }
}
