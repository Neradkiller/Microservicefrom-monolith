package com.non_real_ecommerce.user_service.domain.exception;

public class InvalidCredentialsException extends DomainException{
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
