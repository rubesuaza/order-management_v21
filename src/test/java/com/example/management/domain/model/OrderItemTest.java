package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    @DisplayName("Debe crear OrderItem con todos los campos válidos")
    void shouldCreateOrderItemWithValidFields() {
        String productId = "PROD-001";
        String productName = "Producto Test";
        Money unitPrice = new Money(10.0);
        Quantity quantity = new Quantity(2);
        
        OrderItem item = new OrderItem(productId, productName, unitPrice, quantity);
        
        assertEquals(productId, item.getProductId());
        assertEquals(productName, item.getProductName());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(quantity, item.getQuantity());
    }

    @Test
    @DisplayName("Debe calcular el total del item correctamente")
    void shouldCalculateItemTotalCorrectly() {
        Money unitPrice = new Money(10.0);
        Quantity quantity = new Quantity(3);
        OrderItem item = new OrderItem("PROD-001", "Producto", unitPrice, quantity);
        
        Money total = item.calculateTotal();
        
        assertEquals(30.0, total.getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando productId es nulo")
    void shouldThrowExceptionWhenProductIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem(null, "Producto", new Money(10.0), new Quantity(1));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando productId está vacío")
    void shouldThrowExceptionWhenProductIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem("", "Producto", new Money(10.0), new Quantity(1));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando productName es nulo")
    void shouldThrowExceptionWhenProductNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem("PROD-001", null, new Money(10.0), new Quantity(1));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando unitPrice es nulo")
    void shouldThrowExceptionWhenUnitPriceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem("PROD-001", "Producto", null, new Quantity(1));
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando quantity es nulo")
    void shouldThrowExceptionWhenQuantityIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderItem("PROD-001", "Producto", new Money(10.0), null);
        });
    }
}
