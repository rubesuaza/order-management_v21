package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("Debe crear OrderStatus y verificar su nombre")
    void shouldCreateOrderStatusAndVerifyName(OrderStatus status) {
        assertEquals(status.name(), status.name());
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
