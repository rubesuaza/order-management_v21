package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

/**
 * Puerto de entrada para actualizar el estado de una orden.
 */
public interface UpdateOrderStatusUseCase {
    /**
     * Confirma una orden.
     * @param orderId El ID de la orden
     * @return La orden confirmada
     */
    Order confirmOrder(String orderId);

    /**
     * Envía una orden.
     * @param orderId El ID de la orden
     * @return La orden enviada
     */
    Order shipOrder(String orderId);

    /**
     * Marca una orden como entregada.
     * @param orderId El ID de la orden
     * @return La orden entregada
     */
    Order deliverOrder(String orderId);

    /**
     * Cancela una orden.
     * @param orderId El ID de la orden
     * @return La orden cancelada
     */
    Order cancelOrder(String orderId);
}
