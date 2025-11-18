package com.non_real_ecommerce.product_service.domain.exception;

public class ProductNotFounfException extends DomainException{
    public ProductNotFounfException(Long id) {
        super("Product not found with id: " + id);
    }
}
