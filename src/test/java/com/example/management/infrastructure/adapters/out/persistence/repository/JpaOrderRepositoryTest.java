package com.example.management.infrastructure.adapters.out.persistence.repository;

import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.Quantity;
import com.example.management.infrastructure.adapters.out.persistence.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de contrato para JpaOrderRepository.
 * Verifica que el adaptador de persistencia cumple con el contrato definido por OrderRepository.
 */
@DataJpaTest
@Import({JpaOrderRepository.class, OrderMapper.class})
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
class JpaOrderRepositoryTest {

    @Autowired
    private JpaOrderRepository repository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        OrderItem item2 = new OrderItem("PROD-002", "Producto 2", new Money(15.0), new Quantity(1));
        List<OrderItem> items = Arrays.asList(item1, item2);
        
        testOrder = new Order("CUSTOMER-001", items);
    }

    @Test
    void shouldSaveOrder() {
        // When
        Order savedOrder = repository.save(testOrder);

        // Then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getCustomerId()).isEqualTo("CUSTOMER-001");
        assertThat(savedOrder.getItems()).hasSize(2);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void shouldFindOrderById() {
        // Given
        Order savedOrder = repository.save(testOrder);
        String orderId = savedOrder.getId();

        // When
        Optional<Order> foundOrder = repository.findById(orderId);

        // Then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(orderId);
        assertThat(foundOrder.get().getCustomerId()).isEqualTo("CUSTOMER-001");
        assertThat(foundOrder.get().getItems()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        // When
        Optional<Order> foundOrder = repository.findById("NON-EXISTENT-ID");

        // Then
        assertThat(foundOrder).isEmpty();
    }

    @Test
    void shouldCheckIfOrderExists() {
        // Given
        Order savedOrder = repository.save(testOrder);
        String orderId = savedOrder.getId();

        // When & Then
        assertThat(repository.existsById(orderId)).isTrue();
        assertThat(repository.existsById("NON-EXISTENT-ID")).isFalse();
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Given
        Order savedOrder = repository.save(testOrder);
        savedOrder.confirm();
        savedOrder.ship();

        // When
        Order updatedOrder = repository.save(savedOrder);

        // Then
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    void shouldPersistOrderItems() {
        // Given
        Order savedOrder = repository.save(testOrder);

        // When
        Optional<Order> foundOrder = repository.findById(savedOrder.getId());

        // Then
        assertThat(foundOrder).isPresent();
        Order order = foundOrder.get();
        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getItems().get(0).getProductId()).isEqualTo("PROD-001");
        assertThat(order.getItems().get(1).getProductId()).isEqualTo("PROD-002");
        assertThat(order.calculateTotal().getValue().doubleValue()).isEqualTo(35.0); // 10*2 + 15*1
    }
}
