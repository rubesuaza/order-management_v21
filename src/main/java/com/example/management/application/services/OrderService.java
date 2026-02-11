package com.example.management.application.services;

import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.application.ports.in.*;
import com.example.management.application.ports.out.OrderRepository;
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
    public Order createOrder(String customerId, List<CreateOrderItemCommand> itemCommands) {
        List<OrderItem> items = itemCommands.stream()
            .map(command -> new OrderItem(
                command.productId(),
                command.productName(),
                new Money(command.unitPrice()),
                new Quantity(command.quantity())
            ))
            .collect(Collectors.toList());
        
        Order order = new Order(customerId, items);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrder(OrderId orderId) {
        return orderRepository.findById(orderId.getValue());
    }

    @Override
    public Order confirmOrder(OrderId orderId) {
        Order order = findOrderOrThrow(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    @Override
    public Order shipOrder(OrderId orderId) {
        Order order = findOrderOrThrow(orderId);
        order.ship();
        return orderRepository.save(order);
    }

    @Override
    public Order deliverOrder(OrderId orderId) {
        Order order = findOrderOrThrow(orderId);
        order.deliver();
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(OrderId orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
        return orderRepository.save(order);
    }

    @Override
    public Order addItemToOrder(OrderId orderId, OrderItem item) {
        Order order = findOrderOrThrow(orderId);
        order.addItem(item);
        return orderRepository.save(order);
    }

    private Order findOrderOrThrow(OrderId orderId) {
        return orderRepository.findById(orderId.getValue())
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + orderId.getValue()));
    }
}
