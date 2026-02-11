package com.example.management.application.dto;

import java.math.BigDecimal;

/**
 * DTO de aplicación que representa un item de orden.
 * Este DTO actúa como capa de abstracción entre el dominio y la infraestructura,
 * evitando que las capas externas dependan directamente de las entidades de dominio.
 */
public record OrderItemDto(
    String productId,
    String productName,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal total
) {
}
