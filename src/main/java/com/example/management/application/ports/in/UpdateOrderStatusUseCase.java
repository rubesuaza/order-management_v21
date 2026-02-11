package com.example.management.application.ports.in;

import com.example.management.application.dto.OrderDetailsDto;
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
    OrderDetailsDto confirmOrder(OrderId orderId);

    /**
     * Envía una orden.
     * @param orderId El ID de la orden
     * @return La orden enviada
     */
    OrderDetailsDto shipOrder(OrderId orderId);

    /**
     * Marca una orden como entregada.
     * @param orderId El ID de la orden
     * @return La orden entregada
     */
    OrderDetailsDto deliverOrder(OrderId orderId);

    /**
     * Cancela una orden.
     * @param orderId El ID de la orden
     * @return La orden cancelada
     */
    OrderDetailsDto cancelOrder(OrderId orderId);
}
