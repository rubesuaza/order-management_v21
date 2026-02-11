package com.example.management.infrastructure.config;

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
}
