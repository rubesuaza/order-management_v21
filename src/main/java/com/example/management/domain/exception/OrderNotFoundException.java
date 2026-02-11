package com.example.management.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una orden.
 */
public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
