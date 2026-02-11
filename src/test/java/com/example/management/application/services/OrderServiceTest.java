package com.example.management.application.services;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para {@link OrderService}.
 * Se mockea el puerto de salida {@link OrderRepository} para verificar únicamente la lógica de aplicación.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private List<OrderItem> createSampleItems() {
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        OrderItem item2 = new OrderItem("PROD-002", "Producto 2", new Money(15.0), new Quantity(1));
        return Arrays.asList(item1, item2);
    }

    @Test
    @DisplayName("Debe crear una orden y guardarla en el repositorio")
    void shouldCreateOrderAndSaveIt() {
        // Arrange
        String customerId = "CUSTOMER-001";
        List<OrderItem> items = createSampleItems();

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.createOrder(customerId, items);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(items.size(), result.getItems().size());
        assertEquals(OrderStatus.PENDING, result.getStatus());

        verify(orderRepository).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe retornar la orden cuando existe en el repositorio")
    void shouldReturnOrderWhenItExists() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        String orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Optional<Order> result = orderService.getOrder(orderId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(orderId);

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando la orden no existe")
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        // Arrange
        String orderId = "NON-EXISTENT-ID";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = orderService.getOrder(orderId);

        // Assert
        assertThat(result).isEmpty();

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe confirmar una orden existente y guardarla")
    void shouldConfirmExistingOrder() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        String orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.confirmOrder(orderId);

        // Assert
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(eq(order));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción al confirmar una orden inexistente")
    void shouldThrowWhenConfirmingNonExistingOrder() {
        // Arrange
        String orderId = "NON-EXISTENT-ID";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.confirmOrder(orderId));

        assertThat(ex.getMessage()).contains("Orden no encontrada");

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe enviar una orden confirmada y guardarla")
    void shouldShipConfirmedOrder() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        order.confirm(); // debe estar confirmada para poderse enviar
        String orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.shipOrder(orderId);

        // Assert
        assertEquals(OrderStatus.SHIPPED, result.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(eq(order));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe marcar como entregada una orden enviada y guardarla")
    void shouldDeliverShippedOrder() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        order.confirm();
        order.ship(); // debe estar enviada para poderse entregar
        String orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.deliverOrder(orderId);

        // Assert
        assertEquals(OrderStatus.DELIVERED, result.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(eq(order));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe cancelar una orden pendiente y guardarla")
    void shouldCancelPendingOrder() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        String orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.cancelOrder(orderId);

        // Assert
        assertEquals(OrderStatus.CANCELLED, result.getStatus());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(eq(order));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("Debe agregar un item a una orden pendiente y guardarla")
    void shouldAddItemToPendingOrder() {
        // Arrange
        Order order = new Order("CUSTOMER-001", createSampleItems());
        String orderId = order.getId();

        OrderItem newItem = new OrderItem("PROD-003", "Producto 3", new Money(20.0), new Quantity(1));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.addItemToOrder(orderId, newItem);

        // Assert
        assertEquals(3, result.getItems().size());
        assertThat(result.getItems()).contains(newItem);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(eq(order));
        verifyNoMoreInteractions(orderRepository);
    }
}

