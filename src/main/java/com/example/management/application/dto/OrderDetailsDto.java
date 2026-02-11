package com.example.management.application.dto;

import com.example.management.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de aplicación que representa los detalles de una orden.
 * Este DTO actúa como capa de abstracción entre el dominio y la infraestructura,
 * evitando que las capas externas dependan directamente de las entidades de dominio.
 */
public record OrderDetailsDto(
    String id,
    String customerId,
    OrderStatus status,
    List<OrderItemDto> items,
    java.math.BigDecimal total,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
