package com.example.management.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO para la creación de una orden.
 */
public record CreateOrderRequest(
    @NotBlank(message = "El ID del cliente es obligatorio")
    String customerId,
    
    @NotEmpty(message = "La orden debe tener al menos un item")
    List<OrderItemRequest> items
) {
}
