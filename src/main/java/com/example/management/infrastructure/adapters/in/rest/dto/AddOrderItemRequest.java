package com.example.management.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para agregar un item a una orden existente.
 */
public record AddOrderItemRequest(
    @NotBlank(message = "El ID del producto es obligatorio")
    String productId,
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    String productName,
    
    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio unitario debe ser mayor o igual a cero")
    Double unitPrice,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    Integer quantity
) {
}
