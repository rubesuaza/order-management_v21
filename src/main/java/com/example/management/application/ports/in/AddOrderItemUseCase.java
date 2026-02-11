package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderItem;

/**
 * Puerto de entrada para agregar un item a una orden.
 */
public interface AddOrderItemUseCase {
    /**
     * Agrega un item a una orden existente.
     * @param orderId El ID de la orden
     * @param item El item a agregar
     * @return La orden actualizada
     */
    Order addItemToOrder(String orderId, OrderItem item);
}
