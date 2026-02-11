package com.example.management.infrastructure.config;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.application.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuración de Spring para la capa de infraestructura.
 * Maneja el escaneo de componentes y la configuración de transacciones.
 * TransactionalOrderService se registra automáticamente mediante @Service.
 */
@Configuration
@ComponentScan(basePackages = {
    "com.example.management.infrastructure.adapters.out.persistence",
    "com.example.management.infrastructure.adapters.in.rest",
    "com.example.management.infrastructure.config"
})
@EnableTransactionManagement
public class ApplicationConfig {
    
    /**
     * Crea una instancia de OrderService para ser inyectada en TransactionalOrderService.
     * Esto mantiene la capa de aplicación libre de anotaciones de Spring.
     */
    @Bean
    public OrderService orderService(OrderRepository orderRepository) {
        return new OrderService(orderRepository);
    }
}
