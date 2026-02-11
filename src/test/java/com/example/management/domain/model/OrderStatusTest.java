package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    @DisplayName("Debe crear OrderStatus PENDING")
    void shouldCreatePendingStatus() {
        OrderStatus status = OrderStatus.PENDING;
        assertEquals("PENDING", status.name());
    }

    @Test
    @DisplayName("Debe crear OrderStatus CONFIRMED")
    void shouldCreateConfirmedStatus() {
        OrderStatus status = OrderStatus.CONFIRMED;
        assertEquals("CONFIRMED", status.name());
    }

    @Test
    @DisplayName("Debe crear OrderStatus SHIPPED")
    void shouldCreateShippedStatus() {
        OrderStatus status = OrderStatus.SHIPPED;
        assertEquals("SHIPPED", status.name());
    }

    @Test
    @DisplayName("Debe crear OrderStatus DELIVERED")
    void shouldCreateDeliveredStatus() {
        OrderStatus status = OrderStatus.DELIVERED;
        assertEquals("DELIVERED", status.name());
    }

    @Test
    @DisplayName("Debe crear OrderStatus CANCELLED")
    void shouldCreateCancelledStatus() {
        OrderStatus status = OrderStatus.CANCELLED;
        assertEquals("CANCELLED", status.name());
    }

    @Test
    @DisplayName("Debe verificar si el estado permite cancelación")
    void shouldCheckIfStatusAllowsCancellation() {
        assertTrue(OrderStatus.PENDING.canBeCancelled());
        assertTrue(OrderStatus.CONFIRMED.canBeCancelled());
        assertFalse(OrderStatus.SHIPPED.canBeCancelled());
        assertFalse(OrderStatus.DELIVERED.canBeCancelled());
        assertFalse(OrderStatus.CANCELLED.canBeCancelled());
    }
}
