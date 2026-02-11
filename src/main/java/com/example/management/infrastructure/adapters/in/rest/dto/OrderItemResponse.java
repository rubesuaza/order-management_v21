package com.example.management.infrastructure.adapters.in.rest.dto;

import com.example.management.application.dto.OrderItemDto;

import java.math.BigDecimal;

/**
 * DTO para un item de orden en las respuestas REST.
 */
public record OrderItemResponse(
    String productId,
    String productName,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal total
) {
    public static OrderItemResponse fromApplicationDto(OrderItemDto itemDto) {
        return new OrderItemResponse(
            itemDto.productId(),
            itemDto.productName(),
            itemDto.unitPrice(),
            itemDto.quantity(),
            itemDto.total()
        );
    }
}
