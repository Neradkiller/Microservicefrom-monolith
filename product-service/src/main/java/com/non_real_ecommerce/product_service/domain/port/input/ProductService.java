package com.non_real_ecommerce.product_service.domain.port.input;

import com.non_real_ecommerce.product_service.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(String name, String description, BigDecimal price, Long stock, String category);
    Optional<Product> getProductById(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    Product updateProduct(Long id, String name, String description, BigDecimal price, String category);
    void deleteProduct(Long id);

    Product updateStock(Long id, Long newStock);
    Product reduceStock(Long id, Long quantity);
    Product increaseStock(Long id, Long quantity);

    Product reserveStock(Long id, Long quantity);
    Product revertStock(Long id, Long quantity);
    Product releaseStock(Long id, Long quantity);

    Page<Product> getAvailableProducts(Pageable pageable);
    Page<Product> getProductsWithStockGreaterThan(Long minStock, Pageable pageable);
    Page<Product> getProductsByCategory(String category, Pageable pageable);
    Page<Product> searchProducts(String keyword, Pageable pageable);

    void deactivateProduct(Long id);
    void activateProduct(Long id);

    record ProductsPage(List<Product> products, int page, int size, long totalElements, int totalPages){}
}
