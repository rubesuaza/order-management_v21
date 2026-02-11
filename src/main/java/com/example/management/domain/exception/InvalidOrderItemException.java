package com.example.management.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear o agregar un item de pedido inválido.
 */
public class InvalidOrderItemException extends DomainException {
    public InvalidOrderItemException(String message) {
        super(message);
    }
}
