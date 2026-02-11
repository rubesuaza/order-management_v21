package com.example.management.application.commands;

/**
 * Comando DTO para agregar un item a una orden existente.
 * Representa los datos necesarios para agregar un OrderItem sin exponer el modelo de dominio.
 */
public record AddOrderItemCommand(
    String productId,
    String productName,
    java.math.BigDecimal unitPrice,
    Integer quantity
) {
    public AddOrderItemCommand {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo o vacío");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (unitPrice == null || unitPrice.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor o igual a cero");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }
}
