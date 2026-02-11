package com.example.management.domain.model;

/**
 * Enum que representa los estados posibles de un pedido.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    /**
     * Verifica si el estado permite cancelación.
     * Solo los estados PENDING y CONFIRMED pueden ser cancelados.
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
}
