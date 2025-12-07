package com.non_real_ecommerce.product_service.infrastructure.controller;

import com.non_real_ecommerce.product_service.application.dto.ApiErrorResponse;
import com.non_real_ecommerce.product_service.domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return "";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<ApiErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapToValidationError)
                .collect(Collectors.toList());

        ApiErrorResponse errorResponse = ApiErrorResponse.validationError(
                "Validation failed for one or more fields",
                validationErrors,
                getPath(request)
        );

        log.warn("Validation error: {}", validationErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
            DomainException ex, WebRequest request) {

        ApiErrorResponse errorResponse = ApiErrorResponse.businessError(
                ex.getMessage(),
                "DOMAIN_RULE_VIOLATION",
                getPath(request)
        );

        log.warn("Domain rule violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaughtException(
            Exception ex, WebRequest request) {

        log.error("Internal server error: {}", ex.getMessage(), ex);

        ApiErrorResponse errorResponse = ApiErrorResponse.internalError(
                "An unexpected error occurred. Please try again later.",
                getPath(request)
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        ApiErrorResponse errorResponse = ApiErrorResponse.businessError(
                ex.getMessage(),
                "INVALID_INPUT",
                getPath(request)
        );

        log.warn("Invalid input: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ApiErrorResponse.ValidationError mapToValidationError(FieldError fieldError) {
        return ApiErrorResponse.ValidationError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue() != null ?
                        fieldError.getRejectedValue().toString() : null)
                .build();
    }
}
