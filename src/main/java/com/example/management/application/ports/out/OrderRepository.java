package com.example.management.application.ports.out;

import com.example.management.domain.model.Order;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de órdenes.
 */
public interface OrderRepository {
    /**
     * Guarda una orden.
     * @param order La orden a guardar
     * @return La orden guardada
     */
    Order save(Order order);

    /**
     * Busca una orden por su ID.
     * @param orderId El ID de la orden
     * @return La orden si existe, Optional vacío en caso contrario
     */
    Optional<Order> findById(String orderId);

    /**
     * Verifica si existe una orden con el ID dado.
     * @param orderId El ID de la orden
     * @return true si existe, false en caso contrario
     */
    boolean existsById(String orderId);
}
