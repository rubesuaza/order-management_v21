package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderId;
import java.util.Optional;

/**
 * Puerto de entrada para obtener una orden.
 */
public interface GetOrderUseCase {
    /**
     * Obtiene una orden por su ID.
     * @param orderId El ID de la orden
     * @return La orden si existe, Optional vacío en caso contrario
     */
    Optional<Order> getOrder(OrderId orderId);
}
