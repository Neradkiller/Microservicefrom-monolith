package com.non_real_ecommerce.product_service.application.service;

import com.non_real_ecommerce.product_service.domain.exception.ConcurrencyException;
import com.non_real_ecommerce.product_service.domain.exception.ProductNotFounfException;
import com.non_real_ecommerce.product_service.domain.model.Product;
import com.non_real_ecommerce.product_service.domain.model.ProductStatus;
import com.non_real_ecommerce.product_service.domain.port.input.ProductService;
import com.non_real_ecommerce.product_service.domain.port.output.ProductRepository;
import com.non_real_ecommerce.product_service.infrastructure.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Product createProduct(String name, String description, BigDecimal price, Long stock, String category) {
        log.info("Creating new product with name: {}", name);
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock != null ? stock : 0L)
                .category(category)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product updateProduct(Long id, String name, String description, BigDecimal price, String category) {
        log.info("Attempting to update product with ID: {}", id);
        try{
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));
            Product updatedProduct = product.updateProduct(name, description,price);
            return productRepository.save(updatedProduct);
        }
        catch (ObjectOptimisticLockingFailureException e){
            log.warn("Concurrency conflict for product ID: {}. Update failed.", id);
            throw new ConcurrencyException("Product update failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFounfException(id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public Product updateStock(Long id, Long newStock) {
        log.info("Attempting to update stock for product ID: {} to {}", id, newStock);
        try {
            if (newStock < 0) {
                throw new IllegalArgumentException("Stock cannot be negative");
            }
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));

            Product updatedProduct = product.addStock(newStock);

            return productRepository.save(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Stock update failed.", id);
            throw new ConcurrencyException("Stock update failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Product reduceStock(Long id, Long quantity) {
        log.info("Attempting to update stock for product ID: {} to {}", id, quantity);
        try {
            if (quantity < 0) {
                throw new IllegalArgumentException("Stock cannot be negative");
            }
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));

            Product updatedProduct = product.reduceStock(quantity);

            return productRepository.save(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Stock update failed.", id);
            throw new ConcurrencyException("Stock update failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Product increaseStock(Long id, Long quantity) {
        log.info("Attempting to update stock for product ID: {} to {}", id, quantity);
        try {
            if (quantity < 0) {
                throw new IllegalArgumentException("Stock cannot be negative");
            }
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));

            Product updatedProduct = product.addStock(quantity);

            return productRepository.save(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Stock update failed.", id);
            throw new ConcurrencyException("Stock update failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Product reserveStock(Long id, Long quantity) {
        log.info("Attempting to reserve stock for product ID: {} by {}", id, quantity);
        try {
            Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFounfException(id));

            // La lógica de negocio vive en el dominio
            Product updatedProduct = product.reserveStock(quantity);

            // La capa de repositorio se encarga de la actualización segura
            return productRepository.save(updatedProduct);

        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Reservation failed.", id);
            // Lanza una excepción de negocio que puede ser manejada por el orquestador de la saga.
            throw new ConcurrencyException("Reservation failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Product revertStock(Long id, Long quantity) {
        log.info("Attempting to revert stock for product ID: {} by {}", id, quantity);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));

            // La lógica de negocio vive en el dominio
            Product updatedProduct = product.revertStock(quantity);

            // La capa de repositorio se encarga de la actualización segura
            return productRepository.save(updatedProduct);

        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Revert failed.", id);
            // Lanza una excepción de negocio que puede ser manejada por el orquestador de la saga.
            throw new ConcurrencyException("Revert failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Product releaseStock(Long id, Long quantity) {
        log.info("Attempting to release stock for product ID: {} by {}", id, quantity);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));

            // La lógica de negocio vive en el dominio
            Product updatedProduct = product.releaseStock(quantity);

            // La capa de repositorio se encarga de la actualización segura
            return productRepository.save(updatedProduct);

        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. release failed.", id);
            // Lanza una excepción de negocio que puede ser manejada por el orquestador de la saga.
            throw new ConcurrencyException("Reservation failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public Page<Product> getAvailableProducts(Pageable pageable) {
        return productRepository.findByStockGreaterThan(0L, pageable);
    }

    @Override
    public Page<Product> getProductsWithStockGreaterThan(Long minStock, Pageable pageable) {
        return productRepository.findByStockGreaterThan(minStock, pageable);
    }

    @Override
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategoryAndStatus(category, ProductStatus.ACTIVE, pageable);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingOrDescriptionContaining(keyword, pageable);
    }

    @Override
    public void deactivateProduct(Long id) {
        log.info("Attempting to deactivate product with ID: {}", id);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));
            Product deactivatedProduct = product.deactivate();
            productRepository.save(deactivatedProduct);
            log.info("Product deactivated successfully with ID: {}", id);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Deactivation failed.", id);
            throw new ConcurrencyException("Product deactivation failed due to a concurrent update. Please retry.");
        }
    }

    @Override
    public void activateProduct(Long id) {
        log.info("Attempting to activate product with ID: {}", id);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFounfException(id));
            Product deactivatedProduct = product.activate();
            productRepository.save(deactivatedProduct);
            log.info("Product activated successfully with ID: {}", id);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Concurrency conflict for product ID: {}. Activation failed.", id);
            throw new ConcurrencyException("Product activation failed due to a concurrent update. Please retry.");
        }
    }
}
