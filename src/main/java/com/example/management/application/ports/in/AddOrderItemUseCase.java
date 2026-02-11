package com.example.management.application.ports.in;

import com.example.management.application.commands.AddOrderItemCommand;
import com.example.management.application.dto.OrderDetailsDto;

/**
 * Puerto de entrada para agregar un item a una orden.
 */
public interface AddOrderItemUseCase {
    /**
     * Agrega un item a una orden existente.
     * @param orderId El ID de la orden como String
     * @param command El comando con los datos del item a agregar
     * @return La orden actualizada
     */
    OrderDetailsDto addItemToOrder(String orderId, AddOrderItemCommand command);
}
