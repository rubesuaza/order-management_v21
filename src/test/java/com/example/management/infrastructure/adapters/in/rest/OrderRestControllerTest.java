package com.example.management.infrastructure.adapters.in.rest;

import com.example.management.application.ports.in.*;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.Quantity;
import com.example.management.infrastructure.adapters.in.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    private OrderItem testItem;
    private Order testOrder;
    private String testOrderId;
    private String testCustomerId;

    @BeforeEach
    void setUp() {
        testCustomerId = "CUSTOMER-001";
        testItem = new OrderItem("PROD-001", "Producto 1", new Money(10.0), new Quantity(2));
        testOrder = new Order(testCustomerId, Arrays.asList(testItem));
        testOrderId = testOrder.getId();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest(
            testCustomerId,
            Arrays.asList(new OrderItemRequest("PROD-001", "Producto 1", 10.0, 2))
        );

        when(createOrderUseCase.createOrder(any(String.class), any(List.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.customerId").value(testCustomerId))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].productId").value("PROD-001"));
    }

    @Test
    void shouldGetOrder() throws Exception {
        // Given
        when(getOrderUseCase.getOrder(any(OrderId.class)))
            .thenReturn(Optional.of(testOrder));

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testOrderId))
            .andExpect(jsonPath("$.customerId").value(testCustomerId));
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        // Given
        when(getOrderUseCase.getOrder(any(OrderId.class)))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", "NON-EXISTENT"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldConfirmOrder() throws Exception {
        // Given
        testOrder.confirm();
        when(updateOrderStatusUseCase.confirmOrder(any(OrderId.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/confirm", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldShipOrder() throws Exception {
        // Given
        testOrder.confirm();
        testOrder.ship();
        when(updateOrderStatusUseCase.shipOrder(any(OrderId.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/ship", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void shouldDeliverOrder() throws Exception {
        // Given
        testOrder.confirm();
        testOrder.ship();
        testOrder.deliver();
        when(updateOrderStatusUseCase.deliverOrder(any(OrderId.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/deliver", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void shouldCancelOrder() throws Exception {
        // Given
        testOrder.cancel();
        when(updateOrderStatusUseCase.cancelOrder(any(OrderId.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/cancel", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldAddItemToOrder() throws Exception {
        // Given
        OrderItem newItem = new OrderItem("PROD-002", "Producto 2", new Money(15.0), new Quantity(1));
        testOrder.addItem(newItem);

        AddOrderItemRequest request = new AddOrderItemRequest(
            "PROD-002", "Producto 2", 15.0, 1
        );

        when(addOrderItemUseCase.addItemToOrder(any(OrderId.class), any(OrderItem.class)))
            .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/items", testOrderId)
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
