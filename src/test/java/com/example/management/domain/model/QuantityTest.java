package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    @DisplayName("Debe crear Quantity con valor positivo")
    void shouldCreateQuantityWithPositiveValue() {
        Quantity quantity = new Quantity(5);
        assertEquals(5, quantity.getValue());
    }

    @Test
    @DisplayName("Debe crear Quantity con valor uno")
    void shouldCreateQuantityWithOne() {
        Quantity quantity = new Quantity(1);
        assertEquals(1, quantity.getValue());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el valor es cero")
    void shouldThrowExceptionWhenValueIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quantity(0);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el valor es negativo")
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Quantity(-1);
        });
    }

    @Test
    @DisplayName("Debe sumar dos cantidades correctamente")
    void shouldAddTwoQuantities() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(3);
        Quantity result = q1.add(q2);
        
        assertEquals(8, result.getValue());
    }

    @Test
    @DisplayName("Debe comparar dos Quantity iguales")
    void shouldCompareEqualQuantities() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(5);
        
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }
}
