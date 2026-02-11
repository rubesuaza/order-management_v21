package com.example.management.infrastructure.adapters.out.persistence.entity;

/**
 * Enum para el estado de la orden en la entidad JPA.
 */
public enum OrderStatusEntity {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
