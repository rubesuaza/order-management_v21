package com.example.management.application.ports.in;

import com.example.management.application.dto.OrderDetailsDto;

/**
 * Puerto de entrada para actualizar el estado de una orden.
 */
public interface UpdateOrderStatusUseCase {
    /**
     * Confirma una orden.
     * @param orderId El ID de la orden como String
     * @return La orden confirmada
     */
    OrderDetailsDto confirmOrder(String orderId);

    /**
     * Envía una orden.
     * @param orderId El ID de la orden como String
     * @return La orden enviada
     */
    OrderDetailsDto shipOrder(String orderId);

    /**
     * Marca una orden como entregada.
     * @param orderId El ID de la orden como String
     * @return La orden entregada
     */
    OrderDetailsDto deliverOrder(String orderId);

    /**
     * Cancela una orden.
     * @param orderId El ID de la orden como String
     * @return La orden cancelada
     */
    OrderDetailsDto cancelOrder(String orderId);
}
