package com.non_real_ecommerce.product_service.infrastructure.persistence.repository;

import com.non_real_ecommerce.product_service.domain.exception.ProductNotFounfException;
import com.non_real_ecommerce.product_service.domain.model.Product;
import com.non_real_ecommerce.product_service.domain.model.ProductStatus;
import com.non_real_ecommerce.product_service.domain.port.output.ProductRepository;
import com.non_real_ecommerce.product_service.infrastructure.persistence.mapper.ProductMapper;
import com.non_real_ecommerce.product_service.infrastructure.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        final ProductEntity entityToSave;
        if (product.getId() == null) {
            entityToSave = productMapper.toEntity(product);
        } else {
            ProductEntity existingEntity = jpaProductRepository.findById(product.getId())
                    .orElseThrow(() -> new ProductNotFounfException(product.getId()));

            productMapper.updateEntityFromDomain(product, existingEntity);
            entityToSave = existingEntity;
        }

        ProductEntity savedEntity = jpaProductRepository.save(entityToSave);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaProductRepository.findAll(pageable).map(productMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaProductRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaProductRepository.deleteById(id);
    }

    @Override
    public Page<Product> findByStockGreaterThan(Long stock, Pageable pageable) {
        return jpaProductRepository.findByStockGreaterThan(stock, pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findByStockGreaterThanAndStatus(Long stock, ProductStatus status, Pageable pageable) {
        return jpaProductRepository.findByStockGreaterThanAndStatus(stock, status, pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findByCategoryAndStatus(String category, ProductStatus status, Pageable pageable) {
        return jpaProductRepository.findByCategoryAndStatus(category, status, pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findByStatus(ProductStatus status, Pageable pageable) {
        return jpaProductRepository.findByStatus(status, pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findByNameContainingOrDescriptionContaining(String keyword, Pageable pageable) {
        return jpaProductRepository.findByNameContainingOrDescriptionContaining(keyword, pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public long countByStockGreaterThanAndStatus(Long stock, ProductStatus status) {
        return jpaProductRepository.countByStockGreaterThanAndStatus(stock, status);
    }
}
