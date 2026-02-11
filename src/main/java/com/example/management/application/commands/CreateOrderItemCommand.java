package com.example.management.application.commands;

/**
 * Comando DTO para crear un item de orden.
 * Representa los datos necesarios para crear un OrderItem sin exponer el modelo de dominio.
 */
public record CreateOrderItemCommand(
    String productId,
    String productName,
    Double unitPrice,
    Integer quantity
) {
    public CreateOrderItemCommand {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo o vacío");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (unitPrice == null || unitPrice < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor o igual a cero");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }
}
