package com.example.management.infrastructure.config;

import com.example.management.application.commands.AddOrderItemCommand;
import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.application.dto.OrderDetailsDto;
import com.example.management.application.ports.in.*;
import com.example.management.application.services.OrderService;
import com.example.management.domain.model.OrderId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de infraestructura que envuelve OrderService con gestión de transacciones.
 * Esto mantiene la capa de aplicación libre de dependencias de Spring.
 * Implementa el patrón Decorator para añadir comportamiento transaccional sin modificar
 * la lógica de negocio en OrderService.
 */
@Service
public class TransactionalOrderService implements 
        CreateOrderUseCase, GetOrderUseCase, UpdateOrderStatusUseCase, AddOrderItemUseCase {

    private final OrderService orderService;

    public TransactionalOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public OrderDetailsDto createOrder(String customerId, List<CreateOrderItemCommand> itemCommands) {
        return orderService.createOrder(customerId, itemCommands);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDetailsDto> getOrder(OrderId orderId) {
        return orderService.getOrder(orderId);
    }

    @Override
    @Transactional
    public OrderDetailsDto confirmOrder(OrderId orderId) {
        return orderService.confirmOrder(orderId);
    }

    @Override
    @Transactional
    public OrderDetailsDto shipOrder(OrderId orderId) {
        return orderService.shipOrder(orderId);
    }

    @Override
    @Transactional
    public OrderDetailsDto deliverOrder(OrderId orderId) {
        return orderService.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public OrderDetailsDto cancelOrder(OrderId orderId) {
        return orderService.cancelOrder(orderId);
    }

    @Override
    @Transactional
    public OrderDetailsDto addItemToOrder(OrderId orderId, AddOrderItemCommand command) {
        return orderService.addItemToOrder(orderId, command);
    }
}
