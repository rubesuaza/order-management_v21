package com.example.management.application.ports.in;

import com.example.management.application.dto.OrderDetailsDto;
import java.util.Optional;

/**
 * Puerto de entrada para obtener una orden.
 */
public interface GetOrderUseCase {
    /**
     * Obtiene una orden por su ID.
     * @param orderId El ID de la orden como String
     * @return La orden si existe, Optional vacío en caso contrario
     */
    Optional<OrderDetailsDto> getOrder(String orderId);
}
