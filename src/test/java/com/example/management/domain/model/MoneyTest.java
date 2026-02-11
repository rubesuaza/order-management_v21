package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    @DisplayName("Debe crear Money con valor positivo")
    void shouldCreateMoneyWithPositiveValue() {
        Money money = new Money(java.math.BigDecimal.valueOf(100.50));
        assertEquals(100.50, money.getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe crear Money con valor cero")
    void shouldCreateMoneyWithZeroValue() {
        Money money = new Money(java.math.BigDecimal.valueOf(0.0));
        assertEquals(0.0, money.getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el valor es negativo")
    void shouldThrowExceptionWhenValueIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Money(java.math.BigDecimal.valueOf(-10.0));
        });
    }

    @Test
    @DisplayName("Debe sumar dos valores Money correctamente")
    void shouldAddTwoMoneyValues() {
        Money money1 = new Money(java.math.BigDecimal.valueOf(100.0));
        Money money2 = new Money(java.math.BigDecimal.valueOf(50.5));
        Money result = money1.add(money2);
        
        assertEquals(150.5, result.getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe multiplicar Money por cantidad correctamente")
    void shouldMultiplyMoneyByQuantity() {
        Money money = new Money(java.math.BigDecimal.valueOf(10.0));
        Money result = money.multiply(3);
        
        assertEquals(30.0, result.getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe comparar dos Money iguales")
    void shouldCompareEqualMoney() {
        Money money1 = new Money(java.math.BigDecimal.valueOf(100.0));
        Money money2 = new Money(java.math.BigDecimal.valueOf(100.0));
        
        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    @DisplayName("Debe comparar dos Money diferentes")
    void shouldCompareDifferentMoney() {
        Money money1 = new Money(java.math.BigDecimal.valueOf(100.0));
        Money money2 = new Money(java.math.BigDecimal.valueOf(200.0));
        
        assertNotEquals(money1, money2);
    }
}
