package com.example.management.application.services;

import com.example.management.application.ports.in.*;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de órdenes.
 */
@Service
@Transactional
public class OrderService implements CreateOrderUseCase, GetOrderUseCase, 
                                     UpdateOrderStatusUseCase, AddOrderItemUseCase {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(String customerId, List<OrderItem> items) {
        Order order = new Order(customerId, items);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order confirmOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    @Override
    public Order shipOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.ship();
        return orderRepository.save(order);
    }

    @Override
    public Order deliverOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.deliver();
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(String orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
        return orderRepository.save(order);
    }

    @Override
    public Order addItemToOrder(String orderId, OrderItem item) {
        Order order = findOrderOrThrow(orderId);
        order.addItem(item);
        return orderRepository.save(order);
    }

    private Order findOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + orderId));
    }
}
