package com.example.management.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio que representa un pedido.
 * Invariantes:
 * - customerId no puede ser nulo o vacío
 * - items no puede ser nulo o vacío
 * - El total del pedido debe ser la suma de los totales de los items
 * - Solo se pueden agregar items cuando el estado es PENDING
 * - Las transiciones de estado deben seguir el flujo: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED
 * - Solo se puede cancelar cuando el estado es PENDING o CONFIRMED
 */
public class Order {
    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String customerId, List<OrderItem> items) {
        this(UUID.randomUUID().toString(), customerId, items, OrderStatus.PENDING, 
             LocalDateTime.now(), LocalDateTime.now());
    }

    /**
     * Constructor para reconstruir una orden desde la persistencia.
     * Solo debe ser usado por adaptadores de infraestructura.
     */
    public Order(String id, String customerId, List<OrderItem> items, 
                 OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo o vacío");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }
        if (status == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("La fecha de creación no puede ser nula");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("La fecha de actualización no puede ser nula");
        }
        
        this.id = id;
        this.customerId = customerId.trim();
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Calcula el total del pedido sumando los totales de todos los items.
     */
    public Money calculateTotal() {
        return items.stream()
            .map(OrderItem::calculateTotal)
            .reduce(new Money(0.0), Money::add);
    }

    /**
     * Confirma el pedido.
     * Solo se puede confirmar si el estado es PENDING.
     */
    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se puede confirmar un pedido pendiente");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Envía el pedido.
     * Solo se puede enviar si el estado es CONFIRMED.
     */
    public void ship() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Solo se puede enviar un pedido confirmado");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca el pedido como entregado.
     * Solo se puede entregar si el estado es SHIPPED.
     */
    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Solo se puede entregar un pedido enviado");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cancela el pedido.
     * Solo se puede cancelar si el estado es PENDING o CONFIRMED.
     */
    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new IllegalStateException("No se puede cancelar un pedido en estado " + status);
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Agrega un item al pedido.
     * Solo se puede agregar items cuando el estado es PENDING.
     */
    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden agregar items a un pedido pendiente");
        }
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        this.items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", status=" + status +
                ", itemsCount=" + items.size() +
                ", total=" + calculateTotal().getValue().doubleValue() +
                ", createdAt=" + createdAt +
                '}';
    }
}
