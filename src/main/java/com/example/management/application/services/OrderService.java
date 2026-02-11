package com.example.management.application.services;

import com.example.management.application.commands.AddOrderItemCommand;
import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.application.dto.OrderDetailsDto;
import com.example.management.application.dto.OrderItemDto;
import com.example.management.application.exception.ApplicationOrderNotFoundException;
import com.example.management.application.ports.in.*;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.exception.OrderNotFoundException;
import com.example.management.domain.model.Money;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import com.example.management.domain.model.OrderItem;
import com.example.management.domain.model.Quantity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de órdenes.
 */
public class OrderService implements CreateOrderUseCase, GetOrderUseCase, 
                                     UpdateOrderStatusUseCase, AddOrderItemUseCase {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDetailsDto createOrder(String customerId, List<CreateOrderItemCommand> itemCommands) {
        List<OrderItem> items = itemCommands.stream()
            .map(command -> new OrderItem(
                command.productId(),
                command.productName(),
                new Money(java.math.BigDecimal.valueOf(command.unitPrice())),
                new Quantity(command.quantity())
            ))
            .collect(Collectors.toList());
        
        Order order = new Order(customerId, items);
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    @Override
    public Optional<OrderDetailsDto> getOrder(String orderId) {
        return orderRepository.findById(orderId)
            .map(this::toOrderDetailsDto);
    }

    @Override
    public OrderDetailsDto confirmOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.confirm();
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto shipOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.ship();
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto deliverOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.deliver();
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto cancelOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    @Override
    public OrderDetailsDto addItemToOrder(String orderId, AddOrderItemCommand command) {
        Order order = findOrderOrThrow(orderId);
        OrderItem item = new OrderItem(
            command.productId(),
            command.productName(),
            new Money(command.unitPrice()),
            new Quantity(command.quantity())
        );
        order.addItem(item);
        Order savedOrder = orderRepository.save(order);
        return toOrderDetailsDto(savedOrder);
    }

    private Order findOrderOrThrow(String orderId) {
        try {
            return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada con ID: " + orderId));
        } catch (OrderNotFoundException e) {
            throw new ApplicationOrderNotFoundException("Orden no encontrada con ID: " + orderId);
        }
    }

    private OrderDetailsDto toOrderDetailsDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(this::toOrderItemDto)
            .collect(Collectors.toList());
        
        return new OrderDetailsDto(
            order.getId(),
            order.getCustomerId(),
            order.getStatus(),
            itemDtos,
            order.calculateTotal().getValue(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {
        return new OrderItemDto(
            item.getProductId(),
            item.getProductName(),
            item.getUnitPrice().getValue(),
            item.getQuantity().getValue(),
            item.calculateTotal().getValue()
        );
    }
}
