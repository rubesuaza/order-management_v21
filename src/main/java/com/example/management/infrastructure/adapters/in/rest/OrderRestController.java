package com.example.management.infrastructure.adapters.in.rest;

import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.application.ports.in.*;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
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
        List<CreateOrderItemCommand> itemCommands = request.items().stream()
            .map(item -> new CreateOrderItemCommand(
                item.productId(),
                item.productName(),
                item.unitPrice(),
                item.quantity()
            ))
            .collect(Collectors.toList());
        
        Order order = createOrderUseCase.createOrder(request.customerId(), itemCommands);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return getOrderUseCase.getOrder(new OrderId(orderId))
            .map(order -> ResponseEntity.ok(OrderResponse.fromDomain(order)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        return executeStatusUpdate(() -> updateOrderStatusUseCase.confirmOrder(new OrderId(orderId)));
    }

    @PostMapping("/{orderId}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable String orderId) {
        return executeStatusUpdate(() -> updateOrderStatusUseCase.shipOrder(new OrderId(orderId)));
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable String orderId) {
        return executeStatusUpdate(() -> updateOrderStatusUseCase.deliverOrder(new OrderId(orderId)));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return executeStatusUpdate(() -> updateOrderStatusUseCase.cancelOrder(new OrderId(orderId)));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addItemToOrder(
            @PathVariable String orderId,
            @Valid @RequestBody AddOrderItemRequest request) {
        try {
            OrderItem item = toDomainItem(request);
            Order order = addOrderItemUseCase.addItemToOrder(new OrderId(orderId), item);
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<OrderResponse> executeStatusUpdate(java.util.function.Supplier<Order> statusUpdateOperation) {
        try {
            Order order = statusUpdateOperation.get();
            return ResponseEntity.ok(OrderResponse.fromDomain(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
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
