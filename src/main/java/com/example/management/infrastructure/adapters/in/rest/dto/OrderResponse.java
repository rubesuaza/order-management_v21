package com.example.management.infrastructure.adapters.in.rest.dto;

import com.example.management.application.dto.OrderDetailsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden.
 */
public record OrderResponse(
    String id,
    String customerId,
    String status,
    List<OrderItemResponse> items,
    Double total,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static OrderResponse fromApplicationDto(OrderDetailsDto orderDto) {
        List<OrderItemResponse> itemResponses = orderDto.items().stream()
            .map(OrderItemResponse::fromApplicationDto)
            .toList();
        
        return new OrderResponse(
            orderDto.id(),
            orderDto.customerId(),
            orderDto.status().name(),
            itemResponses,
            orderDto.total().doubleValue(),
            orderDto.createdAt(),
            orderDto.updatedAt()
        );
    }
}
