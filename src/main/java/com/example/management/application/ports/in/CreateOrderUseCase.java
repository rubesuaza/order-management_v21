package com.example.management.application.ports.in;

import com.example.management.application.commands.CreateOrderItemCommand;
import com.example.management.domain.model.Order;
import java.util.List;

/**
 * Puerto de entrada para crear una nueva orden.
 */
public interface CreateOrderUseCase {
    /**
     * Crea una nueva orden.
     * @param customerId El ID del cliente
     * @param itemCommands La lista de comandos para crear items de la orden
     * @return La orden creada
     */
    Order createOrder(String customerId, List<CreateOrderItemCommand> itemCommands);
}
