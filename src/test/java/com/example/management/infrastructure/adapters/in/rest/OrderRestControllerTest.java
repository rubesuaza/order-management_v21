package com.example.management.infrastructure.adapters.in.rest;

import com.example.management.application.ports.in.*;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.OrderStatus;
import com.example.management.domain.model.Quantity;
import com.example.management.infrastructure.adapters.in.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de contrato para OrderRestController.
 * Verifica que el adaptador REST expone correctamente los endpoints y maneja las respuestas.
 */
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @MockBean
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @MockBean
    private AddOrderItemUseCase addOrderItemUseCase;

    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        
        CreateOrderRequest request = new CreateOrderRequest(
            "CUSTOMER-001",
            Arrays.asList(new OrderItemRequest("PROD-001", "Producto 1", 10.0, 2))
        );

        when(createOrderUseCase.createOrder(any(String.class), any(List.class)))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.customerId").value("CUSTOMER-001"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].productId").value("PROD-001"));
    }

    @Test
    void shouldGetOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        String orderId = order.getId();

        when(getOrderUseCase.getOrder(orderId))
            .thenReturn(Optional.of(order));

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(orderId))
            .andExpect(jsonPath("$.customerId").value("CUSTOMER-001"));
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        // Given
        when(getOrderUseCase.getOrder("NON-EXISTENT"))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", "NON-EXISTENT"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldConfirmOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        order.confirm();
        String orderId = order.getId();

        when(updateOrderStatusUseCase.confirmOrder(orderId))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/confirm", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldShipOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        order.confirm();
        order.ship();
        String orderId = order.getId();

        when(updateOrderStatusUseCase.shipOrder(orderId))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/ship", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void shouldDeliverOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        order.confirm();
        order.ship();
        order.deliver();
        String orderId = order.getId();

        when(updateOrderStatusUseCase.deliverOrder(orderId))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/deliver", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void shouldCancelOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        order.cancel();
        String orderId = order.getId();

        when(updateOrderStatusUseCase.cancelOrder(orderId))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/cancel", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldAddItemToOrder() throws Exception {
        // Given
        OrderItem item1 = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        Order order = new Order("CUSTOMER-001", Arrays.asList(item1));
        String orderId = order.getId();
        
        OrderItem newItem = new OrderItem("PROD-002", "Producto 2", new Money(15.0), new Quantity(1));
        order.addItem(newItem);

        AddOrderItemRequest request = new AddOrderItemRequest(
            "PROD-002", "Producto 2", 15.0, 1
        );

        when(addOrderItemUseCase.addItemToOrder(eq(orderId), any(OrderItem.class)))
            .thenReturn(order);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/items", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    void shouldValidateCreateOrderRequest() throws Exception {
        // Given - Request inválido sin customerId
        CreateOrderRequest invalidRequest = new CreateOrderRequest("", Arrays.asList());

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }
}
