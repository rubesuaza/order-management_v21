package com.example.management.domain.model;

import java.util.Objects;

/**
 * Value Object que representa un item de pedido.
 * Invariantes:
 * - productId no puede ser nulo o vacío
 * - productName no puede ser nulo
 * - unitPrice no puede ser nulo
 * - quantity no puede ser nulo
 */
public final class OrderItem {
    private final String productId;
    private final String productName;
    private final Money unitPrice;
    private final Quantity quantity;

    public OrderItem(String productId, String productName, Money unitPrice, Quantity quantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo o vacío");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("El precio unitario no puede ser nulo");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        
        this.productId = productId.trim();
        this.productName = productName.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Calcula el total del item multiplicando el precio unitario por la cantidad.
     */
    public Money calculateTotal() {
        return unitPrice.multiply(quantity.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId) &&
               Objects.equals(productName, orderItem.productName) &&
               Objects.equals(unitPrice, orderItem.unitPrice) &&
               Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, unitPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
