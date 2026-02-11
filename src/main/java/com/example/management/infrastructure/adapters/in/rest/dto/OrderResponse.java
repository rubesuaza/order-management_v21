package com.example.management.infrastructure.adapters.in.rest.dto;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden.
 */
public record OrderResponse(
    String id,
    String customerId,
    OrderStatus status,
    List<OrderItemResponse> items,
    Double total,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static OrderResponse fromDomain(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
            .map(OrderItemResponse::fromDomain)
            .toList();
        
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getStatus(),
            itemResponses,
            order.calculateTotal().getValue().doubleValue(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
