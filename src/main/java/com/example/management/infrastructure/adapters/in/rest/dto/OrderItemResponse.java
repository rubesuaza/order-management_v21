package com.example.management.infrastructure.adapters.in.rest.dto;

import com.example.management.application.dto.OrderItemDto;

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
    public static OrderItemResponse fromApplicationDto(OrderItemDto itemDto) {
        return new OrderItemResponse(
            itemDto.productId(),
            itemDto.productName(),
            itemDto.unitPrice().doubleValue(),
            itemDto.quantity(),
            itemDto.total().doubleValue()
        );
    }
}
