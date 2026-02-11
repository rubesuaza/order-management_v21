package com.example.management.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida
 * sobre un pedido debido a su estado actual.
 */
public class InvalidOrderStateException extends DomainException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
