package com.example.management.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class OrderTest {

    @Test
    @DisplayName("Debe crear Order con campos válidos")
    void shouldCreateOrderWithValidFields() {
        String customerId = "CUST-001";
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto 1", new Money(java.math.BigDecimal.valueOf(10.0)), new Quantity(2)));
        
        Order order = new Order(customerId, items);
        
        assertNotNull(order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(1, order.getItems().size());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    @DisplayName("Debe calcular el total del pedido correctamente")
    void shouldCalculateOrderTotalCorrectly() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto 1", new Money(java.math.BigDecimal.valueOf(10.0)), new Quantity(2)));
        items.add(new OrderItem("PROD-002", "Producto 2", new Money(java.math.BigDecimal.valueOf(15.0), new Quantity(3)));
        
        Order order = new Order("CUST-001", items);
        Money total = order.calculateTotal();
        
        assertEquals(65.0, total.getValue().doubleValue(), 0.001); // (10*2) + (15*3) = 20 + 45 = 65
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando customerId es nulo")
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(null, new ArrayList<>());
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando customerId está vacío")
    void shouldThrowExceptionWhenCustomerIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order("", new ArrayList<>());
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando items es nulo")
    void shouldThrowExceptionWhenItemsIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order("CUST-001", null);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando items está vacío")
    void shouldThrowExceptionWhenItemsIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order("CUST-001", new ArrayList<>());
        });
    }

    @Test
    @DisplayName("Debe confirmar un pedido pendiente")
    void shouldConfirmPendingOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        
        order.confirm();
        
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar excepción al confirmar pedido no pendiente")
    void shouldThrowExceptionWhenConfirmingNonPendingOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        
        assertThrows(IllegalStateException.class, () -> {
            order.confirm();
        });
    }

    @Test
    @DisplayName("Debe cancelar un pedido pendiente")
    void shouldCancelPendingOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        
        order.cancel();
        
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Debe cancelar un pedido confirmado")
    void shouldCancelConfirmedOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        
        order.cancel();
        
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar excepción al cancelar pedido enviado")
    void shouldThrowExceptionWhenCancellingShippedOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        order.ship();
        
        assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
    }

    @Test
    @DisplayName("Debe enviar un pedido confirmado")
    void shouldShipConfirmedOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        
        order.ship();
        
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    @DisplayName("Debe marcar como entregado un pedido enviado")
    void shouldDeliverShippedOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        order.ship();
        
        order.deliver();
        
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("Debe agregar item a un pedido pendiente")
    void shouldAddItemToPendingOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto 1", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        
        OrderItem newItem = new OrderItem("PROD-002", "Producto 2", new Money(java.math.BigDecimal.valueOf(20.0)), new Quantity(2));
        order.addItem(newItem);
        
        assertEquals(2, order.getItems().size());
        assertEquals(50.0, order.calculateTotal().getValue().doubleValue(), 0.001);
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar item a pedido no pendiente")
    void shouldThrowExceptionWhenAddingItemToNonPendingOrder() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("PROD-001", "Producto", new Money(java.math.BigDecimal.valueOf(10.0), new Quantity(1)));
        Order order = new Order("CUST-001", items);
        order.confirm();
        
        OrderItem newItem = new OrderItem("PROD-002", "Producto 2", new Money(java.math.BigDecimal.valueOf(20.0)), new Quantity(1));
        assertThrows(IllegalStateException.class, () -> {
            order.addItem(newItem);
        });
    }
}
