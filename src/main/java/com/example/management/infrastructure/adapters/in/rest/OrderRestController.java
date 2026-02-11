package com.example.management.infrastructure.adapters.in.rest;

import com.example.management.application.ports.in.*;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.Quantity;
import com.example.management.infrastructure.adapters.in.rest.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de órdenes.
 * Adaptador de entrada que expone los endpoints HTTP.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final AddOrderItemUseCase addOrderItemUseCase;

    public OrderRestController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            UpdateOrderStatusUseCase updateOrderStatusUseCase,
            AddOrderItemUseCase addOrderItemUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.addOrderItemUseCase = addOrderItemUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        List<OrderItem> items = request.items().stream()
            .map(this::toDomainItem)
            .collect(Collectors.toList());
        
        Order order = createOrderUseCase.createOrder(request.customerId(), items);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(orderId)
            .map(order -> ResponseEntity.ok(OrderResponse.fromDomain(order)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        try {
            Order order = updateOrderStatusUseCase.confirmOrder(orderId);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orderId}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable String orderId) {
        try {
            Order order = updateOrderStatusUseCase.shipOrder(orderId);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable String orderId) {
        try {
            Order order = updateOrderStatusUseCase.deliverOrder(orderId);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        try {
            Order order = updateOrderStatusUseCase.cancelOrder(orderId);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(
            @PathVariable String orderId,
            @Valid @RequestBody AddOrderItemRequest request) {
        try {
            OrderItem item = toDomainItem(request);
            Order order = addOrderItemUseCase.addItemToOrder(orderId, item);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private OrderItem toDomainItem(OrderItemRequest request) {
        return new OrderItem(
            request.productId(),
            request.productName(),
            new Money(request.unitPrice()),
            new Quantity(request.quantity())
        );
    }

    private OrderItem toDomainItem(AddOrderItemRequest request) {
        return new OrderItem(
            request.productId(),
            request.productName(),
            new Money(request.unitPrice()),
            new Quantity(request.quantity())
        );
    }
}
