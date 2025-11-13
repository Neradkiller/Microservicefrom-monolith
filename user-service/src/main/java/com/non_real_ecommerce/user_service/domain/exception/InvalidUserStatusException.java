package com.non_real_ecommerce.user_service.domain.exception;

public class InvalidUserStatusException extends DomainException{
    public InvalidUserStatusException(String message) {
        super(message);
    }
}
