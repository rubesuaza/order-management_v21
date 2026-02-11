package com.example.management.domain.model;

import java.util.Objects;

/**
 * Value Object que representa una cantidad de dinero.
 * Invariante: El valor debe ser mayor o igual a cero.
 */
public final class Money {
    private final double value;

    public Money(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("El valor del dinero no puede ser negativo");
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Money add(Money other) {
        return new Money(this.value + other.value);
    }

    public Money multiply(int multiplier) {
        return new Money(this.value * multiplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
