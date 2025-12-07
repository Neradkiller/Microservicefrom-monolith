package com.non_real_ecommerce.product_service.domain.exception;

public class ConcurrencyException extends DomainException{
    public ConcurrencyException(String message){
        super(message);
    }
}