package com.example.management.infrastructure.adapters.in.rest;

import com.example.management.application.commands.AddOrderItemCommand;
import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.application.dto.OrderDetailsDto;
import com.example.management.application.dto.OrderItemDto;
import com.example.management.application.ports.in.*;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderStatus;
import com.example.management.infrastructure.adapters.in.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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

    private OrderDetailsDto testOrderDto;
    private OrderDetailsDto confirmedOrderDto;
    private OrderDetailsDto shippedOrderDto;
    private OrderDetailsDto deliveredOrderDto;
    private OrderDetailsDto cancelledOrderDto;
    private OrderDetailsDto orderWithTwoItemsDto;
    private String testOrderId;
    private String testCustomerId;

    @BeforeEach
    void setUp() {
        testCustomerId = "CUSTOMER-001";
        testOrderId = "ORDER-001";
        
        OrderItemDto testItemDto = new OrderItemDto(
            "PROD-001", "Producto 1", BigDecimal.valueOf(10.0), 2, BigDecimal.valueOf(20.0)
        );
        
        testOrderDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.PENDING,
            Arrays.asList(testItemDto),
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        confirmedOrderDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.CONFIRMED,
            Arrays.asList(testItemDto),
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        shippedOrderDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.SHIPPED,
            Arrays.asList(testItemDto),
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        deliveredOrderDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.DELIVERED,
            Arrays.asList(testItemDto),
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        cancelledOrderDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.CANCELLED,
            Arrays.asList(testItemDto),
            BigDecimal.valueOf(20.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        OrderItemDto secondItemDto = new OrderItemDto(
            "PROD-002", "Producto 2", BigDecimal.valueOf(15.0), 1, BigDecimal.valueOf(15.0)
        );
        orderWithTwoItemsDto = new OrderDetailsDto(
            testOrderId, testCustomerId, OrderStatus.PENDING,
            Arrays.asList(testItemDto, secondItemDto),
            BigDecimal.valueOf(35.0),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        CreateOrderRequest request = new CreateOrderRequest(
            testCustomerId,
            Arrays.asList(new OrderItemRequest("PROD-001", "Producto 1", 10.0, 2))
        );

        when(createOrderUseCase.createOrder(any(String.class), any(List.class)))
            .thenReturn(testOrderDto);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(testOrderId))
            .andExpect(jsonPath("$.customerId").value(testCustomerId))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].productId").value("PROD-001"));
    }

    @Test
    void shouldGetOrder() throws Exception {
        // Given
        when(getOrderUseCase.getOrder(any(OrderId.class)))
            .thenReturn(Optional.of(testOrderDto));

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
        when(updateOrderStatusUseCase.confirmOrder(any(OrderId.class)))
            .thenReturn(confirmedOrderDto);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/confirm", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldShipOrder() throws Exception {
        // Given
        when(updateOrderStatusUseCase.shipOrder(any(OrderId.class)))
            .thenReturn(shippedOrderDto);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/ship", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void shouldDeliverOrder() throws Exception {
        // Given
        when(updateOrderStatusUseCase.deliverOrder(any(OrderId.class)))
            .thenReturn(deliveredOrderDto);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/deliver", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void shouldCancelOrder() throws Exception {
        // Given
        when(updateOrderStatusUseCase.cancelOrder(any(OrderId.class)))
            .thenReturn(cancelledOrderDto);

        // When & Then
        mockMvc.perform(post("/api/orders/{orderId}/cancel", testOrderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldAddItemToOrder() throws Exception {
        // Given
        AddOrderItemRequest request = new AddOrderItemRequest(
            "PROD-002", "Producto 2", 15.0, 1
        );

        when(addOrderItemUseCase.addItemToOrder(any(OrderId.class), any(AddOrderItemCommand.class)))
            .thenReturn(orderWithTwoItemsDto);

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
