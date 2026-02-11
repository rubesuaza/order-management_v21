package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;

/**
 * Puerto de entrada para actualizar el estado de una orden.
 */
public interface UpdateOrderStatusUseCase {
    /**
     * Confirma una orden.
     * @param orderId El ID de la orden
     * @return La orden confirmada
     */
    Order confirmOrder(OrderId orderId);

    /**
     * Envía una orden.
     * @param orderId El ID de la orden
     * @return La orden enviada
     */
    Order shipOrder(OrderId orderId);

    /**
     * Marca una orden como entregada.
     * @param orderId El ID de la orden
     * @return La orden entregada
     */
    Order deliverOrder(OrderId orderId);

    /**
     * Cancela una orden.
     * @param orderId El ID de la orden
     * @return La orden cancelada
     */
    Order cancelOrder(OrderId orderId);
}
