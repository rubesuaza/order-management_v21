package com.example.management.infrastructure.config;

import com.example.management.application.ports.in.*;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.OrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador de infraestructura que envuelve OrderService con gestión de transacciones.
 * Esto mantiene la capa de aplicación libre de dependencias de Spring.
 */
@org.springframework.stereotype.Service
public class TransactionalOrderService extends OrderService implements 
        CreateOrderUseCase, GetOrderUseCase, UpdateOrderStatusUseCase, AddOrderItemUseCase {

    public TransactionalOrderService(OrderRepository orderRepository) {
        super(orderRepository);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order createOrder(String customerId, 
            java.util.List<com.example.management.application.commands.CreateOrderItemCommand> itemCommands) {
        return super.createOrder(customerId, itemCommands);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<com.example.management.domain.model.Order> getOrder(
            com.example.management.domain.model.OrderId orderId) {
        return super.getOrder(orderId);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order confirmOrder(
            com.example.management.domain.model.OrderId orderId) {
        return super.confirmOrder(orderId);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order shipOrder(
            com.example.management.domain.model.OrderId orderId) {
        return super.shipOrder(orderId);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order deliverOrder(
            com.example.management.domain.model.OrderId orderId) {
        return super.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order cancelOrder(
            com.example.management.domain.model.OrderId orderId) {
        return super.cancelOrder(orderId);
    }

    @Override
    @Transactional
    public com.example.management.domain.model.Order addItemToOrder(
            com.example.management.domain.model.OrderId orderId,
            com.example.management.domain.model.OrderItem item) {
        return super.addItemToOrder(orderId, item);
    }
}
