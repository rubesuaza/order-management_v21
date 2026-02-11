package com.example.management.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO para agregar un item a una orden existente.
 */
public record AddOrderItemRequest(
    @NotBlank(message = "El ID del producto es obligatorio")
    String productId,
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    String productName,
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a cero")
    BigDecimal unitPrice,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    Integer quantity
) {
}
