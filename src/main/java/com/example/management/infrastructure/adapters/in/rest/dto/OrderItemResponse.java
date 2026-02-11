package com.example.management.infrastructure.adapters.in.rest.dto;

import com.example.management.domain.model.OrderItem;

/**
 * DTO para un item de orden en las respuestas REST.
 */
public record OrderItemResponse(
    String productId,
    String productName,
    Double unitPrice,
    Integer quantity,
    Double total
) {
    public static OrderItemResponse fromDomain(OrderItem item) {
        return new OrderItemResponse(
            item.getProductId(),
            item.getProductName(),
            item.getUnitPrice().getValue(),
            item.getQuantity().getValue(),
            item.calculateTotal().getValue()
        );
    }
}
